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
    object Expense : TripTab("expense", "Expense", Icons.Default.Info)
    object Logistics : TripTab("logistics", "Logistics", Icons.Default.LocationOn)
    object Schedule : TripTab("schedule", "Schedule", Icons.Default.Info)
    object More : TripTab("more", "More", Icons.Default.Info)
}
