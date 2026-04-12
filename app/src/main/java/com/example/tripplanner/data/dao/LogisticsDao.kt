package com.example.tripplanner.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tripplanner.data.entity.LogisticsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogisticsDao {

    @Query("SELECT * FROM logistics WHERE tripId = :tripId ORDER BY logisticsId DESC")
    fun getLogisticsForTrip(tripId: Long): Flow<List<LogisticsEntity>>

    @Query("SELECT * FROM logistics WHERE logisticsId = :logisticsId")
    suspend fun getLogisticsById(logisticsId: Long): LogisticsEntity?

    @Query("SELECT * FROM logistics WHERE tripId = :tripId AND type = :type ORDER BY logisticsId DESC")
    fun getLogisticsForTripByType(tripId: Long, type: String): Flow<List<LogisticsEntity>>

    @Query("SELECT * FROM logistics WHERE tripId = :tripId AND location = :location ORDER BY logisticsId DESC")
    fun getLogisticsForTripByLocation(tripId: Long, location: String): Flow<List<LogisticsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogistics(logistics: LogisticsEntity): Long

    @Delete
    suspend fun deleteLogistics(vararg logistics: LogisticsEntity)
}
