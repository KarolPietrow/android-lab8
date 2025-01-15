package pl.karolpietrow.kp8

data class Book(
    val id: Int,
    val title: String,
    val content: String,
    val wordCount: Int,
    val charCount: Int,
    val mostCommonWord: String
)