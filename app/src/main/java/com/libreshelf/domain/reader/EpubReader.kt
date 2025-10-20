package com.libreshelf.domain.reader

import android.content.Context
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.File
import java.io.InputStream
import java.util.zip.ZipFile
import javax.inject.Inject

/**
 * Simple EPUB reader implementation using built-in Android XML parsers.
 * EPUB is essentially a ZIP file containing HTML/XHTML files and metadata in XML format.
 */
class EpubReader @Inject constructor(
    private val context: Context
) {
    private var currentZipFile: ZipFile? = null
    private var opfPath: String = ""
    private var opfBasePath: String = ""

    fun loadBook(filePath: String): EpubBook {
        closeBook()

        val file = File(filePath)
        currentZipFile = ZipFile(file)

        // Step 1: Read container.xml to find OPF file location
        opfPath = findOpfPath()
        opfBasePath = opfPath.substringBeforeLast('/')

        // Step 2: Parse OPF file for metadata and spine
        val metadata = parseMetadata()
        val spine = parseSpine()

        return EpubBook(
            title = metadata.title,
            author = metadata.author,
            chapters = spine,
            filePath = filePath
        )
    }

    private fun findOpfPath(): String {
        val zipFile = currentZipFile ?: return ""

        val containerEntry = zipFile.getEntry("META-INF/container.xml")
            ?: return "content.opf" // Fallback

        val inputStream = zipFile.getInputStream(containerEntry)
        val parser = Xml.newPullParser()
        parser.setInput(inputStream, "UTF-8")

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == "rootfile") {
                val fullPath = parser.getAttributeValue(null, "full-path")
                if (fullPath != null) {
                    inputStream.close()
                    return fullPath
                }
            }
            eventType = parser.next()
        }

        inputStream.close()
        return "content.opf"
    }

    private fun parseMetadata(): EpubMetadata {
        val zipFile = currentZipFile ?: return EpubMetadata()

        val opfEntry = zipFile.getEntry(opfPath) ?: return EpubMetadata()
        val inputStream = zipFile.getInputStream(opfEntry)

        var title = ""
        var author = ""
        var publisher = ""
        var description = ""
        var language = ""
        var isbn = ""

        try {
            val parser = Xml.newPullParser()
            parser.setInput(inputStream, "UTF-8")

            var eventType = parser.eventType
            var inMetadata = false

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "metadata" -> inMetadata = true
                            "dc:title", "title" -> if (inMetadata) {
                                title = parser.nextText()
                            }
                            "dc:creator", "creator" -> if (inMetadata) {
                                author = parser.nextText()
                            }
                            "dc:publisher", "publisher" -> if (inMetadata) {
                                publisher = parser.nextText()
                            }
                            "dc:description", "description" -> if (inMetadata) {
                                description = parser.nextText()
                            }
                            "dc:language", "language" -> if (inMetadata) {
                                language = parser.nextText()
                            }
                            "dc:identifier", "identifier" -> if (inMetadata) {
                                val scheme = parser.getAttributeValue(null, "opf:scheme")
                                    ?: parser.getAttributeValue(null, "scheme")
                                if (scheme == "ISBN") {
                                    isbn = parser.nextText()
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "metadata") {
                            inMetadata = false
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream.close()
        }

        return EpubMetadata(
            title = title.ifEmpty { "Unknown" },
            author = author.ifEmpty { "Unknown" },
            publisher = publisher,
            description = description,
            language = language,
            isbn = isbn
        )
    }

    private fun parseSpine(): List<ChapterInfo> {
        val zipFile = currentZipFile ?: return emptyList()

        val opfEntry = zipFile.getEntry(opfPath) ?: return emptyList()
        val inputStream = zipFile.getInputStream(opfEntry)

        val manifestMap = mutableMapOf<String, String>() // id -> href
        val spineList = mutableListOf<String>() // list of idref

        try {
            val parser = Xml.newPullParser()
            parser.setInput(inputStream, "UTF-8")

            var eventType = parser.eventType
            var inManifest = false
            var inSpine = false

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "manifest" -> inManifest = true
                            "spine" -> inSpine = true
                            "item" -> if (inManifest) {
                                val id = parser.getAttributeValue(null, "id")
                                val href = parser.getAttributeValue(null, "href")
                                if (id != null && href != null) {
                                    manifestMap[id] = href
                                }
                            }
                            "itemref" -> if (inSpine) {
                                val idref = parser.getAttributeValue(null, "idref")
                                if (idref != null) {
                                    spineList.add(idref)
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        when (parser.name) {
                            "manifest" -> inManifest = false
                            "spine" -> inSpine = false
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream.close()
        }

        // Build chapter list from spine
        return spineList.mapIndexedNotNull { index, idref ->
            val href = manifestMap[idref]
            if (href != null) {
                val fullPath = if (opfBasePath.isNotEmpty()) {
                    "$opfBasePath/$href"
                } else {
                    href
                }
                ChapterInfo(
                    index = index,
                    title = "Chapter ${index + 1}",
                    href = fullPath
                )
            } else {
                null
            }
        }
    }

    fun getChapterContent(book: EpubBook, chapterIndex: Int): String {
        if (chapterIndex < 0 || chapterIndex >= book.chapters.size) {
            return ""
        }

        val zipFile = currentZipFile ?: return ""
        val chapter = book.chapters[chapterIndex]

        val entry = zipFile.getEntry(chapter.href) ?: return ""
        val inputStream = zipFile.getInputStream(entry)

        val content = inputStream.bufferedReader().use { it.readText() }
        inputStream.close()

        return content
    }

    fun getTotalChapters(book: EpubBook): Int {
        return book.chapters.size
    }

    fun getTableOfContents(book: EpubBook): List<TOCItem> {
        return book.chapters.map { chapter ->
            TOCItem(
                title = chapter.title,
                chapterIndex = chapter.index
            )
        }
    }

    fun getMetadata(book: EpubBook): EpubMetadata {
        return EpubMetadata(
            title = book.title,
            author = book.author,
            publisher = "",
            description = "",
            language = "",
            isbn = ""
        )
    }

    fun closeBook() {
        currentZipFile?.close()
        currentZipFile = null
    }
}

data class EpubBook(
    val title: String,
    val author: String,
    val chapters: List<ChapterInfo>,
    val filePath: String
)

data class ChapterInfo(
    val index: Int,
    val title: String,
    val href: String
)

data class TOCItem(
    val title: String,
    val chapterIndex: Int
)

data class EpubMetadata(
    val title: String = "",
    val author: String = "",
    val publisher: String = "",
    val description: String = "",
    val language: String = "",
    val isbn: String = ""
)
