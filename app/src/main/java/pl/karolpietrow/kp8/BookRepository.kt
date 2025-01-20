package pl.karolpietrow.kp8

import com.google.firebase.firestore.FirebaseFirestore

class BookRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("books")

//    suspend fun addBook(book: BookModel) {
//
//    }
}