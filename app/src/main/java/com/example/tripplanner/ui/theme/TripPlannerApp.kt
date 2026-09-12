package com.example.tripplanner.ui.theme

import android.app.Application
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tripplanner.ui.theme.account.AccountScreen
import com.example.tripplanner.ui.theme.core.navigation.Routes
import com.example.tripplanner.ui.theme.trip.LocalTripDependencies
import com.example.tripplanner.ui.theme.trip.LocalTripViewModel
import com.example.tripplanner.ui.theme.trip.TripDependencies
import com.example.tripplanner.ui.theme.trip.TripScaffold
import com.example.tripplanner.ui.theme.trip.TripViewModel
import com.example.tripplanner.ui.theme.trip.TripViewModelFactory
import com.example.tripplanner.ui.theme.trips.TripsScaffold

@Composable
fun TripPlannerApp() {
    val navController = rememberNavController()
    val application = LocalContext.current.applicationContext as Application
    val dependencies = remember { TripDependencies(application) }

    CompositionLocalProvider(LocalTripDependencies provides dependencies) {
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
                arguments = listOf(navArgument("tripId") { type = NavType.LongType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getLong("tripId") ?: 0L

                val tripViewModel: TripViewModel = viewModel(
                    key = tripId.toString(),
                    factory = TripViewModelFactory(
                        savedStateHandle = backStackEntry.savedStateHandle.apply {
                            this["tripId"] = tripId
                        },
                        application = application
                    )
                )

                CompositionLocalProvider(LocalTripViewModel provides tripViewModel) {
                    TripScaffold(
                        tripViewModel = tripViewModel,
                        onExitTrip = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
