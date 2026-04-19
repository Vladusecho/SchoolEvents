package com.vladusecho.schoolevents.data.repository

import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
) : EventsRepository {
    override fun getEvents(): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

    override fun getSubscribedEvents(): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeToEvent(eventId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun unsubscribeFromEvent(eventId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun switchFavouriteStatus(isFavourite: Boolean, eventId: Int) {
        TODO("Not yet implemented")
    }

    override fun getFavouriteEvents(): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getEventById(eventId: Int): Event {
        TODO("Not yet implemented")
    }

    override fun getConfirmationEvents(): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

    override fun getArchivedEvents(): Flow<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun addNewEvent(event: Event) {
        TODO("Not yet implemented")
    }

    override fun getProfile(): Flow<Profile> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfile(profile: Profile) {
        TODO("Not yet implemented")
    }

    override suspend fun saveImageToInternalStorage(uri: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun checkUserExists(email: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun checkUserPassword(
        email: String,
        password: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun checkUserIsAuth(): Flow<Boolean>{
        TODO("Not yet implemented")
    }

    override suspend fun changeUserIsAuth() {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(profile: Profile): Boolean {
        TODO("Not yet implemented")
    }
}