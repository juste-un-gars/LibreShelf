package com.libreshelf.domain.network

import com.libreshelf.data.model.NetworkSource
import com.libreshelf.data.model.NetworkType
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class FtpClient @Inject constructor() {

    private fun connect(source: NetworkSource): FTPClient? {
        return try {
            val ftpClient = FTPClient()

            ftpClient.connect(source.host, source.port)

            val reply = ftpClient.replyCode
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect()
                return null
            }

            val loggedIn = ftpClient.login(source.username, source.password)
            if (!loggedIn) {
                ftpClient.disconnect()
                return null
            }

            ftpClient.enterLocalPassiveMode()
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

            if (source.path.isNotEmpty()) {
                ftpClient.changeWorkingDirectory(source.path)
            }

            ftpClient
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun listFiles(source: NetworkSource, path: String): List<NetworkFile> {
        val ftpClient = connect(source) ?: return emptyList()

        return try {
            val workingPath = if (path.isEmpty()) "." else path

            val files = ftpClient.listFiles(workingPath)

            files.filter { it.name != "." && it.name != ".." }.map { file ->
                NetworkFile(
                    name = file.name,
                    path = if (path.isEmpty()) file.name else "$path/${file.name}",
                    isDirectory = file.isDirectory,
                    size = file.size,
                    lastModified = file.timestamp.timeInMillis
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        } finally {
            ftpClient.logout()
            ftpClient.disconnect()
        }
    }

    fun downloadFile(source: NetworkSource, remotePath: String, localFile: File): Boolean {
        val ftpClient = connect(source) ?: return false

        return try {
            val outputStream = FileOutputStream(localFile)
            val success = ftpClient.retrieveFile(remotePath, outputStream)
            outputStream.close()
            success
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            ftpClient.logout()
            ftpClient.disconnect()
        }
    }

    fun testConnection(source: NetworkSource): Boolean {
        val ftpClient = connect(source)
        return if (ftpClient != null) {
            ftpClient.logout()
            ftpClient.disconnect()
            true
        } else {
            false
        }
    }
}
