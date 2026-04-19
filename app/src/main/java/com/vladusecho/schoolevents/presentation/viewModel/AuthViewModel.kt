package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.usecase.ChangeUserIsAuthUseCase
import com.vladusecho.schoolevents.domain.usecase.CheckUserExistsUseCase
import com.vladusecho.schoolevents.domain.usecase.CheckUserIsAuthUseCase
import com.vladusecho.schoolevents.domain.usecase.CheckUserPasswordUseCase
import com.vladusecho.schoolevents.domain.usecase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val checkUserExistsUseCase: CheckUserExistsUseCase,
    private val checkUserPasswordUseCase: CheckUserPasswordUseCase,
    private val changeUserIsAuthUseCase: ChangeUserIsAuthUseCase,
    private val checkUserIsAuthUseCase: CheckUserIsAuthUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AuthNavigationTarget>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _authResult = MutableSharedFlow<Boolean>()
    val authResult = _authResult.asSharedFlow()

    private val _isAuth = MutableStateFlow(false)
    val isAuth = _isAuth.asStateFlow()

    init {
        viewModelScope.launch {
            checkUserIsAuthUseCase().collect { isAuth ->
                _isAuth.value = isAuth
            }
        }
    }

    fun changeUserIsAuth() {
        viewModelScope.launch {
            changeUserIsAuthUseCase()
        }
    }

    fun checkPassword(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000)
            try {
                val result = checkUserPasswordUseCase(email, password)
                _authResult.emit(result)
                _isLoading.value = false
            } catch (e: Exception) {
                // Обработка ошибки (например, через еще один StateFlow)
            }
        }
    }

    fun registerUser(profile: Profile) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000)
            try {
                val result = registerUserUseCase(profile)
                _authResult.emit(result)
                _isLoading.value = false
            } catch (e: Exception) {
                // Обработка ошибки (например, через еще один StateFlow)
            }
        }
    }

    fun checkEmail(email: String) {
        if (email.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            delay(2000)

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