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
import com.example.tripplanner.ui.theme.trip.LocalTripDependencies
import com.example.tripplanner.ui.theme.trip.LocalTripViewModel
import com.example.tripplanner.ui.theme.trip.TripDependencies
import com.example.tripplanner.ui.theme.trip.*
import androidx.compose.material3.FloatingActionButton

@Composable
fun ExpenseScreen() {
    val tripViewModel = LocalTripViewModel.current
    val dependencies = LocalTripDependencies.current

    val viewModel: ExpenseViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
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
    val tripExpensesWithSplits by tripViewModel.expensesWithSplits.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var expenseToEdit by remember { mutableStateOf<ExpenseEntity?>(null) }
    var expenseToDelete by remember { mutableStateOf<ExpenseEntity?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            LoadingIndicator()
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
                    ErrorBanner(message)
                }

                // Expense list
                if (expenses.isEmpty()) {
                    EmptyStateCard("No expenses yet. Add one to get started!")
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(expenses) { expense ->
                            ExpenseCard(
                                expense = expense,
                                people = people,
                                onDelete = { expenseToDelete = expense },
                                onEdit = {
                                    expenseToEdit = expense
                                },
                                onSelect = { viewModel.selectExpense(expense) },
                                isSelected = selectedExpense?.expenseId == expense.expenseId
                            )
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add expense"
                    )
                }
            }
        }

        if (showAddDialog || expenseToEdit != null) {
            val editingExpense = expenseToEdit
            val existingSplitPersonIds = tripExpensesWithSplits
                .firstOrNull { it.expense.expenseId == editingExpense?.expenseId }
                ?.splits
                ?.map { it.personId }
                ?: emptyList()

            AddExpenseDialog(
                people = people,
                logistics = logistics,
                initialExpense = editingExpense,
                initialSelectedSplitPersonIds = existingSplitPersonIds,
                onDismiss = {
                    showAddDialog = false
                    expenseToEdit = null
                },
                onAdd = { name, amount, paidBy, splitWith, logisticsId, notes ->
                    if (editingExpense != null) {
                        viewModel.updateExpense(
                            expense = editingExpense.copy(
                                name = name,
                                total = amount,
                                paidByPersonId = paidBy,
                                logisticsId = logisticsId,
                                notes = notes
                            ),
                            splitWithPersonIds = splitWith
                        )
                    } else {
                        viewModel.addExpense(
                            name = name,
                            total = amount,
                            paidByPersonId = paidBy,
                            logisticsId = logisticsId,
                            notes = notes,
                            splitWithPersonIds = splitWith
                        )
                    }
                    showAddDialog = false
                    expenseToEdit = null
                }
            )
        }

        expenseToDelete?.let { expense ->
            AlertDialog(
                onDismissRequest = { expenseToDelete = null },
                title = { Text("Delete expense?") },
                text = { Text("Delete '${expense.name}'? This will also remove its related split entries.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteExpense(expense.expenseId)
                            expenseToDelete = null
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { expenseToDelete = null }) {
                        Text("Cancel")
                    }
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
    onEdit: () -> Unit,
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit expense",
                        tint = MaterialTheme.colorScheme.primary
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
}

@Composable
fun AddExpenseDialog(
    people: List<PersonEntity>,
    logistics: List<LogisticsEntity>,
    initialExpense: ExpenseEntity? = null,
    initialSelectedSplitPersonIds: List<Long> = emptyList(),
    onDismiss: () -> Unit,
    onAdd: (name: String, amount: Int, paidBy: Long?, splitWith: List<Long>, logisticsId: Long?, notes: String?) -> Unit
) {
    var name by remember(initialExpense?.expenseId) { mutableStateOf(initialExpense?.name.orEmpty()) }
    var amount by remember(initialExpense?.expenseId) { mutableStateOf(initialExpense?.total?.toString().orEmpty()) }
    var paidBy by remember(initialExpense?.expenseId, people) {
        mutableStateOf(people.find { it.personId == initialExpense?.paidByPersonId })
    }
    var selectedSplits by remember(initialExpense?.expenseId, initialSelectedSplitPersonIds) {
        mutableStateOf(initialSelectedSplitPersonIds.toSet())
    }
    var selectedLogistics by remember(initialExpense?.expenseId, logistics) {
        mutableStateOf(logistics.find { it.logisticsId == initialExpense?.logisticsId })
    }
    var notes by remember(initialExpense?.expenseId) { mutableStateOf(initialExpense?.notes.orEmpty()) }
    var validationError by remember(initialExpense?.expenseId) { mutableStateOf<String?>(null) }

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
                        if (initialExpense == null) "Add New Expense" else "Edit Expense",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Name", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; validationError = null },
                        placeholder = { Text("Enter expense name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = validationError != null && name.isBlank()
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Amount", style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it; validationError = null },
                        placeholder = { Text("Enter amount") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = validationError != null && (amount.toIntOrNull() == null || amount.toIntOrNull()!! <= 0)
                    )
                }

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
                            },
                            isError = validationError != null && paidBy == null
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
                                        validationError = null
                                    }
                                )
                            }
                        }
                    }
                }

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

                validationError?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Button(
                    onClick = {
                        val amountInt = amount.toIntOrNull()
                        when {
                            name.isBlank() -> validationError = "Expense name is required"
                            amountInt == null || amountInt <= 0 -> validationError = "Amount must be a positive number"
                            paidBy == null -> validationError = "Please select who paid for this expense"
                            else -> {
                                validationError = null
                                onAdd(
                                    name.trim(),
                                    amountInt,
                                    paidBy?.personId,
                                    selectedSplits.toList(),
                                    selectedLogistics?.logisticsId,
                                    notes.ifBlank { null }
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(if (initialExpense == null) "Add Expense" else "Save Changes", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}
