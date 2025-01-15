package pl.karolpietrow.kp8.api.book

data class BookModel(
    val authors: List<Any>,
    val bookshelves: List<String>,
    val copyright: Boolean,
    val download_count: Int,
//    val formats: Formats,
    val id: Int,
    val languages: List<String>,
    val media_type: String,
    val subjects: List<String>,
    val title: String,
    val translators: List<Any>
)