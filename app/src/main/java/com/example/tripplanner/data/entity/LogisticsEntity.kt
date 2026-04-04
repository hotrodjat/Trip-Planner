package com.example.tripplanner.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "logistics",
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
data class LogisticsEntity(
    @PrimaryKey(autoGenerate = true)
    val logisticsId: Long = 0,
    val tripId: Long,
    val type: String,          // flight, hotel, car, train, etc.
    val provider: String?,
    val referenceNumber: String?,
    val notes: String? = null
)
