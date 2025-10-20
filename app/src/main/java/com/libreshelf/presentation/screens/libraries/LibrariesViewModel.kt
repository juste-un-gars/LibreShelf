package com.libreshelf.presentation.screens.libraries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libreshelf.data.model.Library
import com.libreshelf.data.repository.LibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibrariesViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    private val _libraries = MutableStateFlow<List<Library>>(emptyList())
    val libraries: StateFlow<List<Library>> = _libraries.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    init {
        loadLibraries()
    }

    private fun loadLibraries() {
        viewModelScope.launch {
            libraryRepository.getAllLibraries().collect { libraryList ->
                _libraries.value = libraryList
            }
        }
    }

    fun createLibrary(name: String, description: String, color: String) {
        viewModelScope.launch {
            val library = Library(
                name = name,
                description = description,
                color = color
            )
            libraryRepository.insertLibrary(library)
            _showDialog.value = false
        }
    }

    fun deleteLibrary(library: Library) {
        viewModelScope.launch {
            libraryRepository.deleteLibrary(library)
        }
    }

    fun showCreateDialog() {
        _showDialog.value = true
    }

    fun hideDialog() {
        _showDialog.value = false
    }
}
