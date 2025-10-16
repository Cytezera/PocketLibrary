package com.example.pocketlibrary.library

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pocketlibrary.Book
import com.example.pocketlibrary.internalDatabase.AppDatabase
import kotlinx.coroutines.launch

class SavedBookViewModel (application: Application) : AndroidViewModel(application) {
    private val bookDAO = AppDatabase.getDatabase(application).bookDao()
    val allBooks: LiveData<List<Book>> = bookDAO.getAllBooks()

    fun insert(book: Book) = viewModelScope.launch {
        bookDAO.insert(book)
    }

    fun delete(book: Book) = viewModelScope.launch {
        bookDAO.delete(book)
    }

    fun search(query: String): LiveData<List<Book>> {
        return bookDAO.searchBooks(query)
    }
}