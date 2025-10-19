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

import com.google.firebase.firestore.SetOptions

object SyncManager {
    private val firestore = FirebaseFirestore.getInstance()
    suspend fun syncWithFirebase(context: Context) {
        if (!isOnline(context)) return
        val db = AppDatabase.getDatabase(context)
        val bookDao = db.bookDao()
        val shelfDao = db.shelfDAO()

        withContext(Dispatchers.IO) {
            try {

                val shelfSnapshot = firestore.collection("shelves").get().await()
                val firebaseShelves = shelfSnapshot.toObjects(Shelf::class.java)
                val localShelves = shelfDao.getAllShelves()

                for (localShelf in localShelves) {
                    val existsInFirebase = firebaseShelves.any { it.shelfName == localShelf.shelfName }
                    if (!existsInFirebase) {
                        try {
                            addShelfToFirebase(localShelf)
                        } catch (e: Exception) {
                            Log.e("SyncManager", "Failed to push shelf ${localShelf.shelfName} to Firebase", e)
                        }
                    }
                }

                for (remoteShelf in firebaseShelves) {
                    val existsLocally = localShelves.any { it.shelfName == remoteShelf.shelfName }
                    if (!existsLocally) {
                        try {
                            shelfDao.insertShelf(remoteShelf)
                        } catch (e: Exception) {
                            Log.e("SyncManager", "Failed to insert shelf ${remoteShelf.shelfName} locally", e)
                        }
                    }
                }

                // This part ho is for yk when you save a book offline but you want it to sync w fb (firebase) not facebook
                val shelf = db.shelfDAO().getShelfByName("local")
                val booksInShelf = shelf?.bookIds?.mapNotNull { bookId ->
                    db.bookDao().getBookByKey(bookId)
                }?: emptyList()

                for (book in booksInShelf) {

                    try {
                        Log.e("SyncManager","Local sync fine eh ")

                        addBookToFirebase(book)
                        addBookIdToShelf("local",book.key)
                    } catch (e: Exception) {
                        Log.e("SyncManager","Ma gai gui got error liao bro , tho i doubt this error would happen ahhahahahahahahhaa")
                    }
                }


                Log.d("SyncManager", "Works here too cuh")

                val snapshot = firestore.collection("books").get().await()
                val books = snapshot.toObjects(Book::class.java)



                for (book in books) {
                    val exists = bookDao.countBookByKey(book.key) > 0
                    if (!exists) {
                        bookDao.insert(book)
                    }
                }


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
                    .set(book, SetOptions.merge())
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

    suspend fun removeBookIdFromShelf(shelfName: String, bookKey: String) {
        withContext(Dispatchers.IO) {
            try {
                if (shelfName.isBlank() || bookKey.isBlank()) return@withContext

                firestore.collection("shelves")
                    .document(shelfName)
                    .update("bookIds", com.google.firebase.firestore.FieldValue.arrayRemove(bookKey))
                    .await()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}