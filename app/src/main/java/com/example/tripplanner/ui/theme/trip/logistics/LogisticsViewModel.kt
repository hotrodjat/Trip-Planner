package com.example.tripplanner.ui.theme.trip.logistics

import androidx.lifecycle.ViewModel
import com.example.tripplanner.ui.theme.trip.TripViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class LogisticsItem(
    val title: String,
    val description: String = "No items yet"
)

class LogisticsViewModel(
    private val tripViewModel: TripViewModel
) : ViewModel() {

    // Example: each section has a list of items
    private val _flights = MutableStateFlow<List<LogisticsItem>>(emptyList())
    val flights: StateFlow<List<LogisticsItem>> = _flights

    private val _accommodations = MutableStateFlow<List<LogisticsItem>>(emptyList())
    val accommodations: StateFlow<List<LogisticsItem>> = _accommodations

    private val _transportation = MutableStateFlow<List<LogisticsItem>>(emptyList())
    val transportation: StateFlow<List<LogisticsItem>> = _transportation

    // Add item to a section
    fun addFlight(item: LogisticsItem) {
        _flights.value += item
    }

    fun addAccommodation(item: LogisticsItem) {
        _accommodations.value += item
    }

    fun addTransportation(item: LogisticsItem) {
        _transportation.value += item
    }

    // Access tripId if needed
    val tripId: String get() = tripViewModel.tripId
}
