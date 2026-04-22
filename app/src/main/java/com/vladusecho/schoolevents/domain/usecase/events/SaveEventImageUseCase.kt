package com.vladusecho.schoolevents.domain.usecase.events

import com.vladusecho.schoolevents.domain.repository.EventsRepository
import javax.inject.Inject

class SaveEventImageUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) {
    suspend operator fun invoke(uri: String): String = eventsRepository.saveImageToInternalStorage(uri)
}
