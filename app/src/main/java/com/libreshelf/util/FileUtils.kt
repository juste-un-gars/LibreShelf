package com.libreshelf.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.libreshelf.data.model.BookFormat
import java.io.File
import java.io.FileOutputStream

object FileUtils {

    fun getFileExtension(fileName: String): String {
        return fileName.substringAfterLast('.', "").lowercase()
    }

    fun getBookFormat(fileName: String): BookFormat {
        return when (getFileExtension(fileName)) {
            "epub" -> BookFormat.EPUB
            "pdf" -> BookFormat.PDF
            "cbz" -> BookFormat.CBZ
            "cbr" -> BookFormat.CBR
            else -> BookFormat.UNKNOWN
        }
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String {
        var fileName = "unknown"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }

    fun getFileSizeFromUri(context: Context, uri: Uri): Long {
        var fileSize = 0L
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                if (sizeIndex != -1) {
                    fileSize = it.getLong(sizeIndex)
                }
            }
        }
        return fileSize
    }

    fun copyFileFromUri(context: Context, uri: Uri, destinationFile: File): Boolean {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return false
            val outputStream = FileOutputStream(destinationFile)

            inputStream.copyTo(outputStream)

            outputStream.close()
            inputStream.close()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
            else -> "${bytes / (1024 * 1024 * 1024)} GB"
        }
    }

    fun isBookFile(fileName: String): Boolean {
        val extension = getFileExtension(fileName)
        return extension in listOf("epub", "pdf", "cbz", "cbr")
    }

    fun getLibraryDirectory(context: Context): File {
        val libraryDir = File(context.filesDir, "library")
        if (!libraryDir.exists()) {
            libraryDir.mkdirs()
        }
        return libraryDir
    }

    fun getCoversDirectory(context: Context): File {
        val coversDir = File(context.filesDir, "covers")
        if (!coversDir.exists()) {
            coversDir.mkdirs()
        }
        return coversDir
    }
}
