package com.vladusecho.schoolevents.data.repository

import androidx.annotation.Discouraged
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class ExampleEventsRepositoryImpl @Inject constructor(
) : EventsRepository {

    val events = mutableListOf<Event>().apply {
        repeat(10) {
            add(
                Event(
                    id = it,
                    title = "Концерт 5opka в нашей школе! Не пропустите это невероятное событие",
                    description = "Пострадав в результате несчастного случая на стриме, провинциальный стример 5opka объединяется с лысым негром под псевдонимом MellSher, чтобы отправиться в тур «1+1» по городам России и рассказать всем свою невыдуманную историю, о которой невозможно молчать.",
                    eventDate = "10 июня",
                    address = "ул. Ленина, д.80, Актовый зал",
                    isFavourite = Random.nextBoolean()
                )
            )
        }
    }

    val eventsFlow = flowOf(events)

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun getEvents(): Flow<List<Event>> {
        return eventsFlow
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
}