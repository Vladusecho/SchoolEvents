package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.usecase.events.AddNewEventUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventCreationViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val addNewEventUseCase: AddNewEventUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<EventCreationState>(EventCreationState.Initial)
    val state = _state.asStateFlow()

    init {
        loadOrganizerProfile()
    }

    private fun loadOrganizerProfile() {
        viewModelScope.launch {
            try {
                val profile = getProfileUseCase().first()
                _state.value = EventCreationState.Content(
                    organizerName = "${profile.name} ${profile.surname}"
                )
            } catch (e: Exception) {
                _state.value = EventCreationState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun createEvent(event: Event) {
        viewModelScope.launch {
            addNewEventUseCase(event)
            _state.value = EventCreationState.Saved
        }
    }

    sealed interface EventCreationState {
        object Initial : EventCreationState
        data class Content(val organizerName: String) : EventCreationState
        data class Error(val message: String) : EventCreationState
        object Saved : EventCreationState
    }
}
