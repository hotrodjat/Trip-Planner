// Jetpack Compose – App Scaffolds (UI-only, fake data)
// Purpose: Translate wireframes into composable structure + navigation
// Assumes: Material3, Navigation-Compose

package com.example.tripplanner.ui.theme

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tripplanner.ui.theme.account.AccountScreen
import com.example.tripplanner.ui.theme.core.navigation.Routes
import com.example.tripplanner.ui.theme.trip.TripScaffold
import com.example.tripplanner.ui.theme.trips.TripsScaffold

@Composable
fun TripPlannerApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.TRIPS
    ) {
        composable(Routes.TRIPS) {
            TripsScaffold(
                onTripSelected = { tripId ->
                    navController.navigate(Routes.trip(tripId))
                },
                onAccountClick = {
                    navController.navigate(Routes.ACCOUNT)
                }
            )
        }

        composable(Routes.ACCOUNT) {
            AccountScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.TRIP,
            arguments = listOf(navArgument("tripId") {
                type = NavType.StringType
            })
        ) {
            TripScaffold(
                onExitTrip = { navController.popBackStack() }
            )
        }
    }
}








