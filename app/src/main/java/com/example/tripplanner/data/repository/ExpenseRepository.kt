package com.example.tripplanner.data.repository

import com.example.tripplanner.data.dao.ExpenseDao
import com.example.tripplanner.data.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao
) {

    fun getExpensesForTrip(tripId: Long): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesForTrip(tripId)

    fun getExpensesForTripByPerson(tripId: Long, personId: Long): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesForTripByPerson(tripId, personId)

    fun getExpensesForTripInTimeRange(tripId: Long, startTime: Long, endTime: Long): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesForTripInTimeRange(tripId, startTime, endTime)

    fun getExpensesForLogistics(logisticsId: Long): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesForLogistics(logisticsId)

    fun getExpensesForTripAndLogistics(tripId: Long, logisticsId: Long): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesForTripAndLogistics(tripId, logisticsId)

    suspend fun getExpenseById(expenseId: Long): ExpenseEntity? =
        expenseDao.getExpenseById(expenseId)

    suspend fun addExpense(expense: ExpenseEntity): Long {
        require(expense.tripId > 0) { "Invalid tripId: ${expense.tripId}" }
        require(expense.total > 0) { "Expense total must be greater than zero" }
        require(expense.name.isNotBlank()) { "Expense name must not be blank" }
        return expenseDao.insertExpense(expense)
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        require(expense.expenseId > 0) { "Invalid expenseId: ${expense.expenseId}" }
        expenseDao.deleteExpense(expense)
    }

    suspend fun deleteExpenseById(expenseId: Long) {
        require(expenseId > 0) { "Invalid expenseId: $expenseId" }
        expenseDao.deleteExpenseById(expenseId)
    }

    suspend fun deleteExpensesForTrip(tripId: Long) {
        require(tripId > 0) { "Invalid tripId: $tripId" }
        expenseDao.deleteExpensesForTrip(tripId)
    }

    suspend fun deleteExpensesForLogistics(logisticsId: Long) {
        require(logisticsId > 0) { "Invalid logisticsId: $logisticsId" }
        expenseDao.deleteExpensesForLogistics(logisticsId)
    }

    suspend fun updateExpense(expense: ExpenseEntity) {
        require(expense.expenseId > 0) { "Invalid expenseId for update: ${expense.expenseId}" }
        require(expense.total > 0) { "Expense total must be greater than zero" }
        require(expense.name.isNotBlank()) { "Expense name must not be blank" }
        expenseDao.insertExpense(expense) // Room replaces on conflict
    }
}
