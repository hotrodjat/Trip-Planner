package com.example.tripplanner.ui.theme.trip.budget

import androidx.lifecycle.ViewModel
import com.example.tripplanner.ui.theme.trip.TripViewModel

class BudgetViewModel(
    private val tripViewModel: TripViewModel
) : ViewModel() {

    val budget = tripViewModel.budget // just forward the shared state

    fun addAmount(amount: Int) {
        tripViewModel.addToBudget(amount)
    }

    fun resetBudget() {
        tripViewModel.resetBudget()
    }
}
