package com.vladusecho.schoolevents.domain.usecase.profile

import com.vladusecho.schoolevents.domain.repository.ProfileRepository
import javax.inject.Inject

class SaveImageToInternalStorageUseCase @Inject constructor(
    private val repository: ProfileRepository
){
    suspend operator fun invoke(uri: String) = repository.saveImageToInternalStorage(uri)
}