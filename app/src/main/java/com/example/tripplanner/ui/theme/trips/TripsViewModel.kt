package com.example.tripplanner.ui.theme.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripplanner.data.entity.TripEntity
import com.example.tripplanner.data.repository.TripRepository
import com.example.tripplanner.ui.theme.trip.BaseTripViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TripsViewModel(
    private val tripRepository: TripRepository
) : BaseTripViewModel() {

    val trips: StateFlow<List<TripEntity>> = tripRepository.getTrips()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addTrip(title: String, description: String? = null, startDate: Long? = null, endDate: Long? = null) {
        if (title.isBlank()) {
            setError("Trip title cannot be empty")
            return
        }

        executeWithLoading ({
            val trip = TripEntity(
                tripId = 0,
                title = title,
                description = description?.takeIf { it.isNotBlank() },
                startDate = startDate,
                endDate = endDate
            )
            tripRepository.addTrip(trip)
            }
        )
    }

    fun deleteTrip(trip: TripEntity) {
        executeWithLoading ({
            tripRepository.deleteTrip(trip)
            }
        )
    }

    fun updateTrip(trip: TripEntity) {
        if (trip.title.isBlank()) {
            setError("Trip title cannot be empty")
            return
        }

        if (trip.tripId <= 0) {
            setError("Invalid trip ID for update")
            return
        }

        executeWithLoading ({
            tripRepository.updateTrip(trip)
            }
        )
    }
}
