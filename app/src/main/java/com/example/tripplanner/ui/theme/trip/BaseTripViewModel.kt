package com.example.tripplanner.ui.theme.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseTripViewModel : ViewModel() {

    // Error state management
    protected val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Loading state management
    protected val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    protected fun setError(message: String?) {
        _errorMessage.value = message
    }

    protected fun clearError() {
        _errorMessage.value = null
    }

    protected fun executeWithLoading(
        operation: suspend () -> Unit,
        errorMessage: String = "Operation failed"
    ) {
        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                operation()
            } catch (e: Exception) {
                val msg = e.message ?: errorMessage
                setError(msg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
