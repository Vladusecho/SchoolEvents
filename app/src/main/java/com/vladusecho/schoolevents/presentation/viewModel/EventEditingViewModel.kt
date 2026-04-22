package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.usecase.events.GetEventByIdUseCase
import com.vladusecho.schoolevents.domain.usecase.events.SaveEventImageUseCase
import com.vladusecho.schoolevents.domain.usecase.events.UpdateEventUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = EventEditingViewModel.Factory::class
)
class EventEditingViewModel @AssistedInject constructor(
    private val getEventByIdUseCase: GetEventByIdUseCase,
    private val updateEventUseCase: UpdateEventUseCase,
    private val saveEventImageUseCase: SaveEventImageUseCase,
    @Assisted("eventId") private val eventId: Int
) : ViewModel() {

    private val _state = MutableStateFlow<EventEditingState>(EventEditingState.Initial)
    val state = _state.asStateFlow()

    init {
        loadEvent()
    }

    private fun loadEvent() {
        viewModelScope.launch {
            _state.value = EventEditingState.Loading
            try {
                val event = getEventByIdUseCase(eventId)
                _state.value = EventEditingState.Content(event)
            } catch (e: Exception) {
                _state.value = EventEditingState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateEvent(event: Event, imageUri: String?) {
        viewModelScope.launch {
            val finalImageUrl = imageUri?.let { saveEventImageUseCase(it) } ?: event.imageUrl
            updateEventUseCase(event.copy(imageUrl = finalImageUrl))
            _state.value = EventEditingState.Saved
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("eventId") eventId: Int
        ): EventEditingViewModel
    }

    sealed interface EventEditingState {
        object Initial : EventEditingState
        object Loading : EventEditingState
        data class Content(val event: Event) : EventEditingState
        data class Error(val message: String) : EventEditingState
        object Saved : EventEditingState
    }
}
