package com.example.tripplanner.ui.theme.trip.logistics

import androidx.lifecycle.viewModelScope
import com.example.tripplanner.data.entity.LogisticsEntity
import com.example.tripplanner.data.repository.LogisticsRepository
import com.example.tripplanner.ui.theme.trip.BaseTripViewModel
import com.example.tripplanner.ui.theme.trip.TripViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LogisticsViewModel(
    private val tripViewModel: TripViewModel,
    private val logisticsRepository: LogisticsRepository
) : BaseTripViewModel() {

    private val tripId: Long
        get() = tripViewModel.tripId

    // StateFlow for flights filtered from TripViewModel
    val flights: StateFlow<List<LogisticsEntity>> = tripViewModel.logistics
        .map { entities -> entities.filter { it.type == "flight" } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // StateFlow for accommodations filtered from TripViewModel
    val accommodations: StateFlow<List<LogisticsEntity>> = tripViewModel.logistics
        .map { entities -> entities.filter { it.type == "hotel" } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // StateFlow for transportation filtered from TripViewModel
    val transportation: StateFlow<List<LogisticsEntity>> = tripViewModel.logistics
        .map { entities -> entities.filter { it.type == "car" || it.type == "train" || it.type == "bus" } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Add flight to database
    fun addFlight(title: String, provider: String? = null, referenceNumber: String? = null, notes: String? = null) {
        addLogistics(type = "flight", title = title, provider = provider, referenceNumber = referenceNumber, notes = notes)
    }

    // Add accommodation to database
    fun addAccommodation(title: String, provider: String? = null, referenceNumber: String? = null, notes: String? = null) {
        addLogistics(type = "hotel", title = title, provider = provider, referenceNumber = referenceNumber, notes = notes)
    }

    // Add transportation to database
    fun addTransportation(title: String, provider: String? = null, referenceNumber: String? = null, notes: String? = null) {
        addLogistics(type = "car", title = title, provider = provider, referenceNumber = referenceNumber, notes = notes)
    }

    // Generic method to add logistics item with error handling
    private fun addLogistics(type: String, title: String, provider: String? = null, referenceNumber: String? = null, notes: String? = null) {
        if (title.isBlank()) {
            setError("Title cannot be empty")
            return
        }

        executeWithLoading ({
            val logistics = LogisticsEntity(
                tripId = tripId,
                title = title,
                type = type,
                provider = provider ?: title, // Use title as provider if not specified
                referenceNumber = referenceNumber?.takeIf { it.isNotBlank() },
                notes = notes?.takeIf { it.isNotBlank() }
            )
            tripViewModel.addLogistics(logistics)
            }
        )
    }

    // Delete a logistics item with error handling
    fun deleteLogistics(logistics: LogisticsEntity) {
        executeWithLoading ({
            tripViewModel.deleteLogistics(logistics)
            }
        )
    }

    // Update a logistics item
    fun updateLogistics(logistics: LogisticsEntity) {
        executeWithLoading ({
            tripViewModel.updateLogistics(logistics)
            }
        )
    }
}
