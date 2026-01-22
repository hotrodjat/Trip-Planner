import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tripplanner.ui.theme.trip.LocalTripViewModel
import com.example.tripplanner.ui.theme.trip.budget.BudgetViewModel

@Composable
fun BudgetScreen() {
    val tripViewModel = LocalTripViewModel.current

    val viewModel: BudgetViewModel = viewModel(
        factory = viewModelFactory {
            initializer { BudgetViewModel(tripViewModel) }
        }
    )

    val budget = viewModel.budget.collectAsState()

    Column {
        Text("Budget Screen")
        Text("Current Budget: \$${budget.value}")

        Button(onClick = { viewModel.addAmount(100) }) {
            Text("Add $100")
        }

        Button(onClick = { viewModel.resetBudget() }) {
            Text("Reset Budget")
        }
    }
}
