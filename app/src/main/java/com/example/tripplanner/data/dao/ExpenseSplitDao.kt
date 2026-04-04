package com.example.tripplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.tripplanner.data.entity.ExpenseSplitEntity
import com.example.tripplanner.data.entity.ExpenseWithSplits
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseSplitDao {

    @Query("""
        SELECT *
        FROM expense_splits
        WHERE expenseId = :expenseId
    """)
    fun getSplitsForExpense(expenseId: Long): Flow<List<ExpenseSplitEntity>>

    @Query("""
        SELECT *
        FROM expense_splits
        WHERE personId = :personId
        AND tripId = :tripId
    """)
    fun getSplitsForPerson(
        tripId: Long,
        personId: Long
    ): Flow<List<ExpenseSplitEntity>>

    @Transaction
    @Query("""
        SELECT *
        FROM expenses
        WHERE tripId = :tripId
        ORDER BY createdAt DESC
    """)
    fun getExpensesWithSplitsForTrip(
        tripId: Long
    ): Flow<List<ExpenseWithSplits>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSplits(
        splits: List<ExpenseSplitEntity>
    )

    @Query("""
        DELETE FROM expense_splits
        WHERE expenseId = :expenseId
    """)
    suspend fun deleteSplitsForExpense(expenseId: Long)
}

