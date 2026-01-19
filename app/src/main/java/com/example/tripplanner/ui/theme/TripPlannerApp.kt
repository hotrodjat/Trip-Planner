// Jetpack Compose – App Scaffolds (UI-only, fake data)
// Purpose: Translate wireframes into composable structure + navigation
// Assumes: Material3, Navigation-Compose

package com.example.tripplanner.ui.theme

import BookingsScreen
import BudgetScreen
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

// -----------------------------
// Overview Screen
// -----------------------------

@Composable
fun OverviewScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Italy Trip 🇮🇹", style = MaterialTheme.typography.headlineSmall)
                    Text("June 10 – June 20 • 3 people")
                    Spacer(Modifier.height(8.dp))
                    Text("5 days to go", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        item { Spacer(Modifier.height(12.dp)) }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Budget", style = MaterialTheme.typography.titleMedium)
                    LinearProgressIndicator(progress = 0.7f, modifier = Modifier.fillMaxWidth())
                    Text("$2,100 spent / $3,000")
                }
            }
        }

        item { Spacer(Modifier.height(12.dp)) }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Next Event", style = MaterialTheme.typography.titleMedium)
                    Text("✈ Flight to Rome – 08:40")
                }
            }
        }
    }
}

// -----------------------------
// Schedule Screen
// -----------------------------

@Composable
fun ScheduleScreen() {
    val events = listOf(
        "08:40 ✈ Flight to Rome",
        "13:00 🏨 Hotel Check-in",
        "15:00 🍝 Lunch",
        "18:30 🚶 Walking Tour"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("June 10 – Day 1", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(events) { event ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Text(event, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}




