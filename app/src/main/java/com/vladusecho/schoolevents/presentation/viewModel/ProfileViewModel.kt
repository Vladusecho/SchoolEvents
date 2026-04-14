package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.usecase.GetEventsUseCase
import com.vladusecho.schoolevents.domain.usecase.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getEventsUseCase: GetEventsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            delay(1000)
            val events = getEventsUseCase()
            getProfileUseCase().collect { profile ->
                _state.value = ProfileState.Content(
                    events = events.first(),
                    profile = profile
                )
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
}
