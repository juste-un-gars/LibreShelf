package com.libreshelf.domain.reader

import android.content.Context
import android.graphics.Bitmap
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.rendering.PDFRenderer
import java.io.File
import javax.inject.Inject

class PdfReader @Inject constructor(
    private val context: Context
) {
    init {
        PDFBoxResourceLoader.init(context)
    }

    private var currentDocument: PDDocument? = null
    private var currentRenderer: PDFRenderer? = null

    fun loadDocument(filePath: String): PdfDocument {
        closeDocument()

        val file = File(filePath)
        currentDocument = PDDocument.load(file)
        currentRenderer = PDFRenderer(currentDocument!!)

        return PdfDocument(
            pageCount = currentDocument!!.numberOfPages,
            filePath = filePath
        )
    }

    fun renderPage(pageIndex: Int, scale: Float = 1.0f): Bitmap? {
        val renderer = currentRenderer ?: return null
        val document = currentDocument ?: return null

        if (pageIndex < 0 || pageIndex >= document.numberOfPages) {
            return null
        }

        return renderer.renderImage(pageIndex, scale)
    }

    fun getPageCount(): Int {
        return currentDocument?.numberOfPages ?: 0
    }

    fun closeDocument() {
        currentDocument?.close()
        currentDocument = null
        currentRenderer = null
    }

    fun extractText(pageIndex: Int): String {
        val document = currentDocument ?: return ""

        if (pageIndex < 0 || pageIndex >= document.numberOfPages) {
            return ""
        }

        return try {
            val page = document.getPage(pageIndex)
            // Note: Text extraction would require additional PDFBox functionality
            ""
        } catch (e: Exception) {
            ""
        }
    }
}

data class PdfDocument(
    val pageCount: Int,
    val filePath: String
)
