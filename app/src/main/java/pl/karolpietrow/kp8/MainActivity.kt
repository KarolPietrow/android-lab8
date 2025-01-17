package pl.karolpietrow.kp8

import android.content.Intent
import android.net.Network
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
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
    val context = LocalContext.current
    val bookViewModel = BookViewModel()
    val bookStatus = bookViewModel.bookStatus.observeAsState()
    val searchStatus = bookViewModel.searchStatus.observeAsState()
    var input by remember { mutableStateOf("") }

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

        when (val bookResult = bookStatus.value) {
            is NetworkResponse.Error -> {
                Text(text = bookResult.message)
            }
            is NetworkResponse.Loading -> {
                Column(
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
            }
            is NetworkResponse.Success -> {
                Text(text = bookResult.data.toString())
            }
            null -> {}
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
