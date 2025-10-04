//Book Class

package com.example.pocketlibrary

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Parcelize
@Entity (tableName ="books")
data class Book(

    @PrimaryKey
    @ColumnInfo(name = "book_key")
    val key : String,

    @ColumnInfo(name ="book_title")
    val title: String,

    @ColumnInfo(name="book_author")
    val author: List<String>,

    @ColumnInfo(name ="book_coverId")
    val coverId : Int,

    @ColumnInfo(name="book_publish_year")
    val publishYear : Int?
):Parcelable


