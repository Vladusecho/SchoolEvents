package com.vladusecho.schoolevents.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.usecase.auth.ChangeUserIsAuthUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.CheckUserExistsUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.CheckUserIsAuthUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.CheckUserPasswordUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.GetCurrentUserRoleUseCase
import com.vladusecho.schoolevents.domain.usecase.auth.SetCurrentUserRoleUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.GetProfileByEmailUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.SaveProfileUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.SetCurrentUserEmailUseCase
import com.vladusecho.schoolevents.presentation.screen.UserRole
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
    private val changeUserIsAuthUseCase: ChangeUserIsAuthUseCase,
    private val setCurrentUserRoleUseCase: SetCurrentUserRoleUseCase,
    private val getCurrentUserRoleUseCase: GetCurrentUserRoleUseCase,
    private val setCurrentUserEmailUseCase: SetCurrentUserEmailUseCase,
    private val getProfileByEmailUseCase: GetProfileByEmailUseCase
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

    val userRole: StateFlow<UserRole> = getCurrentUserRoleUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UserRole.STUDENT
        )

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
                    val role = UserRole.entries.find { it.label == profile.role } ?: UserRole.STUDENT
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

    fun registerUser(profile: Profile) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                saveProfileUseCase(profile)
                setCurrentUserEmailUseCase(profile.email)
                val role = UserRole.entries.find { it.label == profile.role } ?: UserRole.STUDENT
                setCurrentUserRoleUseCase(role)
                changeUserIsAuthUseCase()
                _authResult.emit(true)
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
