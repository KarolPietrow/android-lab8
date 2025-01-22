package pl.karolpietrow.kp8

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.karolpietrow.kp8.api.book.BookModel

class BookRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("books")

    suspend fun addBook(book: BookModel) {
        try {
            val document = collection.document()
            val newBook = book.copy(dbID = document.id)
            document.set(newBook).await()
        } catch (e: Exception) {
            println("Failed to add book: ${e.message}")
        }
    }

    suspend fun getAllBooks(): List<BookModel> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(BookModel::class.java)
        }
    }

    suspend fun deleteBook(id: String) {
        collection.document(id).delete().await()
    }
}