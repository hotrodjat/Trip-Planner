package com.example.tripplanner.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedules",
    foreignKeys = [
        ForeignKey(
            entity = LogisticsEntity::class,
            parentColumns = ["logisticsId"],
            childColumns = ["logisticsId"],
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
        Index(
            value = ["tripId", "startTime"]
        )
    ]
)
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val scheduleId: Long = 0,
    val tripId: Long,
    val logisticsId: Long = 0L,
    val title: String,
    val startTime: Long,
    val endTime: Long,
    val notes: String? = null
)
