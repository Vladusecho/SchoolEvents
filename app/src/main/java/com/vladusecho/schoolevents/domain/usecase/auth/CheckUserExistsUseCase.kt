package com.vladusecho.schoolevents.domain.usecase.auth

import com.vladusecho.schoolevents.domain.repository.AuthRepository
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import javax.inject.Inject

class CheckUserExistsUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Boolean {
        return authRepository.checkUserExists(email)
    }
}