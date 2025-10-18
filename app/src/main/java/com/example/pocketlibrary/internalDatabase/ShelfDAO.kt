package com.example.pocketlibrary.internalDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pocketlibrary.Book
import com.example.pocketlibrary.Shelf

@Dao
interface ShelfDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShelf(shelf: Shelf)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(shelves: List<Shelf>)

    @Query("DELETE FROM shelves")
    suspend fun deleteAll()
    @Update
    suspend fun updateShelf(shelf: Shelf)

    @Query("SELECT * FROM shelves WHERE shelf_name = :shelfName LIMIT 1")
    suspend fun getShelf(shelfName: String): Shelf?

    @Query("SELECT * FROM shelves")
    suspend fun getAllShelves(): List<Shelf>



    @Query("SELECT * FROM shelves ORDER BY shelf_name ASC")
    fun getAllShelvesCategory(): LiveData<List<Shelf>>


    @Query("DELETE FROM shelves WHERE shelf_name = :shelfName")
    suspend fun deleteShelf(shelfName: String)

    // Helper to add a book ID to a shelf
    suspend fun addBookIdToShelf(shelfName: String, bookId: String) {
        val shelf = getShelf(shelfName)
        if (shelf == null) {
            insertShelf(Shelf(shelfName, listOf(bookId)))
        } else {
            val updatedBookIds = shelf.bookIds + bookId
            updateShelf(shelf.copy(bookIds = updatedBookIds))
        }
    }
}
