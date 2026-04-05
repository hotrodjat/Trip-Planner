package com.example.tripplanner.ui.theme.trip.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripplanner.data.entity.ExpenseEntity
import com.example.tripplanner.data.repository.ExpenseRepository
import com.example.tripplanner.ui.theme.trip.TripViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val tripViewModel: TripViewModel,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val tripId: Long
        get() = tripViewModel.tripId

    // Delegate to TripViewModel's expenses for reactive updates
    val expenses: StateFlow<List<ExpenseEntity>> = tripViewModel.expenses

    // Total expense tracking
    val totalExpense: StateFlow<Int> = tripViewModel.totalExpense

    // Error state management
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Loading state management
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Selected expense for detail view
    private val _selectedExpense = MutableStateFlow<ExpenseEntity?>(null)
    val selectedExpense: StateFlow<ExpenseEntity?> = _selectedExpense.asStateFlow()

    // Filter state
    private val _filterByPerson = MutableStateFlow<Long?>(null)
    val filterByPerson: StateFlow<Long?> = _filterByPerson.asStateFlow()

    private fun setError(message: String?) {
        _errorMessage.value = message
    }

    private fun clearError() {
        _errorMessage.value = null
    }

    fun addExpense(expense: ExpenseEntity) {
        // Validate input
        if (expense.total <= 0) {
            setError("Expense amount must be greater than zero")
            return
        }
        if (expense.name.isBlank()) {
            setError("Expense name must not be blank")
            return
        }

        val expenseWithTrip = expense.copy(tripId = tripId)

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                expenseRepository.addExpense(expenseWithTrip)
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
                expenseRepository.deleteExpenseById(expenseId)
                if (_selectedExpense.value?.expenseId == expenseId) {
                    _selectedExpense.value = null
                }
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
        if (expense.name.isBlank()) {
            setError("Expense name must not be blank")
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
                expenseRepository.updateExpense(expense)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to update expense"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectExpense(expense: ExpenseEntity) {
        _selectedExpense.value = expense
    }

    fun deselectExpense() {
        _selectedExpense.value = null
    }

    fun setFilterByPerson(personId: Long?) {
        _filterByPerson.value = personId
    }
}
