package com.example.tripplanner.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey
    val tripId: String,
    val title: String,
    val createdAt: Long = System.currentTimeMillis()
)
