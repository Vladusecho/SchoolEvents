package com.vladusecho.schoolevents.domain.usecase.events

import com.vladusecho.schoolevents.domain.repository.EventsRepository
import javax.inject.Inject

class ApproveEventUseCase @Inject constructor(
    private val repository: EventsRepository
) {
    suspend operator fun invoke(eventId: Int) {
        repository.approveEvent(eventId)
    }
}
