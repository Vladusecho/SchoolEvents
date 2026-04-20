package com.vladusecho.schoolevents.domain.repository

import com.vladusecho.schoolevents.domain.entity.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun saveProfile(profile: Profile)

    fun getProfile(): Flow<Profile>

    suspend fun saveImageToInternalStorage(uri: String): String
}