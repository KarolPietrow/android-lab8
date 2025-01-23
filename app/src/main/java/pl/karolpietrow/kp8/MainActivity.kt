package pl.karolpietrow.kp8

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import pl.karolpietrow.kp8.api.NetworkResponse
import pl.karolpietrow.kp8.api.book.BookModel
import pl.karolpietrow.kp8.ui.theme.KP8Theme
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KP8Theme {
                Scaffold() { innerPadding ->
                    BookScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BookScreen(modifier: Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val bookViewModel = BookViewModel()
    val bookList by bookViewModel.bookList.observeAsState(emptyList())

    var input by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var selectedBook by remember { mutableStateOf<BookModel?>(null) }

    LaunchedEffect(Unit) {
        bookViewModel.refreshList()
    }

    bookViewModel.bookStatus.observe(lifecycleOwner) { bookResult ->
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
                    isLoading = false
                }
            }
            null -> {}
        }
    }

    bookViewModel.searchStatus.observe(lifecycleOwner) { searchResult ->
        when (searchResult) {
            is NetworkResponse.Error -> {
                Toast.makeText(context, searchResult.message, Toast.LENGTH_SHORT).show()
                isLoading = false
            }
            is NetworkResponse.Loading -> {
                isLoading = true
            }
            is NetworkResponse.Success -> {
                if (searchResult.data.toString().isNotEmpty()) {
                    if (searchResult.data.count > 1) {
                        val id: Int = searchResult.data.results[0].id
                        bookViewModel.getBookByID(id.toString())
                    } else if (searchResult.data.count == 1) {
                        val id: Int = searchResult.data.results[0].id
                        bookViewModel.getBookByID(id.toString())
                    }
                } else {
                    Toast.makeText(context, "Błąd ładowania danych", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            }
            null -> {}
        }
    }

    if (selectedBook != null) {
        var string by remember { mutableStateOf("Treść niezaładowana") }
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { selectedBook = null },
            confirmButton = {
                Button(onClick = { selectedBook = null }) {
                    Text("Zamknij")
                }
            },
            title = {
                Text(text = selectedBook!!.title)
            },
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = "ID: " +  selectedBook!!.id,
                        fontSize = 17.sp
                    )
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = "Tytuł: " + selectedBook!!.title,
                        fontSize = 17.sp,
                    )
                    if (selectedBook!!.authors.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = "Autorzy: ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        selectedBook!!.authors.forEach { author ->
                            Text(author.name, modifier = Modifier.padding(5.dp))
                        }
                    }
                    if (selectedBook!!.subjects.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = "Tematyka: ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        selectedBook!!.subjects.forEach { subject ->
                            Text(subject, modifier = Modifier.padding(5.dp))
                        }
                    }
                    if (selectedBook!!.languages.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = "Języki: ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        selectedBook!!.languages.forEach { lang ->
                            Text(lang, modifier = Modifier.padding(5.dp))
                        }
                    }
                    Button(
                        onClick = {
                            string = "Proszę czekać..."
                            bookViewModel.viewModelScope.launch {
                                string = bookViewModel.getBookOkHttp("https://www.gutenberg.org/cache/epub/${selectedBook!!.id}/pg${selectedBook!!.id}.txt")
//                                string = bookViewModel.getBookOkHttp(selectedBook!!.formats.textPlain)
                            }
                        },
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text("Załaduj treść książki")
                    }
                    Text(
                        text = string
                    )
                }
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false // Umożliwia pełną szerokość
            )
        )
    }

    Column(
        modifier
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
                            .clickable { selectedBook = book }
                    ) {
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(0.75f)
                            ) {
                                Text(
                                    modifier = Modifier.padding(5.dp),
                                    text = "ID: " + book.id,
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
                            IconButton(
                                modifier = Modifier.weight(0.25f),
                                onClick = {
                                    bookViewModel.deleteBook(book.dbID)
                                },
                            ) {
                                Icon(imageVector = Icons.Default.Delete, "Delete icon")
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
                        if (input.isNotEmpty()) {
                            if (input.isDigitsOnly()) {
                                bookViewModel.getBookByID(input)
                            } else {
                                bookViewModel.getBookBySearch(input)
                            }
                        } else {
                            Toast.makeText(context, "Pole nie może być puste!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text("Pobierz książki")
                }
                Button(
                    onClick = {
                        bookViewModel.refreshList()
                    },
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text("Odśwież listę")
                }
            }
        }
    }
}