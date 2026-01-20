import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BookingsScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Bookings", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
        }

        item {
            SectionCard("✈ Flights", listOf("JFK → FCO", "FCO → JFK"))
            Spacer(Modifier.height(8.dp))
            SectionCard("🏨 Accommodations", listOf("Hotel Roma Central"))
            Spacer(Modifier.height(8.dp))
            SectionCard("🎟 Activities", listOf("Colosseum Tour"))
        }
    }
}

@Composable
fun SectionCard(title: String, items: List<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            items.forEach {
                Text("• $it")
            }
        }
    }
}