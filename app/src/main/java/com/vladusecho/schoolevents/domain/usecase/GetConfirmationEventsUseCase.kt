package com.vladusecho.schoolevents.domain.usecase

import com.vladusecho.schoolevents.domain.repository.EventsRepository
import javax.inject.Inject

class GetConfirmationEventsUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) {

    operator fun invoke() = eventsRepository.getConfirmationEvents()
}
