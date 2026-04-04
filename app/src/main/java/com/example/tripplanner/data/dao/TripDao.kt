package com.example.tripplanner.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tripplanner.data.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {

    @Query("SELECT * FROM trips ORDER BY createdAt DESC")
    fun getTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE tripId = :tripId")
    fun getTrip(tripId: Long): Flow<TripEntity?>

    @Query("SELECT * FROM trips WHERE title LIKE :searchQuery ORDER BY createdAt DESC")
    fun searchTrips(searchQuery: String): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE createdAt BETWEEN :startTime AND :endTime ORDER BY createdAt DESC")
    fun getTripsInTimeRange(startTime: Long, endTime: Long): Flow<List<TripEntity>>

    @Query("SELECT COUNT(*) FROM trips")
    fun getTripCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity): Long

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Delete
    suspend fun deleteTrip(trip: TripEntity)

    @Query("DELETE FROM trips WHERE tripId = :tripId")
    suspend fun deleteTripById(tripId: Long)

    @Query("SELECT * FROM trips ORDER BY createdAt DESC LIMIT 1")
    fun getMostRecentTrip(): Flow<TripEntity?>

    @Query("SELECT * FROM trips WHERE status = :status ORDER BY createdAt DESC")
    fun getTripsByStatus(status: String): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE location LIKE :locationQuery ORDER BY createdAt DESC")
    fun searchTripsByLocation(locationQuery: String): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE startDate IS NOT NULL AND startDate >= :fromDate ORDER BY startDate ASC")
    fun getUpcomingTrips(fromDate: Long): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE endDate IS NOT NULL AND endDate < :currentDate AND status != 'completed' ORDER BY endDate DESC")
    fun getPastTrips(currentDate: Long): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE startDate IS NOT NULL AND endDate IS NOT NULL AND :date BETWEEN startDate AND endDate ORDER BY startDate ASC")
    fun getTripsActiveOnDate(date: Long): Flow<List<TripEntity>>
}