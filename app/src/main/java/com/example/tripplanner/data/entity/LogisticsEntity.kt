package com.example.tripplanner.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

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
    val title: String,
    val type: String,          // flight, hotel, car, train, activity, tour, etc.
    val provider: String?,
    val location: String? = null,
    val referenceNumber: String?,
    val people: List<Long> = emptyList(),
    val notes: String? = null
)

class PersonIdListConverter {
    @TypeConverter
    fun fromPersonIdList(value: List<Long>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toPersonIdList(value: String?): List<Long> {
        return if (value.isNullOrBlank()) emptyList() else value.split(",").mapNotNull { it.toLongOrNull() }
    }
}

