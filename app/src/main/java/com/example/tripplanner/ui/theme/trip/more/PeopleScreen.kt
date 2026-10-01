package com.example.tripplanner.ui.theme.trip.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tripplanner.data.entity.PersonEntity
import com.example.tripplanner.data.entity.TripParticipantWithPerson
import com.example.tripplanner.ui.theme.trip.LocalTripDependencies
import com.example.tripplanner.ui.theme.trip.LocalTripViewModel
import com.example.tripplanner.ui.theme.trip.person.PersonViewModel

@Composable
fun PeopleScreen() {
    val tripViewModel = LocalTripViewModel.current
    val dependencies = LocalTripDependencies.current

    val viewModel: PersonViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                PersonViewModel(
                    tripViewModel = tripViewModel,
                    personRepository = dependencies.personRepository
                )
            }
        }
    )

    val people by viewModel.people.collectAsState()
    val availablePeople by viewModel.availablePeople.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var showNewPersonDialog by remember { mutableStateOf(false) }
    var personToEdit by remember { mutableStateOf<TripParticipantWithPerson?>(null) }
    var personToDelete by remember { mutableStateOf<TripParticipantWithPerson?>(null) }
    var showExistingPeopleDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadAvailablePeople()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "People",
                style = MaterialTheme.typography.headlineMedium
            )

            errorMessage?.let { message ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            if (people.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No people on this trip yet.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(people) { person ->
                        PersonCard(
                            person = person,
                            onEdit = { personToEdit = person },
                            onDelete = { personToDelete = person }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add person")
        }

        if (isLoading) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    if (showAddDialog) {
        AddPersonChoiceDialog(
            onDismiss = { showAddDialog = false },
            onNewPerson = {
                showAddDialog = false
                showNewPersonDialog = true
            },
            onExistingPerson = {
                showAddDialog = false
                showExistingPeopleDialog = true
            }
        )
    }

    if (showNewPersonDialog || personToEdit != null) {
        PersonDialog(
            person = personToEdit,
            onDismiss = {
                showNewPersonDialog = false
                personToEdit = null
            },
            onSave = { person, budget, notes ->
                if (personToEdit == null) {
                    viewModel.addPerson(person, budget, notes)
                } else {
                    viewModel.updatePerson(person, budget, notes)
                }
                showNewPersonDialog = false
                personToEdit = null
            }
        )
    }

    if (showExistingPeopleDialog) {
        ExistingPeopleDialog(
            people = availablePeople,
            onDismiss = { showExistingPeopleDialog = false },
            onSelect = { chosenPerson ->
                viewModel.addExistingPerson(chosenPerson.personId)
                showExistingPeopleDialog = false
            }
        )
    }

    personToDelete?.let { person ->
        AlertDialog(
            onDismissRequest = { personToDelete = null },
            title = { Text("Remove person?") },
            text = { Text("Remove ${person.firstName} ${person.lastName} from this trip?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePerson(person.personId)
                        personToDelete = null
                    }
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { personToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun PersonCard(
    person: TripParticipantWithPerson,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = person.fullName,
                    style = MaterialTheme.typography.titleMedium
                )
                if (person.personalBudget > 0) {
                    Text(
                        text = "Budget: $${person.personalBudget}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (!person.notes.isNullOrBlank()) {
                    Text(
                        text = person.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit person")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete person")
                }
            }
        }
    }
}

@Composable
private fun AddPersonChoiceDialog(
    onDismiss: () -> Unit,
    onNewPerson: () -> Unit,
    onExistingPerson: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add person") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Would you like to create a new person or add an existing one?")
                TextButton(onClick = onNewPerson) {
                    Text("New person")
                }
                TextButton(onClick = onExistingPerson) {
                    Text("Existing person")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun ExistingPeopleDialog(
    people: List<PersonEntity>,
    onDismiss: () -> Unit,
    onSelect: (PersonEntity) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add existing person") },
        text = {
            if (people.isEmpty()) {
                Text("No saved people are available to add to this trip.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(people) { person ->
                        TextButton(
                            onClick = { onSelect(person) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("${person.firstName} ${person.lastName}")
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun PersonDialog(
    person: TripParticipantWithPerson?,
    onDismiss: () -> Unit,
    onSave: (PersonEntity, Int, String?) -> Unit
) {
    var firstName by remember(person?.personId) { mutableStateOf(person?.firstName.orEmpty()) }
    var lastName by remember(person?.personId) { mutableStateOf(person?.lastName.orEmpty()) }
    var budgetText by remember(person?.personId) { mutableStateOf((person?.personalBudget ?: 0).toString()) }
    var notes by remember(person?.personId) { mutableStateOf(person?.notes.orEmpty()) }

    val canSave = firstName.isNotBlank() && lastName.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (person == null) "Add person" else "Edit person") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = budgetText,
                    onValueChange = { budgetText = it },
                    label = { Text("Personal budget") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = {
                    val parsedBudget = budgetText.toIntOrNull() ?: 0
                    val basePerson = person?.let {
                        PersonEntity(
                            personId = it.personId,
                            firstName = it.firstName,
                            lastName = it.lastName
                        )
                    } ?: PersonEntity(
                        firstName = firstName.trim(),
                        lastName = lastName.trim()
                    )
                    onSave(basePerson, parsedBudget, notes.trim().ifBlank { null })
                }
            ) {
                Text(if (person == null) "Add" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
