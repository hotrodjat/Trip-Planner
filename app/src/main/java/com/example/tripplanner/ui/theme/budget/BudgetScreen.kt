import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BudgetScreen() {
    val expenses = listOf(
        "Dinner – $120 (Alex)",
        "Hotel – $600 (You)"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Budget Summary", style = MaterialTheme.typography.headlineSmall)
        LinearProgressIndicator(progress = 0.7f, modifier = Modifier.fillMaxWidth())
        Text("Remaining: $900")
        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(expenses) { expense ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Text(expense, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}