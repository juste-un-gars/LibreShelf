package com.libreshelf.domain.reader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.InputStream
import java.util.zip.ZipFile
import javax.inject.Inject

class ComicReader @Inject constructor(
    private val context: Context
) {
    private var currentArchive: ZipFile? = null
    private var imageEntries: List<String> = emptyList()

    fun loadComic(filePath: String): ComicBook {
        closeComic()

        val file = File(filePath)
        currentArchive = ZipFile(file)

        // Get all image entries and sort them naturally
        imageEntries = currentArchive!!.entries()
            .toList()
            .filter { entry ->
                !entry.isDirectory && isImageFile(entry.name)
            }
            .map { it.name }
            .sortedWith(NaturalOrderComparator())

        return ComicBook(
            pageCount = imageEntries.size,
            filePath = filePath
        )
    }

    fun getPage(pageIndex: Int): Bitmap? {
        val archive = currentArchive ?: return null

        if (pageIndex < 0 || pageIndex >= imageEntries.size) {
            return null
        }

        val entryName = imageEntries[pageIndex]
        val entry = archive.getEntry(entryName) ?: return null

        val inputStream: InputStream = archive.getInputStream(entry)
        return BitmapFactory.decodeStream(inputStream)
    }

    fun getPageCount(): Int {
        return imageEntries.size
    }

    fun closeComic() {
        currentArchive?.close()
        currentArchive = null
        imageEntries = emptyList()
    }

    private fun isImageFile(fileName: String): Boolean {
        val extension = fileName.substringAfterLast('.', "").lowercase()
        return extension in listOf("jpg", "jpeg", "png", "gif", "webp", "bmp")
    }

    // Natural order comparator for proper page ordering (1, 2, 10 instead of 1, 10, 2)
    private class NaturalOrderComparator : Comparator<String> {
        override fun compare(a: String, b: String): Int {
            val aChunks = a.split(Regex("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)"))
            val bChunks = b.split(Regex("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)"))

            for (i in 0 until minOf(aChunks.size, bChunks.size)) {
                val aChunk = aChunks[i]
                val bChunk = bChunks[i]

                val aNum = aChunk.toIntOrNull()
                val bNum = bChunk.toIntOrNull()

                val result = when {
                    aNum != null && bNum != null -> aNum.compareTo(bNum)
                    else -> aChunk.compareTo(bChunk)
                }

                if (result != 0) return result
            }

            return aChunks.size.compareTo(bChunks.size)
        }
    }
}

data class ComicBook(
    val pageCount: Int,
    val filePath: String
)
