package com.example.pocketlibrary

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "book")
    val bookId: String,

    @ColumnInfo(name = "viewed_at")
    val viewedAt: Long = System.currentTimeMillis()
) : Parcelable