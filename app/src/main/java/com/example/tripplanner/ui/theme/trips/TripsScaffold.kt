package com.example.tripplanner.ui.theme.trips

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScaffold(
    onTripSelected: (String) -> Unit,
    onAccountClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trip Planner") },
                actions = {
                    IconButton(onClick = onAccountClick) {
                        Icon(Icons.Default.Person, contentDescription = "Account")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Create Trip */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Trip")
            }
        }
    ) { padding ->
        TripsListScreen(
            modifier = Modifier.padding(padding),
            onTripSelected = onTripSelected
        )
    }
}
