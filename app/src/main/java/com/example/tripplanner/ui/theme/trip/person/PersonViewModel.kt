package com.example.tripplanner.ui.theme.trip.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripplanner.data.entity.PersonEntity
import com.example.tripplanner.data.repository.PersonRepository
import com.example.tripplanner.ui.theme.trip.TripViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonViewModel(
    private val tripViewModel: TripViewModel,
    private val personRepository: PersonRepository
) : ViewModel() {

    private val tripId: Long
        get() = tripViewModel.tripId

    // Delegate to TripViewModel's people for reactive updates
    val people: StateFlow<List<PersonEntity>> = tripViewModel.people

    // Error state management
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Loading state management
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Selected person for detail view
    private val _selectedPerson = MutableStateFlow<PersonEntity?>(null)
    val selectedPerson: StateFlow<PersonEntity?> = _selectedPerson.asStateFlow()

    // Search functionality
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private fun setError(message: String?) {
        _errorMessage.value = message
    }

    private fun clearError() {
        _errorMessage.value = null
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
                personRepository.addPerson(personWithTrip)
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
                personRepository.deletePersonById(personId)
                if (_selectedPerson.value?.personId == personId) {
                    _selectedPerson.value = null
                }
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
                personRepository.updatePerson(person)
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to update person"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectPerson(person: PersonEntity) {
        _selectedPerson.value = person
    }

    fun deselectPerson() {
        _selectedPerson.value = null
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}



