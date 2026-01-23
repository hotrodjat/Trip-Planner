package com.example.tripplanner.data.dao

import androidx.room.*
import com.example.tripplanner.data.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Query("SELECT total FROM budgets WHERE tripId = :tripId")
    fun getBudget(tripId: String): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBudget(budget: BudgetEntity)
}
