package com.example.tripplanner.ui.theme.trip.schedule

import androidx.lifecycle.ViewModel
import com.example.tripplanner.ui.theme.trip.TripViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ScheduleEvent(
    val time: String,
    val description: String
)

class ScheduleViewModel(
    private val tripViewModel: TripViewModel
) : ViewModel() {

    private val _events = MutableStateFlow<List<ScheduleEvent>>(
        listOf(
            ScheduleEvent("08:40", "✈ Flight to Rome"),
            ScheduleEvent("13:00", "🏨 Hotel Check-in"),
            ScheduleEvent("15:00", "🍝 Lunch"),
            ScheduleEvent("18:30", "🚶 Walking Tour")
        )
    )
    val events: StateFlow<List<ScheduleEvent>> = _events

    // Example: add event dynamically
    fun addEvent(event: ScheduleEvent) {
        _events.value += event
    }

    val tripId: String get() = tripViewModel.tripId
}
