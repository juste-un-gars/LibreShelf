package com.libreshelf.presentation.screens.library

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libreshelf.data.model.Book
import com.libreshelf.data.model.Library
import com.libreshelf.data.repository.BookRepository
import com.libreshelf.data.repository.LibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryDetailViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository,
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val libraryId: Long = savedStateHandle.get<Long>("libraryId") ?: 0L

    private val _library = MutableStateFlow<Library?>(null)
    val library: StateFlow<Library?> = _library.asStateFlow()

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    init {
        loadLibrary()
        loadBooks()
    }

    private fun loadLibrary() {
        viewModelScope.launch {
            libraryRepository.getLibraryById(libraryId).collect { lib ->
                _library.value = lib
            }
        }
    }

    private fun loadBooks() {
        viewModelScope.launch {
            bookRepository.getBooksByLibrary(libraryId).collect { bookList ->
                _books.value = bookList
            }
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            bookRepository.deleteBook(book)
        }
    }

    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            bookRepository.updateFavoriteStatus(book.id, !book.isFavorite)
        }
    }
}
