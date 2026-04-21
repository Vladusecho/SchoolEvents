package com.vladusecho.schoolevents.domain.usecase.events

import com.vladusecho.schoolevents.domain.repository.EventsRepository
import javax.inject.Inject

class SwitchEventFavouriteStatusUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) {

    suspend operator fun invoke(
        isFavourite: Boolean, eventId: Int
    ) = eventsRepository.switchFavouriteStatus(isFavourite, eventId)
}