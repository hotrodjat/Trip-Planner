package com.example.tripplanner.data.repository

import com.example.tripplanner.data.dao.ScheduleDao
import com.example.tripplanner.data.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(
    private val scheduleDao: ScheduleDao
) {

    fun getScheduleForTrip(tripId: Long): Flow<List<ScheduleEntity>> =
        scheduleDao.getScheduleForTrip(tripId)

    fun getScheduleForTripInTimeRange(tripId: Long, startTime: Long, endTime: Long): Flow<List<ScheduleEntity>> =
        scheduleDao.getScheduleForTripInTimeRange(tripId, startTime, endTime)

    suspend fun getScheduleById(scheduleId: Long): ScheduleEntity? =
        scheduleDao.getScheduleById(scheduleId)

    suspend fun addSchedule(schedule: ScheduleEntity) {
        require(schedule.tripId > 0) { "Invalid tripId: ${schedule.tripId}" }
        require(schedule.title.isNotBlank()) { "Schedule title cannot be blank" }
        require(schedule.startTime > 0) { "Start time must be valid" }
        require(schedule.endTime > 0) { "End time must be valid" }
        require(schedule.endTime >= schedule.startTime) { "End time must be after or equal to start time" }
        scheduleDao.insertSchedule(schedule)
    }

    suspend fun deleteSchedule(scheduleId: Long) {
        require(scheduleId > 0) { "Invalid scheduleId: $scheduleId" }
        scheduleDao.deleteSchedule(scheduleId)
    }

    suspend fun deleteScheduleForTrip(tripId: Long) {
        require(tripId > 0) { "Invalid tripId: $tripId" }
        scheduleDao.deleteScheduleForTrip(tripId)
    }

    suspend fun updateSchedule(schedule: ScheduleEntity) {
        require(schedule.scheduleId > 0) { "Invalid scheduleId for update: ${schedule.scheduleId}" }
        require(schedule.title.isNotBlank()) { "Schedule title cannot be blank" }
        require(schedule.endTime >= schedule.startTime) { "End time must be after or equal to start time" }
        scheduleDao.insertSchedule(schedule) // Room replaces on conflict
    }
}

