package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.usecase.auth.ChangeUserIsAuthUseCase
import com.vladusecho.schoolevents.domain.usecase.events.GetEventsUseCase
import com.vladusecho.schoolevents.domain.usecase.events.GetSubscribedEventsUseCase
import com.vladusecho.schoolevents.domain.usecase.events.SwitchEventFavouriteStatusUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.GetProfileUseCase
import com.vladusecho.schoolevents.presentation.screen.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getSubscribedEventsUseCase: GetSubscribedEventsUseCase,
    private val getEventsUseCase: GetEventsUseCase,
    private val changeUserIsAuthUseCase: ChangeUserIsAuthUseCase,
    private val switchEventFavouriteStatusUseCase: SwitchEventFavouriteStatusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val state = _state.asStateFlow()

    private val _isExit = MutableSharedFlow<Boolean>()
    val isExit = _isExit.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = ProfileState.Loading

            getProfileUseCase().flatMapLatest { profile ->
                if (profile.role == UserRole.STUDENT.label) {
                    getSubscribedEventsUseCase().map { events -> profile to events }
                } else {
                    getEventsUseCase().map { allEvents ->
                        val myEvents = allEvents.filter { it.creatorEmail == profile.email }
                        profile to myEvents
                    }
                }
            }.collect { (profile, events) ->
                _state.value = ProfileState.Content(profile, events)
            }
        }
    }

    fun processCommand(command: ProfileCommand) {
        when (command) {
            is ProfileCommand.Exit -> {
                viewModelScope.launch {
                    _isLoading.value = true
                    changeUserIsAuthUseCase()
                    _isExit.emit(true)
                    _isLoading.value = false
                }
            }

            is ProfileCommand.SwitchFavouriteStatus -> {
                viewModelScope.launch {
                    switchEventFavouriteStatusUseCase(command.isFavourite, command.eventId)
                }
            }
        }
    }

    sealed interface ProfileState {
        object Initial : ProfileState
        object Loading : ProfileState
        data class Error(val message: String) : ProfileState
        data class Content(val profile: Profile, val events: List<Event>) : ProfileState
    }

    sealed interface ProfileCommand {
        object Exit : ProfileCommand
        data class SwitchFavouriteStatus(val isFavourite: Boolean, val eventId: Int) :
            ProfileCommand
    }
}
