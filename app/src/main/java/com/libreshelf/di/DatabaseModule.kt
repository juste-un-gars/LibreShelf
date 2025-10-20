package com.libreshelf.di

import android.content.Context
import androidx.room.Room
import com.libreshelf.data.local.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LibreShelfDatabase {
        return Room.databaseBuilder(
            context,
            LibreShelfDatabase::class.java,
            LibreShelfDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideLibraryDao(database: LibreShelfDatabase): LibraryDao {
        return database.libraryDao()
    }

    @Provides
    @Singleton
    fun provideBookDao(database: LibreShelfDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    @Singleton
    fun provideBookmarkDao(database: LibreShelfDatabase): BookmarkDao {
        return database.bookmarkDao()
    }

    @Provides
    @Singleton
    fun provideReadingSessionDao(database: LibreShelfDatabase): ReadingSessionDao {
        return database.readingSessionDao()
    }

    @Provides
    @Singleton
    fun provideNetworkSourceDao(database: LibreShelfDatabase): NetworkSourceDao {
        return database.networkSourceDao()
    }
}
