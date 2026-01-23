package com.example.tripplanner.ui.theme.trip

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tripplanner.data.db.DatabaseProvider
import com.example.tripplanner.data.repository.TripRepository

class TripViewModelFactory(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            val db = DatabaseProvider.get(application)
            return TripViewModel(
                savedStateHandle,
                repository = TripRepository(
                    tripDao = db.tripDao(),
                    budgetDao = db.budgetDao()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}