package com.example.pocketlibrary

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "books")
data class Book(

    @PrimaryKey
    @ColumnInfo(name = "book_key")
    var key: String = "",

    @ColumnInfo(name = "book_title")
    var title: String = "",

    @ColumnInfo(name = "book_author")
    var author: List<String> = emptyList(),

    @ColumnInfo(name = "book_coverId")
    var coverId: Int = 0,

    @ColumnInfo(name = "book_publish_year")
    var publishYear: Int? = null,

    @ColumnInfo(name = "is_favourite")
    var isFavourite: Boolean = false

) : Parcelable {
    constructor() : this("", "", emptyList(), 0, null, false)
}
