import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OverviewScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Italy Trip 🇮🇹", style = MaterialTheme.typography.headlineSmall)
                    Text("June 10 – June 20 • 3 people")
                    Spacer(Modifier.height(8.dp))
                    Text("5 days to go", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        item { Spacer(Modifier.height(12.dp)) }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Budget", style = MaterialTheme.typography.titleMedium)
                    LinearProgressIndicator(progress = 0.7f, modifier = Modifier.fillMaxWidth())
                    Text("$2,100 spent / $3,000")
                }
            }
        }

        item { Spacer(Modifier.height(12.dp)) }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Next Event", style = MaterialTheme.typography.titleMedium)
                    Text("✈ Flight to Rome – 08:40")
                }
            }
        }
    }
}