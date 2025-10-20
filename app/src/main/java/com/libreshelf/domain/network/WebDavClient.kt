package com.libreshelf.domain.network

import com.libreshelf.data.model.NetworkSource
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileOutputStream
import java.io.StringReader
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * WebDAV client implementation using OkHttp.
 * Supports basic WebDAV operations (list, download, etc.)
 */
class WebDavClient @Inject constructor() {

    private fun getClient(source: NetworkSource): OkHttpClient {
        val credentials = Credentials.basic(source.username, source.password)

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Authorization", credentials)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    private fun getUrl(source: NetworkSource, path: String = ""): String {
        val protocol = if (source.port == 443) "https" else "http"
        val portPart = if ((protocol == "https" && source.port != 443) ||
                           (protocol == "http" && source.port != 80)) {
            ":${source.port}"
        } else ""

        val basePath = source.path.trim('/')
        val filePath = path.trim('/')

        val fullPath = when {
            basePath.isNotEmpty() && filePath.isNotEmpty() -> "$basePath/$filePath"
            basePath.isNotEmpty() -> basePath
            filePath.isNotEmpty() -> filePath
            else -> ""
        }

        return if (fullPath.isNotEmpty()) {
            "$protocol://${source.host}$portPart/$fullPath"
        } else {
            "$protocol://${source.host}$portPart/"
        }
    }

    fun listFiles(source: NetworkSource, path: String): List<NetworkFile> {
        val client = getClient(source)
        val url = getUrl(source, path)

        // WebDAV PROPFIND request to list directory contents
        val propfindXml = """<?xml version="1.0" encoding="utf-8" ?>
            <D:propfind xmlns:D="DAV:">
                <D:prop>
                    <D:displayname/>
                    <D:getcontentlength/>
                    <D:getlastmodified/>
                    <D:resourcetype/>
                </D:prop>
            </D:propfind>""".trimIndent()

        val requestBody = propfindXml.toRequestBody("application/xml".toMediaType())

        val request = Request.Builder()
            .url(url)
            .method("PROPFIND", requestBody)
            .header("Depth", "1")
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return emptyList()
                }

                val responseBody = response.body?.string() ?: return emptyList()
                parseWebDavResponse(responseBody, url)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun parseWebDavResponse(xml: String, baseUrl: String): List<NetworkFile> {
        val files = mutableListOf<NetworkFile>()

        try {
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(StringReader(xml))

            var currentHref = ""
            var currentDisplayName = ""
            var currentSize = 0L
            var currentModified = 0L
            var isDirectory = false
            var inResponse = false

            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "response", "D:response" -> {
                                inResponse = true
                                currentHref = ""
                                currentDisplayName = ""
                                currentSize = 0L
                                currentModified = 0L
                                isDirectory = false
                            }
                            "href", "D:href" -> {
                                if (inResponse) {
                                    currentHref = parser.nextText()
                                }
                            }
                            "displayname", "D:displayname" -> {
                                if (inResponse) {
                                    currentDisplayName = parser.nextText()
                                }
                            }
                            "getcontentlength", "D:getcontentlength" -> {
                                if (inResponse) {
                                    currentSize = parser.nextText().toLongOrNull() ?: 0L
                                }
                            }
                            "getlastmodified", "D:getlastmodified" -> {
                                if (inResponse) {
                                    val dateStr = parser.nextText()
                                    // Parse RFC 1123 date format
                                    currentModified = System.currentTimeMillis() // Simplified for now
                                }
                            }
                            "collection", "D:collection" -> {
                                if (inResponse) {
                                    isDirectory = true
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "response" || parser.name == "D:response") {
                            inResponse = false

                            // Skip the base directory itself
                            if (currentHref.isNotEmpty() &&
                                !currentHref.endsWith(baseUrl.substringAfter(baseUrl.indexOf('/', 8)))) {

                                val fileName = if (currentDisplayName.isNotEmpty()) {
                                    currentDisplayName
                                } else {
                                    currentHref.split("/").lastOrNull { it.isNotEmpty() } ?: ""
                                }

                                if (fileName.isNotEmpty()) {
                                    files.add(
                                        NetworkFile(
                                            name = fileName,
                                            path = currentHref,
                                            isDirectory = isDirectory,
                                            size = currentSize,
                                            lastModified = currentModified
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return files
    }

    fun downloadFile(source: NetworkSource, remotePath: String, localFile: File): Boolean {
        val client = getClient(source)
        val url = getUrl(source, remotePath)

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return false
                }

                response.body?.let { body ->
                    val outputStream = FileOutputStream(localFile)
                    body.byteStream().copyTo(outputStream)
                    outputStream.close()
                    true
                } ?: false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun testConnection(source: NetworkSource): Boolean {
        val client = getClient(source)
        val url = getUrl(source)

        // Send a simple OPTIONS request to test connectivity
        val request = Request.Builder()
            .url(url)
            .method("OPTIONS", null)
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                response.isSuccessful || response.code == 200 || response.code == 204
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
