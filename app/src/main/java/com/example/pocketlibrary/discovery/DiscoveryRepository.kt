package com.example.pocketlibrary.discovery

import com.example.pocketlibrary.Book
import com.example.pocketlibrary.Network
import com.example.pocketlibrary.OpenLibraryApi

class DiscoverRepository(
    private val api: OpenLibraryApi = Network.api
) {
    private val topics = listOf("novel", "history", "science", "classic", "fantasy", "biography")

    suspend fun load(page: Int, limit: Int = 20): List<Book> {
        val q = topics[page % topics.size]
        val r = api.searchBooks(
            query = q,

        )
        return r.docs.mapNotNull { d ->
            val key = d.key ?: return@mapNotNull null
            val title = d.title ?: return@mapNotNull null
            Book(
                key = key,
                title = title,
                author = d.authorNames ?: emptyList(),
                coverId = d.coverId ?: 0,
                publishYear = d.publishYear,
                isFavourite = d.isFavourite
            )
        }.distinctBy { it.key }
    }
}
