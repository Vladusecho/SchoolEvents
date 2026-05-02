package com.vladusecho.schoolevents.domain.usecase.profile

import com.vladusecho.schoolevents.domain.repository.ProfileRepository
import javax.inject.Inject

class SetCurrentUserEmailUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(email: String) = profileRepository.setCurrentUserEmail(email)
}
