package com.libreshelf.data.local

import androidx.room.*
import com.libreshelf.data.model.NetworkSource
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkSourceDao {
    @Query("SELECT * FROM network_sources ORDER BY name ASC")
    fun getAllNetworkSources(): Flow<List<NetworkSource>>

    @Query("SELECT * FROM network_sources WHERE isActive = 1")
    fun getActiveNetworkSources(): Flow<List<NetworkSource>>

    @Query("SELECT * FROM network_sources WHERE id = :id")
    suspend fun getNetworkSourceById(id: Long): NetworkSource?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNetworkSource(source: NetworkSource): Long

    @Update
    suspend fun updateNetworkSource(source: NetworkSource)

    @Delete
    suspend fun deleteNetworkSource(source: NetworkSource)

    @Query("UPDATE network_sources SET lastSyncTime = :timestamp WHERE id = :id")
    suspend fun updateLastSyncTime(id: Long, timestamp: Long)
}
