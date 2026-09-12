package com.example.tripplanner.ui.theme.trip

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripplanner.data.entity.ExpenseEntity
import com.example.tripplanner.data.entity.ExpenseSplitEntity
import com.example.tripplanner.data.entity.ExpenseWithSplits
import com.example.tripplanner.data.entity.LogisticsEntity
import com.example.tripplanner.data.entity.PersonEntity
import com.example.tripplanner.data.entity.ScheduleEntity
import com.example.tripplanner.data.repository.TripRepository
import com.example.tripplanner.ui.theme.trip.schedule.ScheduleEventUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

val LocalTripViewModel = staticCompositionLocalOf<TripViewModel> {
    error("TripViewModel not provided")
}

class TripViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: TripRepository
) : ViewModel() {

    val tripId: Long =
        savedStateHandle["tripId"] ?: error("tripId missing")

    var selectedTab: String
        get() = savedStateHandle["selectedTab"] ?: "overview"
        set(value) { savedStateHandle["selectedTab"] = value }

    val expense: StateFlow<Int> =
        repository.getExpense(tripId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    val events: StateFlow<List<ScheduleEntity>> =
        repository.getScheduleForTrip(tripId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val people: StateFlow<List<PersonEntity>> =
        repository.getPeopleForTrip(tripId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val expenses: StateFlow<List<ExpenseEntity>> =
        repository.getExpensesForTrip(tripId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val logistics: StateFlow<List<LogisticsEntity>> =
        repository.getLogisticsForTrip(tripId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val totalExpense: StateFlow<Int> = expenses.map { list ->
        list.sumOf { it.total }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0
    )

    val expensesWithSplits: StateFlow<List<ExpenseWithSplits>> =
        repository.getExpensesWithSplitsForTrip(tripId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    // Error state management
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Loading state management
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private fun setError(message: String?) {
        _errorMessage.value = message
    }

    private fun clearError() {
        _errorMessage.value = null
    }

    fun addEvent(event: ScheduleEventUi) {
        // Validate input
        if (event.title.isBlank()) {
            setError("Event title cannot be empty")
            return
        }
        
        if (event.endTime < event.startTime) {
            setError("End time must be after start time")
            return
        }

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.addEvent(tripId, event)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to add event"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToExpense(amount: Int, name: String) {
        if (amount <= 0) {
            setError("Amount must be greater than zero")
            return
        }

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.addToExpense(tripId, amount, name)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to add expense"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteEvent(scheduleId: Long) {
        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.deleteSchedule(scheduleId)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to delete event"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateEvent(schedule: ScheduleEntity) {
        if (schedule.title.isBlank()) {
            setError("Event title cannot be empty")
            return
        }

        if (schedule.endTime < schedule.startTime) {
            setError("End time must be after start time")
            return
        }

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.updateSchedule(schedule)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to update event"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addPerson(person: PersonEntity) {
        // Validate input
        if (person.firstName.isBlank()) {
            setError("First name cannot be empty")
            return
        }

        if (person.lastName.isBlank()) {
            setError("Last name cannot be empty")
            return
        }

        val personWithTrip = person.copy(tripId = tripId)

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.addPerson(personWithTrip)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to add person"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deletePerson(personId: Long) {
        if (personId <= 0) {
            setError("Invalid person ID")
            return
        }

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.deletePersonById(personId)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to delete person"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updatePerson(person: PersonEntity) {
        if (person.firstName.isBlank()) {
            setError("First name cannot be empty")
            return
        }

        if (person.lastName.isBlank()) {
            setError("Last name cannot be empty")
            return
        }

        if (person.personId <= 0) {
            setError("Invalid person ID for update")
            return
        }

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.updatePerson(person)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to update person"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addExpense(expense: ExpenseEntity) {
        if (expense.total <= 0) {
            setError("Expense amount must be greater than zero")
            return
        }

        val expenseWithTrip = expense.copy(tripId = tripId)

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.addExpense(expenseWithTrip)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to add expense"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteExpense(expenseId: Long) {
        if (expenseId <= 0) {
            setError("Invalid expense ID")
            return
        }

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.deleteExpenseById(expenseId)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to delete expense"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateExpense(expense: ExpenseEntity) {
        if (expense.total <= 0) {
            setError("Expense amount must be greater than zero")
            return
        }

        if (expense.expenseId <= 0) {
            setError("Invalid expense ID for update")
            return
        }

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.updateExpense(expense)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to update expense"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addSplits(splits: List<ExpenseSplitEntity>) {
        if (splits.isEmpty()) {
            setError("Splits list cannot be empty")
            return
        }

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.addSplits(splits)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to add expense splits"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addSplit(split: ExpenseSplitEntity) {
        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.addSplit(split)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to add expense split"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteSplitsForExpense(expenseId: Long) {
        if (expenseId <= 0) {
            setError("Invalid expense ID")
            return
        }

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.deleteSplitsForExpense(expenseId)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to delete expense splits"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addLogistics(logistics: LogisticsEntity) {
        if (logistics.title.isBlank()) {
            setError("Logistics title cannot be empty")
            return
        }

        val logisticsWithTrip = logistics.copy(tripId = tripId)

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.addLogistics(logisticsWithTrip)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to add logistics item"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateLogistics(logistics: LogisticsEntity) {
        if (logistics.title.isBlank()) {
            setError("Logistics title cannot be empty")
            return
        }

        if (logistics.logisticsId <= 0) {
            setError("Invalid logistics ID for update")
            return
        }

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.updateLogistics(logistics)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to update logistics item"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteLogistics(logistics: LogisticsEntity) {

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                repository.deleteLogistics(logistics)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to delete logistics item"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
