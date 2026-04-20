package com.vladusecho.schoolevents.domain.usecase.auth

import com.vladusecho.schoolevents.domain.repository.AuthRepository
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckUserIsAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return authRepository.checkUserIsAuth()
    }
}