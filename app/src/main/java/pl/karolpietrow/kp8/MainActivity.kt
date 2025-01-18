package pl.karolpietrow.kp8

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.LocalLifecycleOwner
import pl.karolpietrow.kp8.api.NetworkResponse
import pl.karolpietrow.kp8.api.book.BookModel
import pl.karolpietrow.kp8.ui.theme.KP8Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KP8Theme {
                BookScreen()
            }
        }
    }
}

@Composable
fun BookScreen() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val bookViewModel = BookViewModel()
    val searchStatus = bookViewModel.searchStatus.observeAsState()

    var input by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var bookList by remember { mutableStateOf(listOf<BookModel>()) }

    fun setData(bookResult: NetworkResponse<BookModel>) {
        when (bookResult) {
            is NetworkResponse.Error -> {
                Toast.makeText(context, bookResult.message, Toast.LENGTH_SHORT).show()
                isLoading = false
            }
            is NetworkResponse.Loading -> {
                isLoading = true
            }
            is NetworkResponse.Success -> {
                if (bookResult.data.toString().isNotEmpty()) {
                    bookList += bookResult.data
                    isLoading = false
                }
            }
            null -> {}
        }
    }

    bookViewModel.bookStatus.observe(lifecycleOwner) { response ->
        setData(response)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Moje książki",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Liczba pobranych książek: ",
        )

        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Proszę czekać...",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .weight(1f)
            ) {
                items(bookList) { book ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
//                            .clickable { selectedBook = book }
                    ) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = "ID: " +  book.id,
                            fontSize = 20.sp
                        )
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = "Tytuł: " + book.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (book.authors.isNotEmpty()) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = "Autorzy: ",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            book.authors.forEach { author ->
                                Text(author.name, modifier = Modifier.padding(5.dp))
                            }
                        }
                    }
                }
            }
        }
        Column (
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ID lub tytuł książki do pobrania",
                fontWeight = FontWeight.Bold
            )
            TextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("ID książki lub Tytuł/Autor") },
                modifier = Modifier
            )
            Row {
                Button(
                    onClick = {
                        if (!input.isEmpty()) {
                            if (input.isDigitsOnly()) {
                                bookViewModel.getBookByID(input)
                            } else {

                            }
                        } else {
                            Toast.makeText(context, "Pole nie może być puste!", Toast.LENGTH_SHORT).show()
                        }

//                    } else {
//                        val intent = Intent(context, BookService::class.java).apply {
//                            putExtra("minID", minID.toInt())
//                            putExtra("maxID", maxID.toInt())
//                        }
//                        context.startService(intent)
                    },
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text("Pobierz książki")
                }
                Button(
                    onClick = {
//                    viewModel.clearList()
                    },
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text("Wyczyść listę")
                }
            }
        }
    }
}
