package com.example.tripplanner.ui.theme.trip

import android.app.Application
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.tripplanner.data.db.DatabaseProvider
import com.example.tripplanner.data.repository.TripRepository
import com.example.tripplanner.data.repository.ExpenseRepository
import com.example.tripplanner.data.repository.ExpenseSplitRepository
import com.example.tripplanner.data.repository.LogisticsRepository
import com.example.tripplanner.data.repository.PersonRepository
import com.example.tripplanner.data.repository.ScheduleRepository

val LocalTripDependencies = staticCompositionLocalOf<TripDependencies> {
    error("TripDependencies not provided")
}

class TripDependencies(private val application: Application) {
    private val db by lazy { DatabaseProvider.get(application) }

    val logisticsRepository by lazy { LogisticsRepository(db.logisticsDao()) }
    val scheduleRepository by lazy { ScheduleRepository(db.scheduleDao()) }
    val personRepository by lazy { PersonRepository(db.personDao()) }
    val expenseRepository by lazy { ExpenseRepository(db.expenseDao()) }
    val expenseSplitRepository by lazy { ExpenseSplitRepository(db.expenseSplitDao()) }
    val tripRepository by lazy { TripRepository(db.tripDao(), logisticsRepository, scheduleRepository, personRepository, expenseRepository, expenseSplitRepository) }

}
