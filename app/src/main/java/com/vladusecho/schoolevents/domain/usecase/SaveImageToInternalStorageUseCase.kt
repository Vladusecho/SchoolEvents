package com.vladusecho.schoolevents.domain.usecase

import com.vladusecho.schoolevents.domain.repository.EventsRepository
import javax.inject.Inject

class SaveImageToInternalStorageUseCase @Inject constructor(
    private val repository: EventsRepository
){
    suspend operator fun invoke(uri: String) = repository.saveImageToInternalStorage(uri)
}