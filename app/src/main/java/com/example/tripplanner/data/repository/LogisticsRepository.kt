package com.example.tripplanner.data.repository

import com.example.tripplanner.data.dao.LogisticsDao
import com.example.tripplanner.data.entity.LogisticsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LogisticsRepository(
    private val logisticsDao: LogisticsDao
) {

    fun getLogisticsForTrip(tripId: Long): Flow<List<LogisticsEntity>> =
        logisticsDao.getLogisticsForTrip(tripId)

    fun getLogisticsForTripByType(tripId: Long, type: String): Flow<List<LogisticsEntity>> =
        getLogisticsForTrip(tripId).map { it.filter { entity -> entity.type == type } }

    suspend fun addLogistics(logistics: LogisticsEntity) {
        require(logistics.tripId > 0) { "Invalid tripId: ${logistics.tripId}" }
        require(logistics.type.isNotBlank()) { "Logistics type cannot be blank" }
        require(logistics.provider?.isNotBlank() != false) { "Provider cannot be blank if provided" }
        logisticsDao.insertLogistics(logistics)
    }

    suspend fun deleteLogistics(logistics: LogisticsEntity) {
        logisticsDao.deleteLogistics(logistics)
    }

    suspend fun updateLogistics(logistics: LogisticsEntity) {
        require(logistics.logisticsId > 0) { "Invalid logisticsId for update: ${logistics.logisticsId}" }
        logisticsDao.insertLogistics(logistics) // Room replaces on conflict
    }
}

