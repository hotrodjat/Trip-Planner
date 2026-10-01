package com.example.tripplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tripplanner.data.entity.TripParticipantEntity
import com.example.tripplanner.data.entity.TripParticipantWithPerson
import kotlinx.coroutines.flow.Flow

@Dao
interface TripParticipantDao {

    @Query(
        "SELECT p.personId, p.firstName, p.lastName, tp.tripId, tp.personalBudget, tp.notes " +
            "FROM trip_participants tp " +
            "JOIN people p ON p.personId = tp.personId " +
            "WHERE tp.tripId = :tripId " +
            "ORDER BY p.firstName ASC, p.lastName ASC"
    )
    fun getPeopleForTrip(tripId: Long): Flow<List<TripParticipantWithPerson>>

    @Query(
        "SELECT p.personId, p.firstName, p.lastName, tp.tripId, tp.personalBudget, tp.notes " +
            "FROM trip_participants tp " +
            "JOIN people p ON p.personId = tp.personId " +
            "WHERE tp.tripId = :tripId AND tp.personId = :personId"
    )
    suspend fun getPersonForTrip(tripId: Long, personId: Long): TripParticipantWithPerson?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addParticipant(participant: TripParticipantEntity): Long

    @Query("UPDATE trip_participants SET personalBudget = :budget, notes = :notes WHERE tripId = :tripId AND personId = :personId")
    suspend fun updateParticipant(tripId: Long, personId: Long, budget: Int, notes: String?)

    @Query("DELETE FROM trip_participants WHERE tripId = :tripId AND personId = :personId")
    suspend fun deleteParticipant(tripId: Long, personId: Long)

    @Query("DELETE FROM trip_participants WHERE tripId = :tripId")
    suspend fun deleteParticipantsForTrip(tripId: Long)
}
