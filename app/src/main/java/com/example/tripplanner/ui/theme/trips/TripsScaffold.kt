package com.example.tripplanner.ui.theme.trips

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tripplanner.ui.theme.trip.LocalTripDependencies

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScaffold(
    onTripSelected: (Long) -> Unit,
    onAccountClick: () -> Unit
) {
    val dependencies = LocalTripDependencies.current

    val viewModel: TripsViewModel = viewModel(
        factory = viewModelFactory {
            initializer { TripsViewModel(dependencies.tripRepository) }
        }
    )

    val trips by viewModel.trips.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

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
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Trip")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            TripsListScreen(
                modifier = Modifier.fillMaxSize(),
                onTripSelected = onTripSelected,
                trips = trips
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp)
                )
            }

            errorMessage?.let { message ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(message)
                }
            }
        }

        if (showAddDialog) {
            AddTripDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { title, description, startDate, endDate ->
                    viewModel.addTrip(
                        title = title,
                        description = description,
                        startDate = startDate,
                        endDate = endDate
                    )
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
private fun AddTripDialog(
    onDismiss: () -> Unit,
    onAdd: (title: String, description: String?, startDate: Long?, endDate: Long?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Create New Trip",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Trip Title
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Trip Name", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("Enter trip name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // Trip Description
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Description (Optional)", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("Enter trip description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp),
                        maxLines = 3
                    )
                }

                // Add button
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onAdd(title, description.takeIf { it.isNotBlank() }, null, null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Create Trip", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}
