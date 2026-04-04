package com.example.tripplanner.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true)
    val tripId: Long = 0,
    val title: String,
    val description: String? = null,
    val location: String? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val status: String = "planning", // planned, active, completed, cancelled
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
