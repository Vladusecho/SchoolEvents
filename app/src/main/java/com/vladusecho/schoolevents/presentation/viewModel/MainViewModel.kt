package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.usecase.GetEventsUseCase
import com.vladusecho.schoolevents.domain.usecase.SwitchEventFavouriteStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val switchFavouriteStatusUseCase: SwitchEventFavouriteStatusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Initial)
    val state = _state.asStateFlow()


    fun processCommand(command: MainCommand) {
        when(command) {
            is MainCommand.SwitchFavouriteStatus -> {
                viewModelScope.launch {
                    switchFavouriteStatusUseCase(command.isFavourite, command.eventId)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            _state.value = MainState.Loading
            delay(1000)
            getEventsUseCase().collect { events ->
                _state.value = MainState.Content(events)
            }
        }
    }

    sealed interface MainState {

        object Initial : MainState

        object Loading : MainState

        data class Error(
            val message: String
        ) : MainState

        data class Content(
            val events: List<Event>
        ) : MainState
    }

    sealed interface MainCommand {

        data class SwitchFavouriteStatus(
            val isFavourite: Boolean,
            val eventId: Int
        ) : MainCommand
    }
}