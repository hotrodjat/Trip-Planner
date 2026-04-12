package com.example.tripplanner.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tripplanner.data.dao.*
import com.example.tripplanner.data.entity.*

@Database(
    entities = [
        TripEntity::class,
        ExpenseEntity::class,
        ScheduleEntity::class,
        LogisticsEntity::class,
        PersonEntity::class,
        ExpenseSplitEntity::class
    ],
    version = 8,
    exportSchema = true
)
@TypeConverters(PersonIdListConverter::class)
abstract class TripDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun logisticsDao(): LogisticsDao
    abstract fun expenseSplitDao(): ExpenseSplitDao
    abstract fun personDao(): PersonDao
}
