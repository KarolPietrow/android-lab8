package pl.karolpietrow.kp8

import android.net.Network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.karolpietrow.kp8.api.NetworkResponse
import pl.karolpietrow.kp8.api.RetrofitInstance
import pl.karolpietrow.kp8.api.book.BookModel
import pl.karolpietrow.kp8.api.search.SearchModel

class BookViewModel: ViewModel() {
    private val bookApi = RetrofitInstance.bookApi
    private val _bookStatus = MutableLiveData<NetworkResponse<BookModel>>()
    val bookStatus: LiveData<NetworkResponse<BookModel>> = _bookStatus
    private val _searchStatus = MutableLiveData<NetworkResponse<SearchModel>>()
    val searchStatus: LiveData<NetworkResponse<SearchModel>> = _searchStatus

    fun getSearchResults(search: String) {
        viewModelScope.launch {
            _searchStatus.value = NetworkResponse.Loading
            viewModelScope.launch {
                try {
                    val response = bookApi.getBookBySearch(search)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _searchStatus.value = NetworkResponse.Success(it)
                        }
                    } else {
                        _searchStatus.value = NetworkResponse.Error("Błąd ładowania danych")
                    }
                } catch (e: Exception) {
                    _searchStatus.value = NetworkResponse.Error("Błąd ładowania danych")
                }
            }
        }
    }

    fun getBookByID(id: String) {
        _bookStatus.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = bookApi.getBookByID(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.i("Response: ", response.body().toString())
                        _bookStatus.value = NetworkResponse.Success(it)
                    } ?: run {
                        _bookStatus.value = NetworkResponse.Error("Brak danych")
                    }
                } else {
                    Log.i("Error: ", response.message())
                    _bookStatus.value = NetworkResponse.Error("Błąd ładowania danych")
                }
            } catch (e: Exception) {
                _bookStatus.value = NetworkResponse.Error("Błąd ładowania danych")
            }
        }
    }
}