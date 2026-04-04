package com.example.tripplanner.data.repository

import com.example.tripplanner.data.dao.PersonDao
import com.example.tripplanner.data.entity.PersonEntity
import kotlinx.coroutines.flow.Flow

class PersonRepository(
    private val personDao: PersonDao
) {

    fun getPeopleForTrip(tripId: Long): Flow<List<PersonEntity>> =
        personDao.getPeopleForTrip(tripId)

    fun searchPeople(searchQuery: String): Flow<List<PersonEntity>> =
        personDao.searchPeople("%$searchQuery%")

    suspend fun getPersonById(personId: Long): PersonEntity? =
        personDao.getPersonById(personId)

    suspend fun addPerson(person: PersonEntity) {
        require(person.firstName.isNotBlank()) { "First name cannot be blank" }
        require(person.lastName.isNotBlank()) { "Last name cannot be blank" }
        personDao.insertPerson(person)
    }

    suspend fun deletePerson(person: PersonEntity) {
        require(person.personId > 0) { "Invalid personId: ${person.personId}" }
        personDao.deletePerson(person)
    }

    suspend fun deletePersonById(personId: Long) {
        require(personId > 0) { "Invalid personId: $personId" }
        personDao.deletePersonById(personId)
    }

    suspend fun deletePeopleForTrip(tripId: Long) {
        require(tripId > 0) { "Invalid tripId: $tripId" }
        personDao.deletePeopleForTrip(tripId)
    }

    suspend fun updatePerson(person: PersonEntity) {
        require(person.personId > 0) { "Invalid personId for update: ${person.personId}" }
        require(person.firstName.isNotBlank()) { "First name cannot be blank" }
        require(person.lastName.isNotBlank()) { "Last name cannot be blank" }
        personDao.insertPerson(person) // Room replaces on conflict
    }
}

