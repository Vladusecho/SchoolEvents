package com.vladusecho.schoolevents.domain.usecase

import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) {

    suspend operator fun invoke(profile: Profile) {
        eventsRepository.updateProfile(profile)
    }
}