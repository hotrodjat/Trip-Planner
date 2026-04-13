package com.example.tripplanner.ui.theme.trip.expense

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tripplanner.data.entity.ExpenseEntity
import com.example.tripplanner.data.entity.LogisticsEntity
import com.example.tripplanner.data.entity.PersonEntity
import com.example.tripplanner.data.repository.TripRepository
import com.example.tripplanner.ui.theme.trip.LocalTripViewModel
import com.example.tripplanner.ui.theme.trip.TripDependencies

@Composable
fun ExpenseScreen() {
    val tripViewModel = LocalTripViewModel.current
    val application = LocalContext.current.applicationContext as Application

    val viewModel: ExpenseViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                val dependencies = TripDependencies(application)
                ExpenseViewModel(
                    tripViewModel,
                    dependencies.expenseRepository,
                    dependencies.tripRepository
                )
            }
        }
    )

    val expenses by viewModel.expenses.collectAsState()
    val people by viewModel.people.collectAsState()
    val logistics by viewModel.logistics.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedExpense by viewModel.selectedExpense.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with total
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Expenses",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            "Total: $$totalExpense",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Error message display
                errorMessage?.let { message ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            message,
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                // Expense list
                if (expenses.isEmpty()) {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No expenses yet. Add one to get started!",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(expenses) { expense ->
                            ExpenseCard(
                                expense = expense,
                                people = people,
                                onDelete = { viewModel.deleteExpense(expense.expenseId) },
                                onSelect = { viewModel.selectExpense(expense) },
                                isSelected = selectedExpense?.expenseId == expense.expenseId
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Add expense button
                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add expense"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Add Expense")
                }
            }
        }

        // Add expense dialog
        if (showAddDialog) {
            AddExpenseDialog(
                people = people,
                logistics = logistics,
                onDismiss = { showAddDialog = false },
                onAdd = { name, amount, paidBy, splitWith, logisticsId, notes ->
                    viewModel.addExpense(
                        name = name,
                        total = amount,
                        paidByPersonId = paidBy,
                        logisticsId = logisticsId,
                        notes = notes,
                        splitWithPersonIds = splitWith
                    )
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun ExpenseCard(
    expense: ExpenseEntity,
    people: List<PersonEntity>,
    onDelete: () -> Unit,
    onSelect: () -> Unit,
    isSelected: Boolean
) {
    val paidByPerson = people.find { it.personId == expense.paidByPersonId }
    val paidByName = paidByPerson?.let { "${it.firstName} ${it.lastName}" } ?: "Unknown"

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = onSelect
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
                    expense.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "$${expense.total}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                if (!expense.notes.isNullOrBlank()) {
                    Text(
                        expense.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    "Paid by: $paidByName",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete expense",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AddExpenseDialog(
    people: List<PersonEntity>,
    logistics: List<LogisticsEntity>,
    onDismiss: () -> Unit,
    onAdd: (name: String, amount: Int, paidBy: Long?, splitWith: List<Long>, logisticsId: Long?, notes: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var paidBy by remember { mutableStateOf<PersonEntity?>(null) }
    var selectedSplits by remember { mutableStateOf(setOf<Long>()) }
    var selectedLogistics by remember { mutableStateOf<LogisticsEntity?>(null) }
    var notes by remember { mutableStateOf("") }

    var paidByExpanded by remember { mutableStateOf(false) }
    var splitWithExpanded by remember { mutableStateOf(false) }
    var logisticsExpanded by remember { mutableStateOf(false) }

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
                        "Add New Expense",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Name
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Name", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Enter expense name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // Amount
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Amount", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        placeholder = { Text("Enter amount") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // Paid By Dropdown
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Paid By", style = MaterialTheme.typography.labelLarge)
                    Box {
                        OutlinedTextField(
                            value = paidBy?.let { "${it.firstName} ${it.lastName}" } ?: "Select person",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = { paidByExpanded = true }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = paidByExpanded,
                            onDismissRequest = { paidByExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            people.forEach { person ->
                                DropdownMenuItem(
                                    text = { Text("${person.firstName} ${person.lastName}") },
                                    onClick = {
                                        paidBy = person
                                        paidByExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Split With Dropdown (Multi-select)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Split With", style = MaterialTheme.typography.labelLarge)
                    Box {
                        OutlinedTextField(
                            value = if (selectedSplits.isEmpty()) "None" else "${selectedSplits.size} people selected",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = { splitWithExpanded = true }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = splitWithExpanded,
                            onDismissRequest = { splitWithExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            people.forEach { person ->
                                val isSelected = selectedSplits.contains(person.personId)
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(
                                                checked = isSelected,
                                                onCheckedChange = null
                                            )
                                            Text("${person.firstName} ${person.lastName}", modifier = Modifier.padding(start = 8.dp))
                                        }
                                    },
                                    onClick = {
                                        selectedSplits = if (isSelected) {
                                            selectedSplits - person.personId
                                        } else {
                                            selectedSplits + person.personId
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                // Logistics Association Dropdown
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Logistics Item (Optional)", style = MaterialTheme.typography.labelLarge)
                    Box {
                        OutlinedTextField(
                            value = selectedLogistics?.title ?: "None",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = { logisticsExpanded = true }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = logisticsExpanded,
                            onDismissRequest = { logisticsExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            DropdownMenuItem(
                                text = { Text("None") },
                                onClick = {
                                    selectedLogistics = null
                                    logisticsExpanded = false
                                }
                            )
                            logistics.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text("${item.type}: ${item.title}") },
                                    onClick = {
                                        selectedLogistics = item
                                        logisticsExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Notes
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Notes", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = { Text("Enter notes (optional)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        maxLines = 5
                    )
                }

                Button(
                    onClick = {
                        val amountInt = amount.toIntOrNull() ?: 0
                        if (name.isNotBlank() && amountInt > 0) {
                            onAdd(
                                name,
                                amountInt,
                                paidBy?.personId,
                                selectedSplits.toList(),
                                selectedLogistics?.logisticsId,
                                notes.ifBlank { null }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Add Expense", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}
