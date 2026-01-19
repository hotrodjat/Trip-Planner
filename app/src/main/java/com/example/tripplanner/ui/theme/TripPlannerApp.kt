// Jetpack Compose – App Scaffolds (UI-only, fake data)
// Purpose: Translate wireframes into composable structure + navigation
// Assumes: Material3, Navigation-Compose

package com.example.tripplanner.ui.theme

import BookingsScreen
import BudgetScreen
import OverviewScreen
import ScheduleScreen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// -----------------------------
// App Entry
// -----------------------------

@Composable
fun TripPlannerApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = { GlobalFab(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Overview.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Overview.route) { OverviewScreen() }
            composable(Screen.Schedule.route) { ScheduleScreen() }
            composable(Screen.Budget.route) { BudgetScreen() }
            composable(Screen.Bookings.route) { BookingsScreen() }
        }
    }
}

// -----------------------------
// Navigation
// -----------------------------

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Overview : Screen("overview", "Overview", Icons.Default.Home)
    object Schedule : Screen("schedule", "Schedule", Icons.Default.DateRange)
    object Budget : Screen("budget", "Budget", Icons.Filled.Email)
    object Bookings : Screen("bookings", "Bookings", Icons.AutoMirrored.Filled.List)
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(Screen.Overview, Screen.Schedule, Screen.Budget, Screen.Bookings)

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate(screen.route) },
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) }
            )
        }
    }
}

@Composable
fun GlobalFab(navController: NavHostController) {
    FloatingActionButton(onClick = {
        // Context-aware in real app (add event / add expense)
        navController.navigate(Screen.Schedule.route)
    }) {
        Icon(Icons.Default.Add, contentDescription = "Add")
    }
}








