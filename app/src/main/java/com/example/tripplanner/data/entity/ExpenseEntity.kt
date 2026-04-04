package com.example.tripplanner.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["tripId"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["personId"],
            childColumns = ["paidByPersonId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("tripId")
    ]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Long = 0L,
    val tripId: Long,
    val total: Int,
    val paidByPersonId: Long?,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
