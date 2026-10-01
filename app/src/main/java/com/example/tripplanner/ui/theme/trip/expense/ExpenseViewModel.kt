package com.example.tripplanner.ui.theme.trip.expense

import com.example.tripplanner.data.entity.ExpenseEntity
import com.example.tripplanner.data.entity.ExpenseSplitEntity
import com.example.tripplanner.data.entity.LogisticsEntity
import com.example.tripplanner.data.entity.TripParticipantWithPerson
import com.example.tripplanner.data.repository.ExpenseRepository
import com.example.tripplanner.data.repository.TripRepository
import com.example.tripplanner.ui.theme.trip.BaseTripViewModel
import com.example.tripplanner.ui.theme.trip.TripViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpenseViewModel(
    private val tripViewModel: TripViewModel,
    private val expenseRepository: ExpenseRepository,
    private val tripRepository: TripRepository
) : BaseTripViewModel() {

    private val tripId: Long
        get() = tripViewModel.tripId

    // Delegate to TripViewModel for reactive updates
    val expenses: StateFlow<List<ExpenseEntity>> = tripViewModel.expenses
    val people: StateFlow<List<TripParticipantWithPerson>> = tripViewModel.people
    val logistics: StateFlow<List<LogisticsEntity>> = tripViewModel.logistics

    // Total expense tracking
    val totalExpense: StateFlow<Int> = tripViewModel.totalExpense

    // Selected expense for detail view
    private val _selectedExpense = MutableStateFlow<ExpenseEntity?>(null)
    val selectedExpense: StateFlow<ExpenseEntity?> = _selectedExpense.asStateFlow()

    // Filter state
    private val _filterByPerson = MutableStateFlow<Long?>(null)
    val filterByPerson: StateFlow<Long?> = _filterByPerson.asStateFlow()

    private fun buildExpenseSplits(
        expenseId: Long,
        splitWithPersonIds: List<Long>,
        total: Int,
        paidByPersonId: Long?
    ): List<ExpenseSplitEntity> {
        val validSplitIds = splitWithPersonIds
            .distinct()
            .filter { it > 0 }
            .filter { it != paidByPersonId }

        if (validSplitIds.isEmpty()) {
            return emptyList()
        }

        val baseAmount = total / validSplitIds.size
        val remainder = total % validSplitIds.size

        return validSplitIds.mapIndexed { index, personId ->
            ExpenseSplitEntity(
                expenseId = expenseId,
                personId = personId,
                tripId = tripId,
                amount = baseAmount + if (index == 0 && remainder > 0) remainder else 0
            )
        }
    }

    fun addExpense(
        name: String,
        total: Int,
        paidByPersonId: Long?,
        logisticsId: Long?,
        notes: String?,
        splitWithPersonIds: List<Long>
    ) {
        // Validate input
        if (total <= 0) {
            setError("Expense amount must be greater than zero")
            return
        }
        if (name.isBlank()) {
            setError("Expense name must not be blank")
            return
        }
        if (paidByPersonId == null) {
            setError("Please select who paid for this expense")
            return
        }

        executeWithLoading(operation = {
            val expense = ExpenseEntity(
                tripId = tripId,
                name = name,
                total = total,
                paidByPersonId = paidByPersonId,
                logisticsId = logisticsId,
                notes = notes
            )

            val splits = buildExpenseSplits(
                expenseId = 0,
                splitWithPersonIds = splitWithPersonIds,
                total = total,
                paidByPersonId = paidByPersonId
            )

            tripRepository.insertExpenseWithSplits(expense, splits)
        })
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

        executeWithLoading(operation = {
            expenseRepository.addExpense(expenseWithTrip)
        })
    }

    fun deleteExpense(expenseId: Long) {
        if (expenseId <= 0) {
            setError("Invalid expense ID")
            return
        }

        executeWithLoading(operation = {
            expenseRepository.deleteExpenseById(expenseId)
            if (_selectedExpense.value?.expenseId == expenseId) {
                _selectedExpense.value = null
            }
        })
    }

    fun updateExpense(expense: ExpenseEntity, splitWithPersonIds: List<Long>) {
        if (expense.total <= 0) {
            setError("Expense amount must be greater than zero")
            return
        }
        if (expense.name.isBlank()) {
            setError("Expense name must not be blank")
            return
        }
        if (expense.paidByPersonId == null) {
            setError("Please select who paid for this expense")
            return
        }

        if (expense.expenseId <= 0) {
            setError("Invalid expense ID for update")
            return
        }

        executeWithLoading(operation = {
            val splits = buildExpenseSplits(
                expenseId = expense.expenseId,
                splitWithPersonIds = splitWithPersonIds,
                total = expense.total,
                paidByPersonId = expense.paidByPersonId
            )
            tripRepository.updateExpenseWithSplits(expense, splits)
            _selectedExpense.value = expense
        })
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
