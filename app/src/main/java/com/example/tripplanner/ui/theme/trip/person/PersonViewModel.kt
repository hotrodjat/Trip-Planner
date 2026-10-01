package com.example.tripplanner.ui.theme.trip.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripplanner.data.entity.PersonEntity
import com.example.tripplanner.data.entity.TripParticipantWithPerson
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
    val people: StateFlow<List<TripParticipantWithPerson>> = tripViewModel.people

    private val _availablePeople = MutableStateFlow<List<PersonEntity>>(emptyList())
    val availablePeople: StateFlow<List<PersonEntity>> = _availablePeople.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedPerson = MutableStateFlow<TripParticipantWithPerson?>(null)
    val selectedPerson: StateFlow<TripParticipantWithPerson?> = _selectedPerson.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private fun setError(message: String?) {
        _errorMessage.value = message
    }

    private fun clearError() {
        _errorMessage.value = null
    }

    fun loadAvailablePeople() {
        viewModelScope.launch {
            try {
                val peopleNotInTrip = personRepository.getPeopleAvailableForTrip(tripId)
                _availablePeople.value = peopleNotInTrip
            } catch (e: Exception) {
                setError(e.message ?: "Failed to load people")
            }
        }
    }

    fun addPerson(person: PersonEntity, personalBudget: Int = 0, notes: String? = null) {
        if (person.firstName.isBlank()) {
            setError("First name cannot be empty")
            return
        }

        if (person.lastName.isBlank()) {
            setError("Last name cannot be empty")
            return
        }

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                personRepository.addPersonToTrip(tripId, person, personalBudget, notes)
                loadAvailablePeople()
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to add person"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addExistingPerson(personId: Long, personalBudget: Int = 0, notes: String? = null) {
        if (personId <= 0) {
            setError("Invalid person ID")
            return
        }

        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                personRepository.addExistingPersonToTrip(tripId, personId, personalBudget, notes)
                loadAvailablePeople()
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to add existing person"
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
                personRepository.deletePersonFromTrip(tripId, personId)
                if (_selectedPerson.value?.personId == personId) {
                    _selectedPerson.value = null
                }
                loadAvailablePeople()
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to delete person"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updatePerson(person: PersonEntity, personalBudget: Int = 0, notes: String? = null) {
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
//                personRepository.updatePerson(person)
                personRepository.updateParticipantDetails(tripId, person.personId, personalBudget, notes)
                loadAvailablePeople()
            } catch (e: Exception) {
                val errorMsg = e.message ?: "Failed to update person"
                setError(errorMsg)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectPerson(person: TripParticipantWithPerson) {
        _selectedPerson.value = person
    }

    fun deselectPerson() {
        _selectedPerson.value = null
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}



