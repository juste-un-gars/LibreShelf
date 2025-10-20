package com.libreshelf.domain.network

import com.hierynomus.msdtyp.AccessMask
import com.hierynomus.mssmb2.SMB2CreateDisposition
import com.hierynomus.mssmb2.SMB2ShareAccess
import com.hierynomus.smbj.SMBClient
import com.hierynomus.smbj.auth.AuthenticationContext
import com.hierynomus.smbj.connection.Connection
import com.hierynomus.smbj.session.Session
import com.hierynomus.smbj.share.DiskShare
import com.libreshelf.data.model.NetworkSource
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class SmbClient @Inject constructor() {

    private fun connect(source: NetworkSource): Triple<Connection, Session, DiskShare>? {
        return try {
            val client = SMBClient()
            val connection = client.connect(source.host)

            val authContext = AuthenticationContext(
                source.username,
                source.password.toCharArray(),
                null
            )

            val session = connection.authenticate(authContext)
            val share = session.connectShare(source.path) as DiskShare

            Triple(connection, session, share)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun listFiles(source: NetworkSource, path: String): List<NetworkFile> {
        val (connection, session, share) = connect(source) ?: return emptyList()

        return try {
            val files = mutableListOf<NetworkFile>()

            share.list(path).forEach { fileInfo ->
                if (fileInfo.fileName != "." && fileInfo.fileName != "..") {
                    files.add(
                        NetworkFile(
                            name = fileInfo.fileName,
                            path = if (path.isEmpty()) fileInfo.fileName else "$path/${fileInfo.fileName}",
                            isDirectory = fileInfo.fileAttributes.contains(
                                com.hierynomus.msfscc.fileinformation.FileStandardInformation.FILE_ATTRIBUTE_DIRECTORY
                            ),
                            size = fileInfo.allocationSize,
                            lastModified = fileInfo.lastWriteTime.toEpochMillis()
                        )
                    )
                }
            }

            files
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        } finally {
            share.close()
            session.close()
            connection.close()
        }
    }

    fun downloadFile(source: NetworkSource, remotePath: String, localFile: File): Boolean {
        val (connection, session, share) = connect(source) ?: return false

        return try {
            val file = share.openFile(
                remotePath,
                setOf(AccessMask.GENERIC_READ),
                null,
                SMB2ShareAccess.ALL,
                SMB2CreateDisposition.FILE_OPEN,
                null
            )

            val inputStream = file.inputStream
            val outputStream = FileOutputStream(localFile)

            inputStream.copyTo(outputStream)

            outputStream.close()
            inputStream.close()
            file.close()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            share.close()
            session.close()
            connection.close()
        }
    }

    fun testConnection(source: NetworkSource): Boolean {
        val result = connect(source)
        result?.let { (connection, session, share) ->
            share.close()
            session.close()
            connection.close()
        }
        return result != null
    }
}
