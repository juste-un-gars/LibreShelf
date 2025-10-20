package com.libreshelf.data.repository

import com.libreshelf.data.local.BookDao
import com.libreshelf.data.model.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {
    fun getBooksByLibrary(libraryId: Long): Flow<List<Book>> = bookDao.getBooksByLibrary(libraryId)

    fun getBookById(id: Long): Flow<Book?> = bookDao.getBookById(id)

    fun getRecentBooks(): Flow<List<Book>> = bookDao.getRecentBooks()

    fun getFavoriteBooks(): Flow<List<Book>> = bookDao.getFavoriteBooks()

    fun getBooksInProgress(): Flow<List<Book>> = bookDao.getBooksInProgress()

    fun getFinishedBooks(): Flow<List<Book>> = bookDao.getFinishedBooks()

    fun searchBooks(query: String): Flow<List<Book>> = bookDao.searchBooks(query)

    suspend fun insertBook(book: Book): Long = bookDao.insertBook(book)

    suspend fun updateBook(book: Book) = bookDao.updateBook(book)

    suspend fun deleteBook(book: Book) = bookDao.deleteBook(book)

    suspend fun updateReadingProgress(bookId: Long, position: Int, progress: Float) {
        bookDao.updateReadingProgress(bookId, position, progress, System.currentTimeMillis())
    }

    suspend fun updateFavoriteStatus(bookId: Long, isFavorite: Boolean) {
        bookDao.updateFavoriteStatus(bookId, isFavorite)
    }

    suspend fun getBookCountByLibrary(libraryId: Long): Int = bookDao.getBookCountByLibrary(libraryId)
}
