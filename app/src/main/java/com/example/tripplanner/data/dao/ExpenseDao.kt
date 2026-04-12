package com.example.tripplanner.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tripplanner.data.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses WHERE tripId = :tripId ORDER BY createdAt DESC")
    fun getExpensesForTrip(tripId: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE expenseId = :expenseId")
    suspend fun getExpenseById(expenseId: Long): ExpenseEntity?

    @Query("SELECT * FROM expenses WHERE tripId = :tripId AND paidByPersonId = :personId ORDER BY createdAt DESC")
    fun getExpensesForTripByPerson(tripId: Long, personId: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE tripId = :tripId AND createdAt BETWEEN :startTime AND :endTime ORDER BY createdAt DESC")
    fun getExpensesForTripInTimeRange(tripId: Long, startTime: Long, endTime: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE logisticsId = :logisticsId ORDER BY createdAt DESC")
    fun getExpensesForLogistics(logisticsId: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE tripId = :tripId AND logisticsId = :logisticsId ORDER BY createdAt DESC")
    fun getExpensesForTripAndLogistics(tripId: Long, logisticsId: Long): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE expenseId = :expenseId")
    suspend fun deleteExpenseById(expenseId: Long)

    @Query("DELETE FROM expenses WHERE tripId = :tripId")
    suspend fun deleteExpensesForTrip(tripId: Long)

    @Query("DELETE FROM expenses WHERE logisticsId = :logisticsId")
    suspend fun deleteExpensesForLogistics(logisticsId: Long)
}
