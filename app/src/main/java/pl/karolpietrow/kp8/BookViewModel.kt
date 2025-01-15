package pl.karolpietrow.kp8

import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.karolpietrow.kp8.api.NetworkResponse
import pl.karolpietrow.kp8.api.RetrofitInstance
import pl.karolpietrow.kp8.api.book.BookModel

class BookViewModel: ViewModel() {
    private val bookApi = RetrofitInstance.bookApi
    private val _bookStatus = MutableLiveData<NetworkResponse<BookModel>>()
    val bookStatus: LiveData<NetworkResponse<BookModel>> = _bookStatus

    fun getSearchResults(search: String) {
        viewModelScope.launch {
            val response = bookApi.getBookBySearch(search)
            if (response.isSuccessful) {

            } else {

            }
        }
    }

    fun getBookByID(id: String) {
        viewModelScope.launch {
            val response = bookApi.getBookByID(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    _bookStatus.value = NetworkResponse.Success(it)
                }
            } else {
                _bookStatus.value = NetworkResponse.Error("Błąd ładowania danych")
            }
        }
    }
}