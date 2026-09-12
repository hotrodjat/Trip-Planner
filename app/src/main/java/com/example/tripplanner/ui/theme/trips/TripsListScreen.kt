package com.example.tripplanner.ui.theme.trips

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tripplanner.data.entity.TripEntity
import com.example.tripplanner.ui.theme.trip.EmptyStateCard
import com.example.tripplanner.ui.theme.trips.components.TripCard

@Composable
fun TripsListScreen(
    modifier: Modifier = Modifier,
    onTripSelected: (Long) -> Unit,
    trips: List<TripEntity>
) {
    if (trips.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            EmptyStateCard("No trips yet. Create one to get started!")
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(trips) { trip ->
                TripCard(
                    tripName = trip.title,
                    onClick = { onTripSelected(trip.tripId) }
                )
            }
        }
    }
}
