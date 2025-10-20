package com.libreshelf.data.local

import androidx.room.*
import com.libreshelf.data.model.Library
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    @Query("SELECT * FROM libraries ORDER BY sortOrder ASC, name ASC")
    fun getAllLibraries(): Flow<List<Library>>

    @Query("SELECT * FROM libraries WHERE id = :id")
    fun getLibraryById(id: Long): Flow<Library?>

    @Query("SELECT * FROM libraries WHERE id = :id")
    suspend fun getLibraryByIdSync(id: Long): Library?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibrary(library: Library): Long

    @Update
    suspend fun updateLibrary(library: Library)

    @Delete
    suspend fun deleteLibrary(library: Library)

    @Query("DELETE FROM libraries WHERE id = :id")
    suspend fun deleteLibraryById(id: Long)

    @Query("SELECT COUNT(*) FROM libraries")
    suspend fun getLibraryCount(): Int
}
