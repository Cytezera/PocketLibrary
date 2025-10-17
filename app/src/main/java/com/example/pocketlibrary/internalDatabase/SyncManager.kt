package com.example.pocketlibrary.internalDatabase

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.pocketlibrary.Book
import com.example.pocketlibrary.internalDatabase.AppDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import android.util.Log
import com.example.pocketlibrary.Shelf


object SyncManager {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun syncWithFirebase(context: Context) {
        Log.d("SyncManager", "Works here cuh")
        if (!isOnline(context)) return
        val db = AppDatabase.getDatabase(context)
        val bookDao = db.bookDao()
        val shelfDAO = db.shelfDAO()

        withContext(Dispatchers.IO) {
            try {
                Log.d("SyncManager", "Works here too cuh")

                // Pull data from Firestore
                val snapshot = firestore.collection("books").get().await()
                val books = snapshot.toObjects(Book::class.java)


                for (book in books) {
                    Log.d("SyncManager", "Book from Firebase: $book")
                }
                // Update local Room DB
                bookDao.deleteAll()
                bookDao.insertAll(books)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun addBookToFirebase(book: Book) {
        withContext(Dispatchers.IO) {
            try {
                if (book.key.isBlank()) {
                    return@withContext
                }

                val documentId = book.key.substringAfterLast("/")
                firestore.collection("books").document(documentId)
                    .set(book)
                    .await()

            } catch (e: Exception) {1
            }
        }
    }




    suspend fun removeBookFromFirebase(bookKey: String) {
        try {
            firestore.collection("books").document(bookKey)
                .delete()
                .await()
            Log.d("SyncManager", "✅ Removed from Firestore: $bookKey")
        } catch (e: Exception) {
            Log.e("SyncManager", "❌ Remove failed", e)
        }
    }


    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    suspend fun addShelfToFirebase(shelf: Shelf){
        withContext(Dispatchers.IO) {
            try {
                if (shelf.shelfName.isBlank()) {
                    return@withContext
                }

                firestore.collection("shelves").document(shelf.shelfName)
                    .set(shelf)
                    .await()

            } catch (e: Exception) {1
            }
        }
    }

    suspend fun addBookIdToShelf(shelfName: String, bookKey: String) {
        withContext(Dispatchers.IO) {
            try {
                if (shelfName.isBlank() || bookKey.isBlank()) return@withContext

                firestore.collection("shelves")
                    .document(shelfName)
                    .update("bookIds", com.google.firebase.firestore.FieldValue.arrayUnion(bookKey))
                    .await()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}
