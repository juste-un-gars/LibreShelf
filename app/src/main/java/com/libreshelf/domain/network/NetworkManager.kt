package com.libreshelf.domain.network

import android.content.Context
import com.libreshelf.data.model.NetworkSource
import com.libreshelf.data.model.NetworkType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class NetworkManager @Inject constructor(
    private val context: Context,
    private val smbClient: SmbClient,
    private val webDavClient: WebDavClient,
    private val ftpClient: FtpClient
) {
    suspend fun listFiles(source: NetworkSource, path: String = ""): List<NetworkFile> {
        return withContext(Dispatchers.IO) {
            when (source.type) {
                NetworkType.SMB -> smbClient.listFiles(source, path)
                NetworkType.WEBDAV -> webDavClient.listFiles(source, path)
                NetworkType.FTP, NetworkType.SFTP -> ftpClient.listFiles(source, path)
                NetworkType.NEXTCLOUD -> webDavClient.listFiles(source, path)
            }
        }
    }

    suspend fun downloadFile(
        source: NetworkSource,
        remotePath: String,
        localFile: File
    ): Boolean {
        return withContext(Dispatchers.IO) {
            when (source.type) {
                NetworkType.SMB -> smbClient.downloadFile(source, remotePath, localFile)
                NetworkType.WEBDAV -> webDavClient.downloadFile(source, remotePath, localFile)
                NetworkType.FTP, NetworkType.SFTP -> ftpClient.downloadFile(source, remotePath, localFile)
                NetworkType.NEXTCLOUD -> webDavClient.downloadFile(source, remotePath, localFile)
            }
        }
    }

    suspend fun testConnection(source: NetworkSource): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                when (source.type) {
                    NetworkType.SMB -> smbClient.testConnection(source)
                    NetworkType.WEBDAV -> webDavClient.testConnection(source)
                    NetworkType.FTP, NetworkType.SFTP -> ftpClient.testConnection(source)
                    NetworkType.NEXTCLOUD -> webDavClient.testConnection(source)
                }
            } catch (e: Exception) {
                false
            }
        }
    }
}

data class NetworkFile(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long,
    val lastModified: Long
)
