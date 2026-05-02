package com.vladusecho.schoolevents.domain.usecase.events

import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetParticipantsUseCase @Inject constructor(
    private val repository: EventsRepository
) {
    operator fun invoke(eventId: Int): Flow<List<Profile>> {
        return repository.getParticipants(eventId)
    }
}
