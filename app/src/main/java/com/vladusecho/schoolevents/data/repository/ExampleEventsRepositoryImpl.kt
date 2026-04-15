package com.vladusecho.schoolevents.data.repository

import android.util.Log
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ExampleEventsRepositoryImpl @Inject constructor(
) : EventsRepository {

    private val _eventsFlow = MutableStateFlow(
        List(10) { id ->
            Event(
                id = id,
                title = "Концерт 5opka в нашей школе! Не пропустите это невероятное событие",
                description = "Пострадав в результате несчастного случая на стриме, провинциальный стример 5opka объединяется с лысым негром под псевдонимом MellSher, чтобы отправиться в тур «1+1» по городам России и рассказать всем свою невыдуманную историю, о которой невозможно молчать. 1+1 = 11 городов. Победители всех музыкальных премий, авторы хитов «XXL» и «Мерси», люди, которые не нуждаются в представлении, но мы их все равно представили, в твоём городе. Приходи на их самые большие концерты или будешь жалеть всю жизнь!",
                eventAddress = "ул. Ленина, д.80",
                eventDate = "$id июня",
                isFavourite = false,
                eventPlace = "Актовый зал",
                eventDuration = "Вторник, 8:00 - 13:00",
                isSubscribed = false
            )
        }
    )

    private val _profileFlow = MutableStateFlow(
        Profile(
            id = 100,
            name = "Никита",
            surname = "Княгинин",
            email = "nikitaknyaginin@yandex.ru",
            classNumber = "9",
            role = "Ученик",
            imageUrl = "",
        )
    )

    override fun getEvents(): Flow<List<Event>> {
        return _eventsFlow.asStateFlow()
    }

    override fun getSubscribedEvents(): Flow<List<Event>> {
        return _eventsFlow.map { events ->
            events.filter { it.isSubscribed }
        }
    }

    override suspend fun subscribeToEvent(eventId: Int) {
        _eventsFlow.update { eventsList ->
            eventsList.map { event ->
                if (event.id == eventId) {
                    event.copy(isSubscribed = true)
                } else {
                    event
                }
            }
        }
    }

    override suspend fun unsubscribeFromEvent(eventId: Int) {
        _eventsFlow.update { eventsList ->
            eventsList.map { event ->
                if (event.id == eventId) {
                    event.copy(isSubscribed = false)
                } else {
                    event
                }
            }
        }
    }

    override suspend fun switchFavouriteStatus(isFavourite: Boolean, eventId: Int) {
        Log.d("tag", _eventsFlow.value.toString())
        _eventsFlow.update { prevState ->
            prevState.map { event ->
                if (event.id == eventId) {
                    event.copy(isFavourite = !isFavourite)
                } else {
                    event
                }
            }
        }
    }

    override fun getFavouriteEvents(): Flow<List<Event>> {
        return _eventsFlow.map { events ->
            events.filter { it.isFavourite }
        }
    }

    override suspend fun getEventById(eventId: Int): Event {
        return _eventsFlow.value.first { it.id == eventId }
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
        return _profileFlow.asStateFlow()
    }
}