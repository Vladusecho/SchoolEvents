package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.usecase.events.ApproveEventUseCase
import com.vladusecho.schoolevents.domain.usecase.events.DeleteEventUseCase
import com.vladusecho.schoolevents.domain.usecase.events.GetEventByIdUseCase
import com.vladusecho.schoolevents.domain.usecase.events.RejectEventUseCase
import com.vladusecho.schoolevents.domain.usecase.events.SubscribeToEventUseCase
import com.vladusecho.schoolevents.domain.usecase.events.SwitchEventFavouriteStatusUseCase
import com.vladusecho.schoolevents.domain.usecase.events.UnsubscribeFromEventUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.GetProfileByEmailUseCase
import com.vladusecho.schoolevents.presentation.viewModel.EventDetailsViewModel.EventDetailsState.*
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
    private val getProfileByEmailUseCase: GetProfileByEmailUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val approveEventUseCase: ApproveEventUseCase,
    private val rejectEventUseCase: RejectEventUseCase,
    @Assisted("eventId") private val eventId: Int
) : ViewModel() {

    private val _state = MutableStateFlow<EventDetailsState>(EventDetailsState.Initial)
    val state = _state.asStateFlow()

    init {
        loadEvent()
    }

    private fun loadEvent() {
        viewModelScope.launch {
            _state.value = EventDetailsState.Loading
            try {
                val event = getEventByIdUseCase(eventId)
                val organizerName = try {
                    val profile = getProfileByEmailUseCase(event.creatorEmail)
                    "${profile.name} ${profile.surname}"
                } catch (e: Exception) {
                    "Организатор не указан"
                }
                _state.value = EventDetailsState.Content(event, organizerName)
            } catch (e: Exception) {
                _state.value = EventDetailsState.Error(e.message ?: "Unknown error")
            }
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
                        _state.value = currentState.copy(event = updatedEvent)
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
                        _state.value = currentState.copy(event = updatedEvent)
                    }
                }
            }

            is EventDetailsCommand.DeleteEvent -> {
                viewModelScope.launch {
                    deleteEventUseCase(eventId)
                    _state.value = EventDetailsState.Deleted
                }
            }

            is EventDetailsCommand.ApproveEvent -> {
                viewModelScope.launch {
                    approveEventUseCase(eventId)
                    _state.value = EventDetailsState.Deleted // Nav back after action
                }
            }

            is EventDetailsCommand.RejectEvent -> {
                viewModelScope.launch {
                    rejectEventUseCase(eventId)
                    _state.value = EventDetailsState.Deleted // Nav back after action
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
            val event: Event,
            val organizerName: String
        ) : EventDetailsState

        object Deleted : EventDetailsState
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

        object DeleteEvent : EventDetailsCommand
        object ApproveEvent : EventDetailsCommand
        object RejectEvent : EventDetailsCommand
    }
}
