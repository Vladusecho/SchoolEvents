package com.vladusecho.schoolevents.domain.usecase

import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import javax.inject.Inject

class AddNewEventUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) {

    suspend operator fun invoke(event: Event) = eventsRepository.addNewEvent(event)
}
