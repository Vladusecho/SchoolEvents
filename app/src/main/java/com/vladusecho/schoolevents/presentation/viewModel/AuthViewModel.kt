package com.vladusecho.schoolevents.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.repository.AuthRepository
import com.vladusecho.schoolevents.domain.usecase.auth.ChangeUserIsAuthUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.CheckUserExistsUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.CheckUserIsAuthUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.CheckUserPasswordUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.GetCurrentUserRoleUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.SetCurrentUserRoleUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.GetProfileByEmailUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.SaveProfileUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.SetCurrentUserEmailUseCase
import com.vladusecho.schoolevents.presentation.util.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val checkUserExistsUseCase: CheckUserExistsUseCase,
    private val checkUserPasswordUseCase: CheckUserPasswordUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val checkUserIsAuthUseCase: CheckUserIsAuthUseCase,
    private val changeUserIsAuthUseCase: ChangeUserIsAuthUseCase,
    private val setCurrentUserRoleUseCase: SetCurrentUserRoleUseCase,
    private val getCurrentUserRoleUseCase: GetCurrentUserRoleUseCase,
    private val setCurrentUserEmailUseCase: SetCurrentUserEmailUseCase,
    private val getProfileByEmailUseCase: GetProfileByEmailUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AuthNavigationTarget>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _authResult = MutableSharedFlow<Boolean>()
    val authResult = _authResult.asSharedFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError = _passwordError.asStateFlow()

    val isAuth: StateFlow<Boolean?> = checkUserIsAuthUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val userRole: StateFlow<UserRole> = getCurrentUserRoleUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UserRole.STUDENT
        )

    val isDarkTheme: StateFlow<Boolean?> = authRepository.isDarkTheme()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun toggleTheme() {
        viewModelScope.launch {
            val current = isDarkTheme.value ?: false
            authRepository.setDarkTheme(!current)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _emailError.value = null
            _passwordError.value = null
            try {
                val exists = checkUserExistsUseCase(email)
                if (!exists) {
                    _emailError.value = "Пользователь не найден"
                    _authResult.emit(false)
                    return@launch
                }

                val result = checkUserPasswordUseCase(email, password)
                if (result) {
                    setCurrentUserEmailUseCase(email)
                    val profile = getProfileByEmailUseCase(email)
                    val role =
                        UserRole.entries.find { it.label == profile.role } ?: UserRole.STUDENT
                    setCurrentUserRoleUseCase(role)
                    changeUserIsAuthUseCase()
                    _authResult.emit(true)
                } else {
                    _passwordError.value = "Неверный пароль"
                    _authResult.emit(false)
                }
            } catch (e: Exception) {
                Log.e("tag", "login: ", e)
                _authResult.emit(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrors() {
        _emailError.value = null
        _passwordError.value = null
    }

    fun checkPassword(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = checkUserPasswordUseCase(email, password)
                if (result) {
                    // 1. Сначала жестко ставим новый email
                    setCurrentUserEmailUseCase(email)
                    // 2. Получаем профиль именно для этого email
                    val profile = getProfileByEmailUseCase(email)
                    val role =
                        UserRole.entries.find { it.label == profile.role } ?: UserRole.STUDENT
                    // 3. Сохраняем роль
                    setCurrentUserRoleUseCase(role)
                    // 4. И только в самом конце — авторизация
                    changeUserIsAuthUseCase()
                }
                _authResult.emit(result)
            } catch (e: Exception) {
                Log.e("tag", "check password: ", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun registerUser(profile: Profile, code: String? = "0000") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                code?.let { code ->
                    if (profile.role != UserRole.STUDENT.label && code != "1991") {
                        _authResult.emit(false)
                    } else {
                        saveProfileUseCase(profile)
                        setCurrentUserEmailUseCase(profile.email)
                        val role =
                            UserRole.entries.find { it.label == profile.role } ?: UserRole.STUDENT
                        setCurrentUserRoleUseCase(role)
                        changeUserIsAuthUseCase()
                        _authResult.emit(true)
                    }
                }
            } catch (e: Exception) {
                Log.e("tag", "registerUser: ", e)
            } finally {
                _isLoading.value = false
            }
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
                Log.e("tag", "checkEmail: ", e)
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
