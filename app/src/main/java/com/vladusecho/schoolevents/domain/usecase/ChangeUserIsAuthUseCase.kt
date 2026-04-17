package com.vladusecho.schoolevents.domain.usecase

import com.vladusecho.schoolevents.domain.repository.EventsRepository
import javax.inject.Inject

class ChangeUserIsAuthUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) {
    suspend operator fun invoke() {
        eventsRepository.changeUserIsAuth()
    }
}