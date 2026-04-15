package com.vladusecho.schoolevents.domain.usecase

import com.vladusecho.schoolevents.domain.repository.EventsRepository
import javax.inject.Inject

class GetSubscribedEventsUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) {
    operator fun invoke() = eventsRepository.getSubscribedEvents()
}
