package com.vladusecho.schoolevents.domain.usecase.profile

import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.repository.ProfileRepository
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(profile: Profile) {
        profileRepository.saveProfile(profile)
    }
}