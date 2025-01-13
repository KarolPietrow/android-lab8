package pl.karolpietrow.kp8

import android.content.Intent
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "ID",
                modifier = Modifier.weight(0.15f),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Tytuł książki",
                modifier = Modifier.weight(0.75f),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Liczba słów",
                modifier = Modifier.weight(0.25f),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Liczba liter",
                modifier = Modifier.weight(0.25f),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Najczęstsze słowo",
                modifier = Modifier.weight(0.25f),
                fontWeight = FontWeight.Bold
            )
        }
        Column (
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ID książek do pobrania",
                fontWeight = FontWeight.Bold
            )
            Row {
                Button(
                    onClick = {
//                    if (minID.toInt()>maxID.toInt()) {
//                        Toast.makeText(context, "Nieprawidłowy zakres!", Toast.LENGTH_SHORT).show()
//                    } else {
//                        val intent = Intent(context, BookService::class.java).apply {
//                            putExtra("minID", minID.toInt())
//                            putExtra("maxID", maxID.toInt())
//                        }
//                        context.startService(intent)
//                    }
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
