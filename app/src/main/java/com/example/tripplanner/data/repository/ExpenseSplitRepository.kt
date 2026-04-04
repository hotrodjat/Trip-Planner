package com.example.tripplanner.data.repository

import com.example.tripplanner.data.dao.ExpenseSplitDao
import com.example.tripplanner.data.entity.ExpenseSplitEntity
import com.example.tripplanner.data.entity.ExpenseWithSplits
import kotlinx.coroutines.flow.Flow

class ExpenseSplitRepository(
    private val expenseSplitDao: ExpenseSplitDao
) {

    fun getSplitsForExpense(expenseId: Long): Flow<List<ExpenseSplitEntity>> =
        expenseSplitDao.getSplitsForExpense(expenseId)

    fun getSplitsForPerson(tripId: Long, personId: Long): Flow<List<ExpenseSplitEntity>> =
        expenseSplitDao.getSplitsForPerson(tripId, personId)

    fun getExpensesWithSplitsForTrip(tripId: Long): Flow<List<ExpenseWithSplits>> =
        expenseSplitDao.getExpensesWithSplitsForTrip(tripId)

    suspend fun addSplits(splits: List<ExpenseSplitEntity>) {
        require(splits.isNotEmpty()) { "Splits list cannot be empty" }
        splits.forEach { split ->
            require(split.expenseId > 0) { "Invalid expenseId: ${split.expenseId}" }
            require(split.personId > 0) { "Invalid personId: ${split.personId}" }
            require(split.tripId > 0) { "Invalid tripId: ${split.tripId}" }
            require(split.amount > 0) { "Split amount must be greater than zero: ${split.amount}" }
        }
        expenseSplitDao.insertSplits(splits)
    }

    suspend fun deleteSplitsForExpense(expenseId: Long) {
        require(expenseId > 0) { "Invalid expenseId: $expenseId" }
        expenseSplitDao.deleteSplitsForExpense(expenseId)
    }

    suspend fun addSplit(split: ExpenseSplitEntity) {
        require(split.expenseId > 0) { "Invalid expenseId: ${split.expenseId}" }
        require(split.personId > 0) { "Invalid personId: ${split.personId}" }
        require(split.tripId > 0) { "Invalid tripId: ${split.tripId}" }
        require(split.amount > 0) { "Split amount must be greater than zero: ${split.amount}" }
        expenseSplitDao.insertSplits(listOf(split))
    }
}

