package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.usecase.events.GetParticipantsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = ParticipantsViewModel.Factory::class
)
class ParticipantsViewModel @AssistedInject constructor(
    private val getParticipantsUseCase: GetParticipantsUseCase,
    @Assisted("eventId") private val eventId: Int
) : ViewModel() {

    private val _state = MutableStateFlow<ParticipantsState>(ParticipantsState.Initial)
    val state = _state.asStateFlow()

    init {
        loadParticipants()
    }

    private fun loadParticipants() {
        viewModelScope.launch {
            _state.value = ParticipantsState.Loading
            try {
                getParticipantsUseCase(eventId).collect { participants ->
                    _state.value = ParticipantsState.Content(participants)
                }
            } catch (e: Exception) {
                _state.value = ParticipantsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("eventId") eventId: Int
        ): ParticipantsViewModel
    }

    sealed interface ParticipantsState {
        object Initial : ParticipantsState
        object Loading : ParticipantsState
        data class Error(val message: String) : ParticipantsState
        data class Content(val participants: List<Profile>) : ParticipantsState
    }
}
