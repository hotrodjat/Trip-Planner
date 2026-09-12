package com.example.tripplanner.ui.theme.trip.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripplanner.ui.theme.trip.TripViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class MoreCategoryData(
    val type: String,
    val displayName: String,
    val itemCount: Int
)

class MoreViewModel(
    private val tripViewModel: TripViewModel
) : ViewModel() {

    val categories: StateFlow<List<MoreCategoryData>> = tripViewModel.logistics.map { logistics ->
        logistics.groupBy { it.type }.map { (type, items) ->
            MoreCategoryData(
                type = type,
                displayName = getDisplayName(type),
                itemCount = items.size
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private fun getDisplayName(type: String): String {
        return when (type) {
            "flight" -> "Flights"
            "hotel" -> "Accommodations"
            "car" -> "Transportation"
            "train" -> "Transportation"
            "bus" -> "Transportation"
            else -> type.capitalize()
        }
    }

    // Access tripId if needed
    val tripId: Long get() = tripViewModel.tripId
}
