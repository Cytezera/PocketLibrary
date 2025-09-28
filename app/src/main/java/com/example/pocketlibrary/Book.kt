//Book Class

package com.example.pocketlibrary

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize


@Parcelize
data class Book(
    val key : String,
    val title: String,
    val author: List<String>,
    val coverId : Int,
    val publishYear : Int?
):Parcelable


