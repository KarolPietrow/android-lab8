package pl.karolpietrow.kp8.api.search

data class SearchModel(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<Result>
)