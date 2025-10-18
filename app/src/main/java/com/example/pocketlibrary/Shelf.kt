package com.example.pocketlibrary

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "shelves")
data class Shelf(

    @PrimaryKey
    @ColumnInfo(name = "shelf_name")
    var shelfName: String = "",

    @ColumnInfo(name = "book_ids")
    val bookIds: List<String>
) : Parcelable {
    constructor() : this("", emptyList())
}
