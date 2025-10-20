package com.libreshelf.domain.network

import com.thegrizzlylabs.sardineandroid.Sardine
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import com.libreshelf.data.model.NetworkSource
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class WebDavClient @Inject constructor() {

    private fun getSardine(source: NetworkSource): Sardine {
        return OkHttpSardine().apply {
            setCredentials(source.username, source.password)
        }
    }

    private fun getUrl(source: NetworkSource, path: String = ""): String {
        val protocol = if (source.port == 443) "https" else "http"
        val portPart = if ((protocol == "https" && source.port != 443) ||
                           (protocol == "http" && source.port != 80)) {
            ":${source.port}"
        } else ""

        val basePath = source.path.trim('/')
        val filePath = path.trim('/')

        val fullPath = if (basePath.isNotEmpty() && filePath.isNotEmpty()) {
            "$basePath/$filePath"
        } else {
            basePath + filePath
        }

        return "$protocol://${source.host}$portPart/$fullPath"
    }

    fun listFiles(source: NetworkSource, path: String): List<NetworkFile> {
        return try {
            val sardine = getSardine(source)
            val url = getUrl(source, path)

            val resources = sardine.list(url)

            resources.drop(1).map { resource ->
                NetworkFile(
                    name = resource.name,
                    path = resource.path,
                    isDirectory = resource.isDirectory,
                    size = resource.contentLength ?: 0L,
                    lastModified = resource.modified?.time ?: 0L
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun downloadFile(source: NetworkSource, remotePath: String, localFile: File): Boolean {
        return try {
            val sardine = getSardine(source)
            val url = getUrl(source, remotePath)

            val inputStream = sardine.get(url)
            val outputStream = FileOutputStream(localFile)

            inputStream.copyTo(outputStream)

            outputStream.close()
            inputStream.close()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun testConnection(source: NetworkSource): Boolean {
        return try {
            val sardine = getSardine(source)
            val url = getUrl(source)
            sardine.exists(url)
        } catch (e: Exception) {
            false
        }
    }
}
