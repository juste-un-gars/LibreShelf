package com.libreshelf.presentation.screens.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libreshelf.data.model.Book
import com.libreshelf.data.model.BookFormat
import com.libreshelf.data.repository.BookRepository
import com.libreshelf.domain.reader.ComicReader
import com.libreshelf.domain.reader.EpubReader
import com.libreshelf.domain.reader.PdfReader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val epubReader: EpubReader,
    private val pdfReader: PdfReader,
    private val comicReader: ComicReader,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookId: Long = savedStateHandle.get<Long>("bookId") ?: 0L

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _totalPages = MutableStateFlow(0)
    val totalPages: StateFlow<Int> = _totalPages.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadBook()
    }

    private fun loadBook() {
        viewModelScope.launch {
            bookRepository.getBookById(bookId).collect { bookData ->
                _book.value = bookData
                bookData?.let {
                    _currentPage.value = it.lastReadPosition
                    openReader(it)
                }
            }
        }
    }

    private suspend fun openReader(book: Book) {
        _isLoading.value = true
        try {
            when (book.fileFormat) {
                BookFormat.EPUB -> {
                    val epubBook = epubReader.loadBook(book.filePath)
                    _totalPages.value = epubReader.getTotalChapters(epubBook)
                }
                BookFormat.PDF -> {
                    val pdfDoc = pdfReader.loadDocument(book.filePath)
                    _totalPages.value = pdfDoc.pageCount
                }
                BookFormat.CBZ, BookFormat.CBR -> {
                    val comic = comicReader.loadComic(book.filePath)
                    _totalPages.value = comic.pageCount
                }
                else -> {
                    // Unsupported format
                }
            }
        } catch (e: Exception) {
            // Handle error
            e.printStackTrace()
        } finally {
            _isLoading.value = false
        }
    }

    fun goToPage(page: Int) {
        val totalPagesValue = _totalPages.value
        if (page in 0 until totalPagesValue) {
            _currentPage.value = page
            updateReadingProgress()
        }
    }

    fun nextPage() {
        val current = _currentPage.value
        val total = _totalPages.value
        if (current < total - 1) {
            goToPage(current + 1)
        }
    }

    fun previousPage() {
        val current = _currentPage.value
        if (current > 0) {
            goToPage(current - 1)
        }
    }

    private fun updateReadingProgress() {
        viewModelScope.launch {
            val book = _book.value ?: return@launch
            val currentPageValue = _currentPage.value
            val totalPagesValue = _totalPages.value

            val progress = if (totalPagesValue > 0) {
                (currentPageValue.toFloat() / totalPagesValue.toFloat()) * 100f
            } else {
                0f
            }

            bookRepository.updateReadingProgress(
                bookId = book.id,
                position = currentPageValue,
                progress = progress
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Clean up readers
        epubReader.closeBook()
        pdfReader.closeDocument()
        comicReader.closeComic()
    }
}
