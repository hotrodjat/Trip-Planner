package com.example.tripplanner.ui.theme.trip.logistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripplanner.data.entity.LogisticsEntity
import com.example.tripplanner.data.repository.LogisticsRepository
import com.example.tripplanner.ui.theme.trip.TripViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LogisticsViewModel(
    private val tripViewModel: TripViewModel,
    private val logisticsRepository: LogisticsRepository
) : ViewModel() {

    private val tripId: Long
        get() = tripViewModel.tripId

    // Error state for UI feedback
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Loading state for UI feedback
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private fun setError(message: String?) {
        _errorMessage.value = message
    }

    private fun clearError() {
        _errorMessage.value = null
    }

    // ...existing code...

    // StateFlow for flights filtered from repository
    val flights: StateFlow<List<LogisticsEntity>> = logisticsRepository
        .getLogisticsForTripByType(tripId, "flight")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // StateFlow for accommodations filtered from repository
    val accommodations: StateFlow<List<LogisticsEntity>> = logisticsRepository
        .getLogisticsForTripByType(tripId, "hotel")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // StateFlow for transportation filtered from repository
    val transportation: StateFlow<List<LogisticsEntity>> = logisticsRepository
        .getLogisticsForTrip(tripId)
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

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                
                val logistics = LogisticsEntity(
                    tripId = tripId,
                    type = type,
                    provider = provider ?: title, // Use title as provider if not specified
                    referenceNumber = referenceNumber?.takeIf { it.isNotBlank() },
                    notes = notes?.takeIf { it.isNotBlank() }
                )
                logisticsRepository.addLogistics(logistics)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to add logistics item"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Delete a logistics item with error handling
    fun deleteLogistics(logistics: LogisticsEntity) {
        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                logisticsRepository.deleteLogistics(logistics)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to delete logistics item"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Update a logistics item
    fun updateLogistics(logistics: LogisticsEntity) {
        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                logisticsRepository.updateLogistics(logistics)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to update logistics item"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
