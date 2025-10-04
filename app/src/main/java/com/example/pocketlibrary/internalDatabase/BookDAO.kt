package com.example.pocketlibrary.internalDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pocketlibrary.Book

@Dao
interface BookDAO {

    @Insert
    suspend fun insert(vararg books: Book)

    @Delete
    suspend fun delete(vararg books: Book)

    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<Book>

}
