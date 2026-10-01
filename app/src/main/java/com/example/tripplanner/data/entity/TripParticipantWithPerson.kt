package com.example.tripplanner.data.entity

data class TripParticipantWithPerson(
    val tripId: Long,
    val personId: Long,
    val firstName: String,
    val lastName: String,
    val personalBudget: Int = 0,
    val notes: String? = null
) {
    val fullName: String
        get() = "$firstName $lastName".trim()
}
