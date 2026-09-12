package com.example.tripplanner.ui.theme.trip.logistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tripplanner.data.entity.LogisticsEntity
import com.example.tripplanner.data.repository.LogisticsRepository
import com.example.tripplanner.ui.theme.trip.LocalTripDependencies
import com.example.tripplanner.ui.theme.trip.LocalTripViewModel

@Composable
fun LogisticsScreen() {
    val tripViewModel = LocalTripViewModel.current
    val dependencies = LocalTripDependencies.current

    // Trip-scoped LogisticsViewModel
    val viewModel: LogisticsViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                LogisticsViewModel(tripViewModel, dependencies.logisticsRepository)
            }
        }
    )

    val flights by viewModel.flights.collectAsState()
    val accommodations by viewModel.accommodations.collectAsState()
    val transportation by viewModel.transportation.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        SectionCard(title = "Flights", items = flights)
        SectionCard(title = "Accommodations", items = accommodations)
        SectionCard(title = "Transportation", items = transportation)
    }
}

@Composable
private fun SectionCard(title: String, items: List<LogisticsEntity>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            if (items.isEmpty()) {
                Text(
                    text = "No items yet",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                items.forEach { item ->
                    Text(
                        text = "${item.provider ?: "Unknown"} - ${item.referenceNumber ?: "No ref"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
