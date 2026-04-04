package com.example.tripplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tripplanner.data.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedules WHERE tripId = :tripId ORDER BY startTime ASC")
    fun getScheduleForTrip(tripId: Long): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedules WHERE scheduleId = :scheduleId")
    suspend fun getScheduleById(scheduleId: Long): ScheduleEntity?

    @Query("SELECT * FROM schedules WHERE tripId = :tripId AND startTime BETWEEN :startTime AND :endTime ORDER BY startTime ASC")
    fun getScheduleForTripInTimeRange(tripId: Long, startTime: Long, endTime: Long): Flow<List<ScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity): Long

    @Query("DELETE FROM schedules WHERE scheduleId = :scheduleId")
    suspend fun deleteSchedule(scheduleId: Long)

    @Query("DELETE FROM schedules WHERE tripId = :tripId")
    suspend fun deleteScheduleForTrip(tripId: Long)
}
