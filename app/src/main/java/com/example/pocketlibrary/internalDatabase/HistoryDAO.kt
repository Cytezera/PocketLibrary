package com.example.pocketlibrary.internalDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pocketlibrary.History

@Dao
interface HistoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: History)

    @Query("SELECT * FROM history ORDER BY viewed_at DESC LIMIT :limit")
    suspend fun getLatestHistory(limit: Int): List<History>

    @Query("DELETE FROM history")
    suspend fun clearHistory()
}
