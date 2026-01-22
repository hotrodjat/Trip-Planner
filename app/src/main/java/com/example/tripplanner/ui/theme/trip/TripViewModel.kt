package com.example.tripplanner.ui.theme.trip

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

val LocalTripViewModel = staticCompositionLocalOf<TripViewModel> {
    error("TripViewModel not provided")
}
class TripViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val tripId: String
        get() = savedStateHandle["tripId"] ?: error("tripId missing")

    var selectedTab: String
        get() = savedStateHandle["selectedTab"] ?: "overview"
        set(value) { savedStateHandle["selectedTab"] = value }

    private val _budget = MutableStateFlow(0)
    val budget: StateFlow<Int> = _budget

    fun addToBudget(amount: Int) { _budget.value += amount }
    fun resetBudget() { _budget.value = 0 }
}
