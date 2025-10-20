package com.libreshelf.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "books",
    foreignKeys = [
        ForeignKey(
            entity = Library::class,
            parentColumns = ["id"],
            childColumns = ["libraryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("libraryId")]
)
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val libraryId: Long,
    val title: String,
    val author: String = "",
    val series: String = "",
    val seriesNumber: Float? = null,
    val publisher: String = "",
    val publishedDate: String = "",
    val description: String = "",
    val isbn: String = "",
    val language: String = "",
    val pageCount: Int = 0,
    val filePath: String,
    val fileFormat: BookFormat,
    val fileSize: Long = 0,
    val coverPath: String? = null,
    val addedAt: Long = System.currentTimeMillis(),
    val lastOpenedAt: Long? = null,
    val lastReadPosition: Int = 0,
    val readingProgress: Float = 0f,
    val isFinished: Boolean = false,
    val isFavorite: Boolean = false,
    val rating: Float = 0f,
    val tags: String = "", // Comma-separated tags
    val notes: String = ""
)

enum class BookFormat {
    EPUB,
    PDF,
    CBZ,
    CBR,
    UNKNOWN
}
