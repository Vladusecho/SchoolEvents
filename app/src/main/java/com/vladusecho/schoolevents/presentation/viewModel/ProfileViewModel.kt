package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.usecase.auth.ChangeUserIsAuthUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.GetCurrentUserRoleUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.SetCurrentUserRoleUseCase
import com.vladusecho.schoolevents.domain.usecase.events.GetSubscribedEventsUseCase
import com.vladusecho.schoolevents.domain.usecase.events.SwitchEventFavouriteStatusUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getSubscribedEventsUseCase: GetSubscribedEventsUseCase,
    private val switchFavouriteStatusUseCase: SwitchEventFavouriteStatusUseCase,
    private val changeUserIsAuthUseCase: ChangeUserIsAuthUseCase,
    private val getCurrentUserRoleUseCase: GetCurrentUserRoleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val state = _state.asStateFlow()

    val isLoading = MutableStateFlow<Boolean>(false)
    val isExit = MutableSharedFlow<Boolean>()

    fun processCommand(command: ProfileCommand) {
        when (command) {
            is ProfileCommand.SwitchFavouriteStatus -> {
                viewModelScope.launch {
                    switchFavouriteStatusUseCase(command.isFavourite, command.eventId)
                }
            }

            ProfileCommand.Exit -> {
                viewModelScope.launch {
                    isLoading.value = true
                    delay(2000)
                    changeUserIsAuthUseCase()
                    isExit.emit(true)
                    delay(1000)
                    isLoading.value = false
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            delay(1000)
            combine(
                getProfileUseCase(),
                getSubscribedEventsUseCase()
            ) { profile, events ->
                ProfileState.Content(
                    events = events,
                    profile = profile
                )
            }.collect {
                _state.value = it
            }
        }
    }

    sealed interface ProfileState {

        object Initial : ProfileState

        object Loading : ProfileState

        data class Error(
            val message: String
        ) : ProfileState

        data class Content(
            val events: List<Event>,
            val profile: Profile
        ) : ProfileState

    }

    sealed interface ProfileCommand {
        data class SwitchFavouriteStatus(
            val isFavourite: Boolean,
            val eventId: Int
        ) : ProfileCommand

        object Exit : ProfileCommand
    }
}
