package com.example.pocketlibrary

import retrofit2.http.GET
import retrofit2.http.Query

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

interface OpenLibraryApi {
    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String
    ): OpenLibrarySearchResponse
}

@JsonClass(generateAdapter = true)
data class OpenLibrarySearchResponse(
    val numFound: Int,
    val docs: List<docs>
)

@JsonClass(generateAdapter = true)
data class docs(
    val title: String?,
    @Json(name = "author_name") val authorNames: List<String>?,
    @Json(name = "cover_i") val coverId: Int?,
    @Json(name = "first_publish_year") val publishYear: Int?,
    @Json(name = "key") val key : String?
){
    val coverUrl: String?
        get() = coverId?.let{"https://covers.openlibrary.org/a/olid/OL23919A-M.jpg"}
}
