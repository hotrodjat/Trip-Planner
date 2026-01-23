package com.example.tripplanner.ui.theme.trip

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripplanner.data.repository.TripRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

val LocalTripViewModel = staticCompositionLocalOf<TripViewModel> {
    error("TripViewModel not provided")
}

class TripViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: TripRepository
) : ViewModel() {

    val tripId: String =
        savedStateHandle["tripId"] ?: error("tripId missing")

    var selectedTab: String
        get() = savedStateHandle["selectedTab"] ?: "overview"
        set(value) { savedStateHandle["selectedTab"] = value }

    val budget: StateFlow<Int> =
        repository.getBudget(tripId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                0
            )

    fun addToBudget(amount: Int) {
        viewModelScope.launch {
            repository.addToBudget(tripId, amount)
        }
    }
}
