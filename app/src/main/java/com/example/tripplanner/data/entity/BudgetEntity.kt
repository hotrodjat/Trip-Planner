package com.example.tripplanner.data.entity

import androidx.room.*

@Entity(
    tableName = "budgets",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["tripId"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tripId")]
)
data class BudgetEntity(
    @PrimaryKey
    val tripId: String,
    val total: Int
)
