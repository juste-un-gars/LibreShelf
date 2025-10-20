package com.libreshelf.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.libreshelf.data.model.*

@Database(
    entities = [
        Library::class,
        Book::class,
        Bookmark::class,
        ReadingSession::class,
        NetworkSource::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class LibreShelfDatabase : RoomDatabase() {
    abstract fun libraryDao(): LibraryDao
    abstract fun bookDao(): BookDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun readingSessionDao(): ReadingSessionDao
    abstract fun networkSourceDao(): NetworkSourceDao

    companion object {
        const val DATABASE_NAME = "libreshelf_db"
    }
}
