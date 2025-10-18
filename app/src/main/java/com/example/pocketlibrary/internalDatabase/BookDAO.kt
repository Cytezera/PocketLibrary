package com.example.pocketlibrary.internalDatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pocketlibrary.Book

@Dao
interface BookDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg books: Book)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(books: List<Book>)

    @Delete
    suspend fun delete(vararg books: Book)

    @Query("DELETE FROM books")
    suspend fun deleteAll()

    @Query("SELECT * FROM books ORDER BY rowid DESC")
    fun getAllBooks(): LiveData<List<Book>>

    @Query("SELECT COUNT(*) FROM books WHERE book_key = :bookId")
    suspend fun isBookSaved(bookId: String): Int

    @Query("SELECT * FROM books WHERE LOWER(book_title) LIKE '%' || LOWER(:query) || '%' OR LOWER(book_author) LIKE '%' || LOWER(:query) || '%' ORDER BY book_title ASC")
    fun searchBooks(query: String): LiveData<List<Book>>

    @Query("SELECT * FROM books ORDER BY rowid DESC LIMIT :count")
    fun getLatestBooks(count: Int): LiveData<List<Book>>

    @Query("SELECT COUNT(*) FROM books WHERE book_key = :bookKey")
    suspend fun countBookByKey(bookKey: String): Int

    @Query("""
    SELECT * FROM books 
    WHERE book_key IN (:bookIds)
    """)
    suspend fun getBooksByIds(bookIds: List<String>): List<Book>

}
