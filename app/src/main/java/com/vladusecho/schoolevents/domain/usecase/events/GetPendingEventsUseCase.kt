package com.vladusecho.schoolevents.domain.usecase.events

import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPendingEventsUseCase @Inject constructor(
    private val repository: EventsRepository
) {
    operator fun invoke(): Flow<List<Event>> {
        return repository.getPendingEvents()
    }
}
