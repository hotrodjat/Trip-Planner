package com.example.tripplanner.data.repository

import com.example.tripplanner.data.dao.PersonDao
import com.example.tripplanner.data.dao.TripParticipantDao
import com.example.tripplanner.data.entity.PersonEntity
import com.example.tripplanner.data.entity.TripParticipantEntity
import com.example.tripplanner.data.entity.TripParticipantWithPerson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PersonRepository(
    private val personDao: PersonDao,
    private val tripParticipantDao: TripParticipantDao
) {

    fun getPeopleForTrip(tripId: Long): Flow<List<TripParticipantWithPerson>> =
        tripParticipantDao.getPeopleForTrip(tripId)

    suspend fun getPeopleAvailableForTrip(tripId: Long): List<PersonEntity> =
        personDao.getPeopleNotInTrip(tripId).first()

    fun searchPeople(searchQuery: String): Flow<List<PersonEntity>> =
        personDao.searchPeople("%$searchQuery%")

    suspend fun getPersonById(personId: Long): PersonEntity? =
        personDao.getPersonById(personId)

    suspend fun addPerson(person: PersonEntity) {
        require(person.firstName.isNotBlank()) { "First name cannot be blank" }
        require(person.lastName.isNotBlank()) { "Last name cannot be blank" }
        personDao.insertPerson(person)
    }

    suspend fun addPersonToTrip(
        tripId: Long,
        person: PersonEntity,
        personalBudget: Int = 0,
        notes: String? = null
    ) {
        require(tripId > 0) { "Invalid tripId: $tripId" }
        val personId = if (person.personId > 0) {
            person.personId
        } else {
            personDao.insertPerson(person)
        }
        tripParticipantDao.addParticipant(
            TripParticipantEntity(
                tripId = tripId,
                personId = personId,
                personalBudget = personalBudget.coerceAtLeast(0),
                notes = notes
            )
        )
    }

    suspend fun addExistingPersonToTrip(
        tripId: Long,
        personId: Long,
        personalBudget: Int = 0,
        notes: String? = null
    ) {
        require(tripId > 0) { "Invalid tripId: $tripId" }
        require(personId > 0) { "Invalid personId: $personId" }
        tripParticipantDao.addParticipant(
            TripParticipantEntity(
                tripId = tripId,
                personId = personId,
                personalBudget = personalBudget.coerceAtLeast(0),
                notes = notes
            )
        )
    }

    suspend fun deletePerson(person: PersonEntity) {
        require(person.personId > 0) { "Invalid personId: ${person.personId}" }
        personDao.deletePerson(person)
    }

    suspend fun deletePersonById(personId: Long) {
        require(personId > 0) { "Invalid personId: $personId" }
        personDao.deletePersonById(personId)
    }

    suspend fun deletePersonFromTrip(tripId: Long, personId: Long) {
        require(tripId > 0) { "Invalid tripId: $tripId" }
        require(personId > 0) { "Invalid personId: $personId" }
        tripParticipantDao.deleteParticipant(tripId, personId)
    }

    suspend fun deletePeopleForTrip(tripId: Long) {
        require(tripId > 0) { "Invalid tripId: $tripId" }
        tripParticipantDao.deleteParticipantsForTrip(tripId)
    }

    suspend fun updatePerson(person: PersonEntity) {
        require(person.personId > 0) { "Invalid personId for update: ${person.personId}" }
        require(person.firstName.isNotBlank()) { "First name cannot be blank" }
        require(person.lastName.isNotBlank()) { "Last name cannot be blank" }
        personDao.insertPerson(person)
    }

    suspend fun updateParticipantDetails(
        tripId: Long,
        personId: Long,
        personalBudget: Int,
        notes: String?
    ) {
        require(tripId > 0) { "Invalid tripId: $tripId" }
        require(personId > 0) { "Invalid personId: $personId" }
        tripParticipantDao.updateParticipant(
            tripId = tripId,
            personId = personId,
            budget = personalBudget.coerceAtLeast(0),
            notes = notes
        )
    }
}

