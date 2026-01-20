import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScheduleScreen() {
    val events = listOf(
        "08:40 ✈ Flight to Rome",
        "13:00 🏨 Hotel Check-in",
        "15:00 🍝 Lunch",
        "18:30 🚶 Walking Tour"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("June 10 – Day 1", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(events) { event ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Text(event, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}