package com.vladusecho.schoolevents.data.repository

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vladusecho.schoolevents.data.local.EventsAppDao
import com.vladusecho.schoolevents.data.mapper.toProfileEntityFlow
import com.vladusecho.schoolevents.data.mapper.toProfileModel
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.repository.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

val Context.dataStoreSettings by preferencesDataStore("settings")

class ProfileRepositoryImpl @Inject constructor(
    private val dao: EventsAppDao,
    @param:ApplicationContext private val context: Context
) : ProfileRepository {

    private val currentUserEmailKey = stringPreferencesKey("current_user_email")

    override suspend fun saveProfile(profile: Profile) {
        context.dataStoreSettings.edit { preferences ->
            preferences[currentUserEmailKey] = profile.email
        }
        Log.d("tag", "saveProfile: $profile")
        dao.saveProfile(profile.toProfileModel())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getProfile(): Flow<Profile> {
        return context.dataStoreSettings.data.flatMapLatest { preferences ->
            val email = preferences[currentUserEmailKey] ?: ""
            dao.getProfile(email).toProfileEntityFlow()
        }
    }

    override suspend fun saveImageToInternalStorage(uri: String): String {
        return withContext(Dispatchers.IO) {
            val uri = uri.toUri()
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "avatar_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)

            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        }
    }
}
