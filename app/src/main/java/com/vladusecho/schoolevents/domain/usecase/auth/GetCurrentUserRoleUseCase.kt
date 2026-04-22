package com.vladusecho.schoolevents.domain.usecase.auth

import com.vladusecho.schoolevents.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserRoleUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    operator fun invoke() = repository.getCurrentUserRole()
}