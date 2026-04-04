package com.example.tripplanner.ui.theme.trips

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tripplanner.ui.theme.trips.components.TripCard

@Composable
fun TripsListScreen(
    modifier: Modifier = Modifier,
    onTripSelected: (Long) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(
            listOf(1L, 2L) // placeholder IDs
        ) { tripId ->
            TripCard(
                tripName = "Trip $tripId",
                onClick = { onTripSelected(tripId) }
            )
        }
    }
}
