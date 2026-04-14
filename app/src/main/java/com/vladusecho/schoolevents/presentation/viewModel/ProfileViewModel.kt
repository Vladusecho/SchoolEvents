package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.vladusecho.schoolevents.domain.entity.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val state = _state.asStateFlow()

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

data class Profile(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val classNumber: String,
    val role: String,
    val imageUrl: String
)
