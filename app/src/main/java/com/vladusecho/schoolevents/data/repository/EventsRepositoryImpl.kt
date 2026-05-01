package com.vladusecho.schoolevents.data.repository

import android.content.Context
import androidx.core.net.toUri
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vladusecho.schoolevents.data.local.EventsAppDao
import com.vladusecho.schoolevents.data.local.model.FavouriteEventModel
import com.vladusecho.schoolevents.data.local.model.SubscribedEventModel
import com.vladusecho.schoolevents.data.mapper.toEventEntity
import com.vladusecho.schoolevents.data.mapper.toEventModel
import com.vladusecho.schoolevents.data.mapper.toEventWithStatusEntityListFlow
import com.vladusecho.schoolevents.data.mapper.toProfileEntity
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val dao: EventsAppDao,
    @param:ApplicationContext private val context: Context
) : EventsRepository {

    private val currentUserEmailKey = stringPreferencesKey("current_user_email")

    private val userEmailFlow: Flow<String> = context.dataStoreSettings.data
        .map { preferences -> preferences[currentUserEmailKey] ?: "" }

    private suspend fun getCurrentUserEmail(): String {
        return userEmailFlow.first()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getEvents(): Flow<List<Event>> {
        return userEmailFlow.flatMapLatest { email ->
            dao.getEvents(email).toEventWithStatusEntityListFlow()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getSubscribedEvents(): Flow<List<Event>> {
        return userEmailFlow.flatMapLatest { email ->
            dao.getSubscribedEvents(email).toEventWithStatusEntityListFlow()
        }
    }

    override suspend fun subscribeToEvent(eventId: Int) {
        val email = getCurrentUserEmail()
        dao.subscribeToEvent(SubscribedEventModel(userEmail = email, eventId = eventId))
    }

    override suspend fun unsubscribeFromEvent(eventId: Int) {
        val email = getCurrentUserEmail()
        dao.unsubscribeFromEvent(userEmail = email, eventId = eventId)
    }

    override suspend fun switchFavouriteStatus(isFavourite: Boolean, eventId: Int) {
        val email = getCurrentUserEmail()
        if (!isFavourite) {
            dao.addFavouriteEvent(FavouriteEventModel(userEmail = email, eventId = eventId))
        } else {
            dao.removeFavouriteEvent(userEmail = email, eventId = eventId)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFavouriteEvents(): Flow<List<Event>> {
        return userEmailFlow.flatMapLatest { email ->
            dao.getFavouriteEvents(email).toEventWithStatusEntityListFlow()
        }
    }

    override suspend fun getEventById(eventId: Int): Event {
        val email = getCurrentUserEmail()
        return dao.getEventWithStatusById(eventId, email).let { 
            it.event.toEventEntity(isFavourite = it.isFavourite, isSubscribed = it.isSubscribed)
        }
    }

    override fun getConfirmationEvents(): Flow<List<Event>> {
        return flowOf(emptyList())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getArchivedEvents(): Flow<List<Event>> {
        return userEmailFlow.flatMapLatest { email ->
            dao.getArchivedEvents(email).toEventWithStatusEntityListFlow()
        }
    }

    override suspend fun addNewEvent(event: Event) {
        dao.insertEvent(event.toEventModel())
    }

    override suspend fun saveImageToInternalStorage(uri: String): String {
        return withContext(Dispatchers.IO) {
            val contentUri = uri.toUri()
            val inputStream = context.contentResolver.openInputStream(contentUri)
            val fileName = "event_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)

            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        }
    }

    override suspend fun saveImagesToInternalStorage(uris: List<String>): List<String> {
        return uris.map { uri ->
            if (uri.startsWith("/")) uri else saveImageToInternalStorage(uri)
        }
    }

    override suspend fun deleteEvent(eventId: Int) {
        dao.deleteEvent(eventId)
    }

    override fun getParticipants(eventId: Int): Flow<List<Profile>> {
        return dao.getParticipants(eventId).map { list ->
            list.map { it.toProfileEntity() }
        }
    }
}
