package com.vladusecho.schoolevents.domain.repository

import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.Profile
import kotlinx.coroutines.flow.Flow

interface EventsRepository {

    fun getEvents(): Flow<List<Event>>

    fun getSubscribedEvents(): Flow<List<Event>>

    suspend fun subscribeToEvent(eventId: Int)

    suspend fun unsubscribeFromEvent(eventId: Int)

    suspend fun switchFavouriteStatus(isFavourite: Boolean, eventId: Int)

    fun getFavouriteEvents(): Flow<List<Event>>

    suspend fun getEventById(eventId: Int): Event

    fun getConfirmationEvents(): Flow<List<Event>>

    fun getArchivedEvents(): Flow<List<Event>>

    suspend fun addNewEvent(event: Event)

    fun getProfile(): Flow<Profile>

    suspend fun updateProfile(profile: Profile)

    suspend fun saveImageToInternalStorage(uri: String): String

    suspend fun checkUserExists(email: String): Boolean

    suspend fun checkUserPassword(email: String, password: String): Boolean

    fun checkUserIsAuth(): Flow<Boolean>

    suspend fun changeUserIsAuth()
}


