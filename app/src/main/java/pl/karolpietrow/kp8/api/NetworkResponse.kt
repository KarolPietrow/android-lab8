package pl.karolpietrow.kp8.api

sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T): NetworkResponse<T>()
    object Loading: NetworkResponse<Nothing>()
    data class Error(val message: String): NetworkResponse<Nothing>()
}