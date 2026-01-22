package com.example.tripplanner.ui.theme.trip.more

import androidx.lifecycle.ViewModel
import com.example.tripplanner.ui.theme.trip.TripViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MoreItemData(
    val title: String,
    val description: String = ""
)

class MoreViewModel(
    private val tripViewModel: TripViewModel
) : ViewModel() {

    private val _items = MutableStateFlow<List<MoreItemData>>(
        listOf(
            MoreItemData("Food & Restaurants"),
            MoreItemData("Notes"),
            MoreItemData("Packing List"),
            MoreItemData("Documents")
        )
    )
    val items: StateFlow<List<MoreItemData>> = _items

    // Example: add more items dynamically
    fun addItem(item: MoreItemData) {
        _items.value += item
    }

    // Access tripId if needed
    val tripId: String get() = tripViewModel.tripId
}
