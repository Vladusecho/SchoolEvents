package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.usecase.events.GetFavouriteEventsUseCase
import com.vladusecho.schoolevents.domain.usecase.events.SwitchEventFavouriteStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val getFavouriteEventsUseCase: GetFavouriteEventsUseCase,
    private val switchFavouriteStatusUseCase: SwitchEventFavouriteStatusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<FavouriteState>(FavouriteState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = FavouriteState.Loading
            delay(1000)
            getFavouriteEventsUseCase().collect { events ->
                _state.value = FavouriteState.Content(events)
            }
        }
    }

    fun processCommand(command: FavouriteCommand) {
        when(command) {
            is FavouriteCommand.SwitchFavouriteStatus -> {
                viewModelScope.launch {
                    switchFavouriteStatusUseCase(command.isFavourite, command.eventId)
                }
            }
        }
    }

    sealed interface FavouriteState {

        object Initial : FavouriteState

        object Loading : FavouriteState

        data class Error(
            val message: String
        ) : FavouriteState

        data class Content(
            val events: List<Event>
        ) : FavouriteState

    }


    sealed interface FavouriteCommand {

        data class SwitchFavouriteStatus(
            val isFavourite: Boolean,
            val eventId: Int
        ) : FavouriteCommand
    }
}