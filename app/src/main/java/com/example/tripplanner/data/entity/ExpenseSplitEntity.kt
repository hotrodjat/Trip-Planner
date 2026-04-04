package com.example.tripplanner.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "expense_splits",
    foreignKeys = [
        ForeignKey(
            entity = ExpenseEntity::class,
            parentColumns = ["expenseId"],
            childColumns = ["expenseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["personId"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["tripId"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("personId"),
        Index("tripId"),
        Index(
            value = ["expenseId", "personId"],
            unique = true
        )
    ]
)
data class ExpenseSplitEntity(
    @PrimaryKey(autoGenerate = true)
    val expenseSplitId: Long = 0,
    val expenseId: Long,
    val personId: Long,
    val tripId: Long,
    val amount: Int
)


data class ExpenseWithSplits(
    @Embedded
    val expense: ExpenseEntity,

    @Relation(
        parentColumn = "expenseId",
        entityColumn = "expenseId"
    )
    val splits: List<ExpenseSplitEntity>? = null
)