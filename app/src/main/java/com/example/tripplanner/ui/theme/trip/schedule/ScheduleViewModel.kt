package com.example.tripplanner.ui.theme.trip.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripplanner.data.entity.ScheduleEntity
import com.example.tripplanner.data.repository.ScheduleRepository
import com.example.tripplanner.ui.theme.trip.TripViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ScheduleEventUi(
    val startTime: Long,
    val endTime: Long,
    val title: String,
    val notes: String? = null
)

class ScheduleViewModel(
    private val tripViewModel: TripViewModel,
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val timeFormatter =
        SimpleDateFormat("HH:mm", Locale.getDefault())
    
    private val dateFormatter =
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    private val tripId: Long
        get() = tripViewModel.tripId

    // Delegate to TripViewModel's events for reactive updates
    val events: StateFlow<List<ScheduleEntity>> = tripViewModel.events

    // Error state management
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Loading state management
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Selected event for detail view
    private val _selectedEvent = MutableStateFlow<ScheduleEntity?>(null)
    val selectedEvent: StateFlow<ScheduleEntity?> = _selectedEvent.asStateFlow()

    private fun setError(message: String?) {
        _errorMessage.value = message
    }

    private fun clearError() {
        _errorMessage.value = null
    }

    // Format timestamp to readable time string
    fun formatTime(timestamp: Long): String {
        return try {
            timeFormatter.format(Date(timestamp))
        } catch (e: Exception) {
            "Invalid time"
        }
    }

    // Format timestamp to readable date string
    fun formatDate(timestamp: Long): String {
        return try {
            dateFormatter.format(Date(timestamp))
        } catch (e: Exception) {
            "Invalid date"
        }
    }

    // Format date and time together
    fun formatDateTime(timestamp: Long): String {
        return "${formatDate(timestamp)} ${formatTime(timestamp)}"
    }

    fun addEvent(event: ScheduleEventUi) {
        // Validate input
        if (event.title.isBlank()) {
            setError("Event title cannot be empty")
            return
        }
        
        if (event.endTime < event.startTime) {
            setError("End time must be after start time")
            return
        }

        // Delegate to TripViewModel which uses the repository
        tripViewModel.addEvent(event)
    }

    fun deleteEvent(scheduleId: Long) {
        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                scheduleRepository.deleteSchedule(scheduleId)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to delete event"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateEvent(schedule: ScheduleEntity) {
        if (schedule.title.isBlank()) {
            setError("Event title cannot be empty")
            return
        }

        if (schedule.endTime < schedule.startTime) {
            setError("End time must be after start time")
            return
        }

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                scheduleRepository.updateSchedule(schedule)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to update event"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectEvent(event: ScheduleEntity) {
        _selectedEvent.value = event
    }

    fun deselectEvent() {
        _selectedEvent.value = null
    }

    // Get events in specific time range
    fun getEventsInTimeRange(startTime: Long, endTime: Long): StateFlow<List<ScheduleEntity>> =
        scheduleRepository.getScheduleForTripInTimeRange(tripId, startTime, endTime)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}
