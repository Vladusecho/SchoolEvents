package com.vladusecho.schoolevents.domain.usecase.profile

import com.vladusecho.schoolevents.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileByEmailUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(email: String) = profileRepository.getProfileByEmail(email)
}
