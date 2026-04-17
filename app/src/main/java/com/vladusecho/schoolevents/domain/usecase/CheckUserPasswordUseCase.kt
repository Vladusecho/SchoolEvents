package com.vladusecho.schoolevents.domain.usecase

import com.vladusecho.schoolevents.domain.repository.EventsRepository
import javax.inject.Inject

class CheckUserPasswordUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) {
    suspend operator fun invoke(email: String, password: String): Boolean {
        return eventsRepository.checkUserPassword(email, password)
    }
}