package com.example.tripplanner.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tripplanner.data.dao.BudgetDao
import com.example.tripplanner.data.dao.TripDao
import com.example.tripplanner.data.entity.BudgetEntity
import com.example.tripplanner.data.entity.TripEntity

@Database(
    entities = [
        TripEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TripDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun budgetDao(): BudgetDao
}
