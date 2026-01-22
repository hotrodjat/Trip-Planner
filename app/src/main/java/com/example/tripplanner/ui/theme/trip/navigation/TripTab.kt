package com.example.tripplanner.ui.theme.trip.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TripTab(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Overview : TripTab("overview", "Overview", Icons.Default.Home)
    object Budget : TripTab("budget", "Budget", Icons.Default.Info)
    object Logistics : TripTab("logistics/{tripId}", "Logistics", Icons.Default.LocationOn)
    object Schedule : TripTab("schedule/{tripId}", "Schedule", Icons.Default.Info)
    object More : TripTab("more/{tripId}", "More", Icons.Default.Info)
}

