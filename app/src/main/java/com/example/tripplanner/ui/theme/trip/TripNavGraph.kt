package com.example.tripplanner.ui.theme.trip

import BudgetScreen
import OverviewScreen
import ScheduleScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tripplanner.ui.theme.trip.logistics.LogisticsScreen
import com.example.tripplanner.ui.theme.trip.more.MoreScreen
import com.example.tripplanner.ui.theme.trip.navigation.TripTab

@Composable
fun TripNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = TripTab.Overview.route,
        modifier = modifier
    ) {
        composable(TripTab.Overview.route) { OverviewScreen() }
        composable(TripTab.Schedule.route) { ScheduleScreen() }
        composable(TripTab.Budget.route) { BudgetScreen() }
        composable(TripTab.Logistics.route) { LogisticsScreen() }
        composable(TripTab.More.route) { MoreScreen() }
    }
}