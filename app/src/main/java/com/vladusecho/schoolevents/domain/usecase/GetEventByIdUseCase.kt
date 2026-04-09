package com.vladusecho.schoolevents.domain.usecase

import com.vladusecho.schoolevents.domain.repository.EventsRepository
import javax.inject.Inject

class GetEventByIdUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) {

    suspend operator fun invoke(eventId: Int) = eventsRepository.getEventById(eventId)
}
