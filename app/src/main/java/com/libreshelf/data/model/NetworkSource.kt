package com.libreshelf.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "network_sources")
data class NetworkSource(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: NetworkType,
    val host: String,
    val port: Int,
    val path: String = "",
    val username: String = "",
    val password: String = "", // Should be encrypted in production
    val isActive: Boolean = true,
    val autoSync: Boolean = false,
    val syncInterval: Int = 60, // in minutes
    val lastSyncTime: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class NetworkType {
    SMB,
    WEBDAV,
    FTP,
    SFTP,
    NEXTCLOUD
}
