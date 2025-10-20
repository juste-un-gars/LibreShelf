package com.libreshelf.data.local

import androidx.room.*
import com.libreshelf.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books WHERE libraryId = :libraryId ORDER BY title ASC")
    fun getBooksByLibrary(libraryId: Long): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :id")
    fun getBookById(id: Long): Flow<Book?>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookByIdSync(id: Long): Book?

    @Query("SELECT * FROM books WHERE lastOpenedAt IS NOT NULL ORDER BY lastOpenedAt DESC LIMIT 10")
    fun getRecentBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE isFavorite = 1 ORDER BY lastOpenedAt DESC")
    fun getFavoriteBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE readingProgress > 0 AND readingProgress < 100 ORDER BY lastOpenedAt DESC")
    fun getBooksInProgress(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE isFinished = 1 ORDER BY lastOpenedAt DESC")
    fun getFinishedBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%' OR series LIKE '%' || :query || '%'")
    fun searchBooks(query: String): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBookById(id: Long)

    @Query("UPDATE books SET lastReadPosition = :position, readingProgress = :progress, lastOpenedAt = :timestamp WHERE id = :bookId")
    suspend fun updateReadingProgress(bookId: Long, position: Int, progress: Float, timestamp: Long)

    @Query("UPDATE books SET isFavorite = :isFavorite WHERE id = :bookId")
    suspend fun updateFavoriteStatus(bookId: Long, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM books WHERE libraryId = :libraryId")
    suspend fun getBookCountByLibrary(libraryId: Long): Int
}
