package com.example.tripplanner.ui.theme.trip

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val tripTitle by tripViewModel.tripTitle.collectAsState()
    var renameDialogOpen by remember { mutableStateOf(false) }
    var draftTripName by remember(tripTitle) { mutableStateOf(tripTitle) }

    val application = LocalContext.current.applicationContext as Application
    val tripDependencies = remember(application) { TripDependencies(application) }

    CompositionLocalProvider(
        LocalTripViewModel provides tripViewModel,
        LocalTripDependencies provides tripDependencies
    ) {
        val currentBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = currentBackStackEntry?.destination?.route

        if (renameDialogOpen) {
            AlertDialog(
                onDismissRequest = { renameDialogOpen = false },
                title = { Text("Rename trip") },
                text = {
                    OutlinedTextField(
                        value = draftTripName,
                        onValueChange = { draftTripName = it },
                        singleLine = true,
                        label = { Text("Trip name") }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            tripViewModel.updateTripTitle(draftTripName)
                            renameDialogOpen = false
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { renameDialogOpen = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(tripTitle) },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (currentRoute?.startsWith("more/") == true) {
                                navController.popBackStack(TripTab.More.route, false)
                            } else {
                                onExitTrip()
                            }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { renameDialogOpen = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Rename trip")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
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
