package com.vladusecho.schoolevents.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.vladusecho.schoolevents.data.local.EventsAppDao
import com.vladusecho.schoolevents.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStoreAuth by preferencesDataStore("auth")

class AuthRepositoryImpl @Inject constructor(
    private val dao: EventsAppDao,
    @param:ApplicationContext private val context: Context
) : AuthRepository {

    private val currentUserAuthKey = booleanPreferencesKey("current_user_auth")

    override suspend fun checkUserExists(email: String): Boolean {
        return dao.checkUserExists(email)
    }

    override suspend fun checkUserPassword(
        email: String,
        password: String
    ): Boolean {
        return dao.checkUserPassword(email, password)
    }

    override suspend fun changeUserIsAuth() {
        context.dataStoreAuth.edit { preferences ->
            val current = preferences[currentUserAuthKey] ?: false
            preferences[currentUserAuthKey] = !current
        }
    }

    override fun checkUserIsAuth(): Flow<Boolean> {
        return context.dataStoreAuth.data.map { preferences ->
            preferences[currentUserAuthKey] ?: false
        }
    }
}