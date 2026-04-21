package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.usecase.events.GetEventByIdUseCase
import com.vladusecho.schoolevents.domain.usecase.events.SubscribeToEventUseCase
import com.vladusecho.schoolevents.domain.usecase.events.SwitchEventFavouriteStatusUseCase
import com.vladusecho.schoolevents.domain.usecase.events.UnsubscribeFromEventUseCase
import com.vladusecho.schoolevents.presentation.viewModel.EventDetailsViewModel.EventDetailsState.*
import com.vladusecho.schoolevents.presentation.viewModel.MainViewModel.MainCommand
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
    private val switchFavouriteStatusUseCase: SwitchEventFavouriteStatusUseCase,
    private val subscribeToEventUseCase: SubscribeToEventUseCase,
    private val unsubscribeFromEventUseCase: UnsubscribeFromEventUseCase,
    @Assisted("eventId") eventId: Int
) : ViewModel() {

    private val _state = MutableStateFlow<EventDetailsState>(EventDetailsState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = EventDetailsState.Loading
            val event = getEventByIdUseCase(eventId)
            _state.value = EventDetailsState.Content(event)
        }
    }

    fun processCommand(command: EventDetailsCommand) {
        when(command) {
            is EventDetailsCommand.SwitchFavouriteStatus -> {
                viewModelScope.launch {
                    switchFavouriteStatusUseCase(command.isFavourite, command.eventId)
                    val currentState = _state.value
                    if (currentState is EventDetailsState.Content) {
                        val updatedEvent = currentState.event.copy(
                            isFavourite = !command.isFavourite
                        )
                        _state.value = Content(updatedEvent)
                    }
                }
            }

            is EventDetailsCommand.SubscribeToEvent -> {
                viewModelScope.launch {
                    if (!command.isSubscribed) {
                        subscribeToEventUseCase(command.eventId)
                    } else {
                        unsubscribeFromEventUseCase(command.eventId)
                    }
                    val currentState = _state.value
                    if (currentState is EventDetailsState.Content) {
                        val updatedEvent = currentState.event.copy(
                            isSubscribed = !command.isSubscribed
                        )
                        _state.value = Content(updatedEvent)
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("eventId") eventId: Int
        ): EventDetailsViewModel
    }

    sealed interface EventDetailsState {
        object Initial : EventDetailsState
        object Loading : EventDetailsState
        data class Error(
            val message: String
        ) : EventDetailsState

        data class Content(
            val event: Event
        ) : EventDetailsState
    }

    sealed interface EventDetailsCommand {
        data class SwitchFavouriteStatus(
            val isFavourite: Boolean,
            val eventId: Int
        ) : EventDetailsCommand

        data class SubscribeToEvent(
            val isSubscribed: Boolean,
            val eventId: Int
        ) : EventDetailsCommand
    }
}