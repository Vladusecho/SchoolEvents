package com.vladusecho.schoolevents.domain.usecase.profile

import com.vladusecho.schoolevents.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke() = profileRepository.getProfile()
}