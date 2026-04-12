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
        ),
        ForeignKey(
            entity = LogisticsEntity::class,
            parentColumns = ["logisticsId"],
            childColumns = ["logisticsId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("tripId"),
        Index("logisticsId")
    ]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Long = 0L,
    val tripId: Long,
    val name: String,
    val total: Int,
    val paidByPersonId: Long?,
    val logisticsId: Long? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
