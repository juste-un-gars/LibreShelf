package com.libreshelf.di

import android.content.Context
import com.libreshelf.domain.reader.ComicReader
import com.libreshelf.domain.reader.EpubReader
import com.libreshelf.domain.reader.PdfReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReaderModule {

    @Provides
    @Singleton
    fun provideEpubReader(@ApplicationContext context: Context): EpubReader {
        return EpubReader(context)
    }

    @Provides
    @Singleton
    fun providePdfReader(@ApplicationContext context: Context): PdfReader {
        return PdfReader(context)
    }

    @Provides
    @Singleton
    fun provideComicReader(@ApplicationContext context: Context): ComicReader {
        return ComicReader(context)
    }
}
