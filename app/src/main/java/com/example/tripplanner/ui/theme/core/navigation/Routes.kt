package com.example.tripplanner.ui.theme.core.navigation

object Routes {
    const val TRIPS = "trips"
    const val ACCOUNT = "account"
    const val TRIP = "trip/{tripId}"

    fun trip(tripId: Long) = "trip/$tripId"
}
