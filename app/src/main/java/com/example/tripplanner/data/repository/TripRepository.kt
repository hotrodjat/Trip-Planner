package com.example.tripplanner.data.repository

import com.example.tripplanner.data.dao.BudgetDao
import com.example.tripplanner.data.dao.TripDao
import com.example.tripplanner.data.entity.BudgetEntity
import com.example.tripplanner.data.entity.TripEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TripRepository(
    private val tripDao: TripDao,
    private val budgetDao: BudgetDao
) {

    fun getTrips() = tripDao.getTrips()

    fun getTrip(tripId: String) = tripDao.getTrip(tripId)

    fun getBudget(tripId: String): Flow<Int> =
        budgetDao.getBudget(tripId).map { it ?: 0 }

    suspend fun addToBudget(tripId: String, amount: Int) {
        if (tripDao.getTrip(tripId).first() == null) {
            tripDao.insertTrip(TripEntity(tripId = tripId, title = "Trip $tripId"))
        }

        val current = budgetDao.getBudget(tripId).first() ?: 0
        budgetDao.upsertBudget(
            BudgetEntity(tripId, current + amount)
        )
    }
}
