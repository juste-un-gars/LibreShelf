package com.libreshelf.data.repository

import com.libreshelf.data.local.LibraryDao
import com.libreshelf.data.model.Library
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryRepository @Inject constructor(
    private val libraryDao: LibraryDao
) {
    fun getAllLibraries(): Flow<List<Library>> = libraryDao.getAllLibraries()

    fun getLibraryById(id: Long): Flow<Library?> = libraryDao.getLibraryById(id)

    suspend fun insertLibrary(library: Library): Long = libraryDao.insertLibrary(library)

    suspend fun updateLibrary(library: Library) = libraryDao.updateLibrary(library)

    suspend fun deleteLibrary(library: Library) = libraryDao.deleteLibrary(library)

    suspend fun getLibraryCount(): Int = libraryDao.getLibraryCount()
}
