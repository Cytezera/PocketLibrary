package com.example.pocketlibrary

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val desc: String,
    val categories: List<String>,
    val imageId : Int
)