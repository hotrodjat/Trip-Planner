package com.example.tripplanner.ui.theme.trip.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tripplanner.ui.theme.trip.LocalTripViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun OverviewScreen() {
    val tripViewModel = LocalTripViewModel.current

    val viewModel: OverviewViewModel = viewModel(
        factory = viewModelFactory {
            initializer { OverviewViewModel(tripViewModel) }
        }
    )

    val uiState = viewModel.uiState

    Column {
        Text(uiState.title)
        Text("Trip ID: ${uiState.tripId}")
        Text("Expense: \$${uiState.expense.collectAsState().value}")

        Button(onClick = { tripViewModel.addToExpense(50) }) {
            Text("Add $50 to expense")
        }
    }
}
