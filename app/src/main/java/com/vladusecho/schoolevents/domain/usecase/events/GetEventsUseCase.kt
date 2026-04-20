package com.vladusecho.schoolevents.domain.usecase.events

import com.vladusecho.schoolevents.domain.repository.EventsRepository
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) {

    operator fun invoke() = eventsRepository.getEvents()
}