package com.vladusecho.schoolevents.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.usecase.auth.ChangeUserIsAuthUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.CheckUserExistsUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.CheckUserIsAuthUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.CheckUserPasswordUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.SaveProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val checkUserExistsUseCase: CheckUserExistsUseCase,
    private val checkUserPasswordUseCase: CheckUserPasswordUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val checkUserIsAuthUseCase: CheckUserIsAuthUseCase,
    private val changeUserIsAuthUseCase: ChangeUserIsAuthUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AuthNavigationTarget>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _authResult = MutableSharedFlow<Boolean>()
    val authResult = _authResult.asSharedFlow()

    val isAuth: StateFlow<Boolean?> = checkUserIsAuthUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun checkPassword(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = checkUserPasswordUseCase(email, password)
                _authResult.emit(result)
                if (result) {
                    changeUserIsAuthUseCase()
                }
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("tag", "check password: ", e)
            }
        }
    }

    fun registerUser(profile: Profile) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                saveProfileUseCase(profile)
                changeUserIsAuthUseCase()
                _authResult.emit(true)
            } catch (e: Exception) {
                Log.e("tag", "registerUser: ", e)
            }

            _isLoading.value = false
        }
    }

    fun checkEmail(email: String) {
        if (email.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val exists = checkUserExistsUseCase(email)
                if (exists) {
                    _navigationEvent.emit(AuthNavigationTarget.ToLogin(email))
                } else {
                    _navigationEvent.emit(AuthNavigationTarget.ToRegistration(email))
                }
            } catch (e: Exception) {
                // Обработка ошибки (например, через еще один StateFlow)
            } finally {
                _isLoading.value = false
            }
        }
    }
}

sealed class AuthNavigationTarget {
    data class ToLogin(val email: String) : AuthNavigationTarget()
    data class ToRegistration(val email: String) : AuthNavigationTarget()
}