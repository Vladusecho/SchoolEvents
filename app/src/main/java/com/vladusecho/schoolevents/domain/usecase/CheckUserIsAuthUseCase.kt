package com.vladusecho.schoolevents.domain.usecase

import com.vladusecho.schoolevents.domain.repository.EventsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckUserIsAuthUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return eventsRepository.checkUserIsAuth()
    }
}
