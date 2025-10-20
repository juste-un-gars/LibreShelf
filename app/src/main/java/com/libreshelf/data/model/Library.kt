package com.libreshelf.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "libraries")
data class Library(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val color: String = "#6200EE",
    val icon: String = "book",
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isLocked: Boolean = false,
    val lockPin: String? = null
)
