package com.vladusecho.schoolevents.domain.usecase.auth

import com.vladusecho.schoolevents.domain.repository.AuthRepository
import com.vladusecho.schoolevents.presentation.screen.UserRole
import javax.inject.Inject

class SetCurrentUserRoleUseCase @Inject constructor(
    private val repository: AuthRepository
){

    suspend operator fun invoke(userRole: UserRole) {
        repository.setCurrentUserRole(userRole)
    }
}