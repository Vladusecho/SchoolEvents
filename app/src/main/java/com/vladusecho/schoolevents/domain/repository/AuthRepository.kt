package com.vladusecho.schoolevents.domain.repository

import com.vladusecho.schoolevents.presentation.screen.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun checkUserExists(email: String): Boolean

    suspend fun checkUserPassword(email: String, password: String): Boolean

    suspend fun changeUserIsAuth()

    fun checkUserIsAuth(): Flow<Boolean>

    suspend fun setCurrentUserRole(role: UserRole)

    fun getCurrentUserRole(): Flow<UserRole>
}