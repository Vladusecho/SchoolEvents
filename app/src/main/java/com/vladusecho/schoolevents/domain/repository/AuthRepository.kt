package com.vladusecho.schoolevents.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun checkUserExists(email: String): Boolean

    suspend fun checkUserPassword(email: String, password: String): Boolean

    suspend fun changeUserIsAuth()

    fun checkUserIsAuth(): Flow<Boolean>
}