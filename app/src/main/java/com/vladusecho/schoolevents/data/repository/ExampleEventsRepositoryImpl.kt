package com.vladusecho.schoolevents.data.repository

import android.util.Log
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ExampleEventsRepositoryImpl @Inject constructor(
) : EventsRepository {

    private val _eventsFlow = MutableStateFlow(
        List(10) { id ->
            Event(
                id = id,
                title = "Концерт 5opka в нашей школе! Не пропустите это невероятное событие",
                description = "Пострадав в результате несчастного случая на стриме, провинциальный стример 5opka объединяется с лысым негром под псевдонимом MellSher, чтобы отправиться в тур «1+1» по городам России и рассказать всем свою невыдуманную историю, о которой невозможно молчать.",
                eventDate = "${id+1} июня",
                address = "ул. Ленина, д.80, Актовый зал",
                isFavourite = false
            )
        }
    )


    override fun getEvents(): Flow<List<Event>> {
        return _eventsFlow.asStateFlow()
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