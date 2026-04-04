package com.example.tripplanner.ui.theme.trip.overview

import androidx.lifecycle.ViewModel
import com.example.tripplanner.ui.theme.trip.TripViewModel
import kotlinx.coroutines.flow.StateFlow

data class OverviewUiState(
    val title: String,
    val tripId: Long,
    val expense: StateFlow<Int>
)

class OverviewViewModel(
    private val tripViewModel: TripViewModel
) : ViewModel() {

    val uiState: OverviewUiState
        get() = OverviewUiState(
            title = "Trip Overview",
            tripId = tripViewModel.tripId,
            expense = tripViewModel.expense
        )
}
