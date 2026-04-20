package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.usecase.profile.GetProfileUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.SaveImageToInternalStorageUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.SaveProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditingProfileViewModel @Inject constructor(
    private val saveProfileUseCase: SaveProfileUseCase,
    private val saveImageToInternalStorageUseCase: SaveImageToInternalStorageUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<EditingProfileState>(EditingProfileState.Initial)
    val state = _state.asStateFlow()


    fun processCommand(command: EditingProfileCommand) {
        when (command) {
            is EditingProfileCommand.SaveProfile -> {
                viewModelScope.launch {
                    val finalPath = if (command.profile.imageUrl.startsWith("content://")) {
                        saveImageToInternalStorageUseCase(command.profile.imageUrl)
                    } else {
                        command.profile.imageUrl
                    }
                    saveProfileUseCase(command.profile.copy(imageUrl = finalPath))
                }
            }
        }
    }


    sealed interface EditingProfileState {
        object Initial : EditingProfileState
        object Loading : EditingProfileState
        data class Content(
            val profile: Profile
        ) : EditingProfileState

        object Error : EditingProfileState
    }

    sealed interface EditingProfileCommand {
        data class SaveProfile(
            val profile: Profile
        ) : EditingProfileCommand
    }
}