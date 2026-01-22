package com.example.tripplanner.ui.theme.trip.schedule

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tripplanner.ui.theme.trip.LocalTripViewModel

@Composable
fun ScheduleScreen() {
    val tripViewModel = LocalTripViewModel.current

    // Trip-scoped ViewModel for schedule
    val viewModel: ScheduleViewModel = viewModel(
        factory = viewModelFactory {
            initializer { ScheduleViewModel(tripViewModel) }
        }
    )

    val events by viewModel.events.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("June 10 – Day 1", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(events) { event ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        "${event.time} ${event.description}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
