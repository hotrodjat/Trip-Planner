package com.example.tripplanner.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tripplanner.data.entity.PersonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Query("SELECT * FROM people WHERE tripId = :tripId ORDER BY firstName ASC, lastName ASC")
    fun getPeopleForTrip(tripId: Long): Flow<List<PersonEntity>>

    @Query("SELECT * FROM people WHERE personId = :personId")
    suspend fun getPersonById(personId: Long): PersonEntity?

    @Query("SELECT * FROM people WHERE firstName LIKE :searchQuery OR lastName LIKE :searchQuery")
    fun searchPeople(searchQuery: String): Flow<List<PersonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: PersonEntity): Long

    @Delete
    suspend fun deletePerson(person: PersonEntity)

    @Query("DELETE FROM people WHERE personId = :personId")
    suspend fun deletePersonById(personId: Long)

    @Query("DELETE FROM people WHERE tripId = :tripId")
    suspend fun deletePeopleForTrip(tripId: Long)

    @Query("SELECT * FROM people WHERE tripId = :tripId AND personalBudget > 0 ORDER BY firstName ASC, lastName ASC")
    fun getPeopleForTripWithBudget(tripId: Long): Flow<List<PersonEntity>>

    @Query("UPDATE people SET personalBudget = :budget WHERE personId = :personId")
    suspend fun updatePersonalBudget(personId: Long, budget: Int)
}

