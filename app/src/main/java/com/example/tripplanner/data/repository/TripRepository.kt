package com.example.tripplanner.data.repository

import androidx.room.Transaction
import com.example.tripplanner.data.dao.*
import com.example.tripplanner.data.entity.*
import com.example.tripplanner.ui.theme.trip.schedule.ScheduleEventUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TripRepository(
    private val tripDao: TripDao,
 //    private val expenseDao: ExpenseDao,
    private val logisticsRepository: LogisticsRepository,
    private val scheduleRepository: ScheduleRepository,
    private val personRepository: PersonRepository,
    private val expenseRepository: ExpenseRepository,
    private val expenseSplitRepository: ExpenseSplitRepository
) {

    fun getTrips() = tripDao.getTrips()

    fun getTrip(tripId: Long) = tripDao.getTrip(tripId)

    fun getExpense(tripId: Long): Flow<Int> =
        expenseRepository.getExpensesForTrip(tripId).map { expenses -> expenses.sumOf { it.total } }
    
    fun getScheduleForTrip(tripId: Long) =
        scheduleRepository.getScheduleForTrip(tripId)

    suspend fun addEvent(tripId: Long, event: ScheduleEventUi) {
        val trip = tripDao.getTrip(tripId).first()
        if (trip == null) {
            tripDao.insertTrip(TripEntity(tripId = tripId, title = "Trip $tripId"))
        }

        scheduleRepository.addSchedule(
            ScheduleEntity(
                scheduleId = 0,
                tripId = tripId,
                logisticsId = 0L,
                title = event.title,
                startTime = event.startTime,
                endTime = event.endTime,
                notes = event.notes
            )
        )
    }

    @Transaction
    suspend fun insertExpenseWithSplits(
        expense: ExpenseEntity,
        splits: List<ExpenseSplitEntity>
    ) {
        val expenseId = expenseRepository.addExpense(expense)
        if (splits.isNotEmpty()) {
            val splitsWithId = splits.map { it.copy(expenseId = expenseId) }
            expenseSplitRepository.addSplits(splitsWithId)
        }
    }

//    TODO: remove this function
    suspend fun addToExpense(tripId: Long, amount: Int, name: String) {
        if (amount <= 0) {
            throw IllegalArgumentException("Amount must be greater than zero")
        }

        val trip = tripDao.getTrip(tripId).first()
        if (trip == null) {
            tripDao.insertTrip(TripEntity(tripId = tripId, title = "Trip $tripId"))
        }

        expenseRepository.addExpense(
            ExpenseEntity(
                tripId = tripId,
                total = amount,
                name = name,
                paidByPersonId = null,
                notes = null
            )
        )
    }

    // Logistics delegation methods
    suspend fun addLogistics(logistics: LogisticsEntity) {
        logisticsRepository.addLogistics(logistics)
    }

    suspend fun deleteLogistics(logistics: LogisticsEntity) {
        logisticsRepository.deleteLogistics(logistics)
    }

    fun getLogisticsForTrip(tripId: Long) = logisticsRepository.getLogisticsForTrip(tripId)

    fun getLogisticsForTripByType(tripId: Long, type: String): Flow<List<LogisticsEntity>> =
        logisticsRepository.getLogisticsForTripByType(tripId, type)

    fun getLogisticsForTripByLocation(tripId: Long, location: String): Flow<List<LogisticsEntity>> =
        logisticsRepository.getLogisticsForTripByLocation(tripId, location)

    fun getLogisticsForTripByPeople(tripId: Long, personId: Long): Flow<List<LogisticsEntity>> =
        logisticsRepository.getLogisticsForTripByPerson(tripId, personId)

    // Schedule delegation methods
    suspend fun deleteSchedule(scheduleId: Long) {
        scheduleRepository.deleteSchedule(scheduleId)
    }

    suspend fun updateSchedule(schedule: ScheduleEntity) {
        scheduleRepository.updateSchedule(schedule)
    }

    fun getScheduleForTripInTimeRange(tripId: Long, startTime: Long, endTime: Long) =
        scheduleRepository.getScheduleForTripInTimeRange(tripId, startTime, endTime)

    suspend fun getScheduleById(scheduleId: Long) = scheduleRepository.getScheduleById(scheduleId)

    // Person delegation methods
    fun getPeopleForTrip(tripId: Long) = personRepository.getPeopleForTrip(tripId)

    fun searchPeople(searchQuery: String) = personRepository.searchPeople(searchQuery)

    suspend fun getPersonById(personId: Long) = personRepository.getPersonById(personId)

    suspend fun addPerson(person: PersonEntity) {
        personRepository.addPerson(person)
    }

    suspend fun deletePerson(person: PersonEntity) {
        personRepository.deletePerson(person)
    }

    suspend fun deletePersonById(personId: Long) {
        personRepository.deletePersonById(personId)
    }

    suspend fun deletePeopleForTrip(tripId: Long) {
        personRepository.deletePeopleForTrip(tripId)
    }

    suspend fun updatePerson(person: PersonEntity) {
        personRepository.updatePerson(person)
    }

    // Expense delegation methods
    fun getExpensesForTrip(tripId: Long) = expenseRepository.getExpensesForTrip(tripId)

    fun getExpensesForTripByPerson(tripId: Long, personId: Long) = 
        expenseRepository.getExpensesForTripByPerson(tripId, personId)

    fun getExpensesForTripInTimeRange(tripId: Long, startTime: Long, endTime: Long) = 
        expenseRepository.getExpensesForTripInTimeRange(tripId, startTime, endTime)

    fun getExpensesForLogistics(logisticsId: Long) =
        expenseRepository.getExpensesForLogistics(logisticsId)

    fun getExpensesForTripAndLogistics(tripId: Long, logisticsId: Long) =
        expenseRepository.getExpensesForTripAndLogistics(tripId, logisticsId)

    suspend fun getExpenseById(expenseId: Long) = expenseRepository.getExpenseById(expenseId)

    suspend fun addExpense(expense: ExpenseEntity) {
        expenseRepository.addExpense(expense)
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        expenseRepository.deleteExpense(expense)
    }

    suspend fun deleteExpenseById(expenseId: Long) {
        expenseRepository.deleteExpenseById(expenseId)
    }

    suspend fun deleteExpensesForTrip(tripId: Long) {
        expenseRepository.deleteExpensesForTrip(tripId)
    }

    suspend fun deleteExpensesForLogistics(logisticsId: Long) {
        expenseRepository.deleteExpensesForLogistics(logisticsId)
    }

    suspend fun updateExpense(expense: ExpenseEntity) {
        expenseRepository.updateExpense(expense)
    }

    // Expense Split delegation methods
    fun getSplitsForExpense(expenseId: Long) = expenseSplitRepository.getSplitsForExpense(expenseId)

    fun getSplitsForPerson(tripId: Long, personId: Long) = expenseSplitRepository.getSplitsForPerson(tripId, personId)

    fun getExpensesWithSplitsForTrip(tripId: Long) = expenseSplitRepository.getExpensesWithSplitsForTrip(tripId)

    suspend fun addSplits(splits: List<ExpenseSplitEntity>) {
        expenseSplitRepository.addSplits(splits)
    }

    suspend fun addSplit(split: ExpenseSplitEntity) {
        expenseSplitRepository.addSplit(split)
    }

    suspend fun deleteSplitsForExpense(expenseId: Long) {
        expenseSplitRepository.deleteSplitsForExpense(expenseId)
    }
}
