package com.example.tripplanner.ui.theme.trip

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tripplanner.ui.theme.trip.navigation.TripTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripScaffold(
    tripViewModel: TripViewModel,
//    tripId: String,
    onExitTrip: () -> Unit
) {
    val navController = rememberNavController()

//    val tripViewModel: TripViewModel = viewModel(
//        key = tripId,
//        factory = androidx.lifecycle.viewmodel.compose.viewModelFactory {
//            initializer {
//                TripViewModel(savedStateHandle = androidx.lifecycle.SavedStateHandle(mapOf("tripId" to tripId)))
//            }
//        }
//    )

    val tabs = listOf(TripTab.Overview, TripTab.Expense, TripTab.Logistics, TripTab.Schedule, TripTab.More)

    CompositionLocalProvider(LocalTripViewModel provides tripViewModel) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Trip ${tripViewModel.tripId}") },
                    navigationIcon = {
                        IconButton(onClick = onExitTrip) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    val currentRoute = navController
                        .currentBackStackEntryAsState()
                        .value
                        ?.destination
                        ?.route

                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                }
                                tripViewModel.selectedTab = tab.route
                            },
                            icon = { Icon(tab.icon, null) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        ) { padding ->
            TripNavGraph(
                navController = navController,
                modifier = Modifier.padding(padding)
            )
        }
    }
}
