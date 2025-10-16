package com.example.pocketlibrary.internalDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pocketlibrary.Book

@Dao
interface BookDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg books: Book)

    @Delete
    suspend fun delete(vararg books: Book)

    @Query("SELECT * FROM books ORDER BY rowid DESC")
    fun getAllBooks(): LiveData<List<Book>>

    @Query("SELECT COUNT(*) FROM books WHERE book_key = :bookId")
    suspend fun isBookSaved(bookId: String): Int

    @Query("SELECT * FROM books WHERE LOWER(book_title) LIKE '%' || LOWER(:query) || '%' OR LOWER(book_author) LIKE '%' || LOWER(:query) || '%' ORDER BY book_title ASC")
    fun searchBooks(query: String): LiveData<List<Book>>
}
