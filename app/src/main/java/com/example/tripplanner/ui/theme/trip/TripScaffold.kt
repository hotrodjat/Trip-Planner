package com.example.tripplanner.ui.theme.trip

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tripplanner.ui.theme.trip.navigation.TripTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripScaffold(
    onExitTrip: () -> Unit
) {
    val navController = rememberNavController()
    val tabs = listOf(
        TripTab.Overview,
        TripTab.Schedule,
        TripTab.Budget,
        TripTab.Logistics,
        TripTab.More
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trip Name") },
                navigationIcon = {
                    IconButton(onClick = onExitTrip) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val currentRoute = currentRoute(navController)
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
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

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}