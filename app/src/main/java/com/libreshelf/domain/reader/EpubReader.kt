package com.libreshelf.domain.reader

import android.content.Context
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubReader as EpubLibReader
import java.io.FileInputStream
import javax.inject.Inject

class EpubReader @Inject constructor(
    private val context: Context
) {
    fun loadBook(filePath: String): Book {
        val inputStream = FileInputStream(filePath)
        val epubReader = EpubLibReader()
        return epubReader.readEpub(inputStream)
    }

    fun getChapterContent(book: Book, chapterIndex: Int): String {
        if (chapterIndex < 0 || chapterIndex >= book.spine.spineReferences.size) {
            return ""
        }

        val spineReference = book.spine.spineReferences[chapterIndex]
        val resource = spineReference.resource

        return String(resource.data)
    }

    fun getTotalChapters(book: Book): Int {
        return book.spine.spineReferences.size
    }

    fun getTableOfContents(book: Book): List<TOCItem> {
        val tocItems = mutableListOf<TOCItem>()
        book.tableOfContents.tocReferences.forEachIndexed { index, tocReference ->
            tocItems.add(
                TOCItem(
                    title = tocReference.title ?: "Chapter ${index + 1}",
                    chapterIndex = index
                )
            )
        }
        return tocItems
    }

    fun getMetadata(book: Book): EpubMetadata {
        return EpubMetadata(
            title = book.title ?: "",
            author = book.metadata.authors.joinToString(", ") { "${it.firstname} ${it.lastname}" },
            publisher = book.metadata.publishers.firstOrNull() ?: "",
            description = book.metadata.descriptions.firstOrNull() ?: "",
            language = book.metadata.language ?: "",
            isbn = book.metadata.identifiers.find { it.scheme == "ISBN" }?.value ?: ""
        )
    }
}

data class TOCItem(
    val title: String,
    val chapterIndex: Int
)

data class EpubMetadata(
    val title: String,
    val author: String,
    val publisher: String,
    val description: String,
    val language: String,
    val isbn: String
)
