package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.usecase.GetEventByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = EventDetailsViewModel.Factory::class
)
class EventDetailsViewModel @AssistedInject constructor(
    private val getEventByIdUseCase: GetEventByIdUseCase,
    @Assisted("eventId") eventId: Int
) : ViewModel() {

    private val _state = MutableStateFlow<EventsDetailsState>(EventsDetailsState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = EventsDetailsState.Loading
            val event = getEventByIdUseCase(eventId)
            _state.value = EventsDetailsState.Content(event)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("eventId") eventId: Int
        ): EventDetailsViewModel
    }

    sealed interface EventsDetailsState {
        object Initial : EventsDetailsState
        object Loading : EventsDetailsState
        data class Error(
            val message: String
        ) : EventsDetailsState

        data class Content(
            val event: Event
        ) : EventsDetailsState
    }
}