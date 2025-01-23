package pl.karolpietrow.kp8.api.book

import pl.karolpietrow.kp8.api.search.Author

data class BookModel(
    val authors: List<Author> = emptyList(),
    val bookshelves: List<String> = emptyList(),
    val copyright: Boolean = false,
    val download_count: Int = 0,
    val formats: Formats = Formats(),
    val id: Int = -1,
    val languages: List<String> = emptyList(),
    val media_type: String = "",
    val subjects: List<String>  = emptyList(),
    val title: String = "",
    val translators: List<Any> = emptyList(),
    val dbID: String = ""
)