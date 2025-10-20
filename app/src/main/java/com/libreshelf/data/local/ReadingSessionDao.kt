package com.libreshelf.data.local

import androidx.room.*
import com.libreshelf.data.model.ReadingSession
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingSessionDao {
    @Query("SELECT * FROM reading_sessions WHERE bookId = :bookId ORDER BY startTime DESC")
    fun getSessionsByBook(bookId: Long): Flow<List<ReadingSession>>

    @Query("SELECT * FROM reading_sessions ORDER BY startTime DESC LIMIT :limit")
    fun getRecentSessions(limit: Int = 10): Flow<List<ReadingSession>>

    @Query("SELECT SUM(duration) FROM reading_sessions WHERE bookId = :bookId")
    suspend fun getTotalReadingTime(bookId: Long): Long?

    @Query("SELECT SUM(duration) FROM reading_sessions")
    suspend fun getTotalReadingTimeAllBooks(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ReadingSession): Long

    @Delete
    suspend fun deleteSession(session: ReadingSession)

    @Query("DELETE FROM reading_sessions WHERE bookId = :bookId")
    suspend fun deleteSessionsByBook(bookId: Long)
}
