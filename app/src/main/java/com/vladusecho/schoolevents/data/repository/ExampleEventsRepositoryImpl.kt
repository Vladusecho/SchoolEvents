package com.vladusecho.schoolevents.data.repository

import android.util.Log
import com.vladusecho.schoolevents.R
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
        mutableListOf<Event>().apply {
            add(
                Event(
                    id = 100,
                    title = "С нуля до 'Hello, World!': первый код за 2 часа",
                    description = "Практический воркшоп для тех, кто боится начинать. Разберём основы Python, напишем первую программу, посмотрим, как работает ChatGPT. Ноутбук нужен, зарядку брать обязательно!",
                    eventAddress = "ул. Ленина, д.80",
                    eventPlace = "Кабинет № 122",
                    eventDate = "18 июня",
                    eventDuration = "Среда, 19:00 - 21:00",
                    imageUrl = R.drawable.img_programming
                )
            )
            add(
                Event(
                    id = 200,
                    title = "День дублёра: ученики становятся учителями!",
                    description = "Старшеклассники проведут уроки вместо учителей. Можно попробовать себя в роли преподавателя математики, физкультуры или даже директора. Уроки по 20 минут. Расписание дублёров будет вывешено у входа в учительскую.",
                    eventAddress = "ул. Ленина, д.80",
                    eventPlace = "Все кабинеты школы",
                    eventDate = "15 июня",
                    eventDuration = "Понедельник, 8:30 - 13:30",
                    imageUrl = R.drawable.img_dubler_day
                )
            )
            add(
                Event(
                    id = 300,
                    title = "Золотая осень: школьная дискотека 2026",
                    description = "Танцы, конкурс на лучший осенний образ, караоке-баттл между классами. Dress code: жёлтый, оранжевый или красный аксессуар. Бутербродный фуршет и лимонад в холле.",
                    eventAddress = "ул. Ленина, д.80",
                    eventPlace = "Актовый зал",
                    eventDate = "28 сентября",
                    eventDuration = "Суббота, 17:00 - 20:00",
                    imageUrl = R.drawable.img_autumn
                )
            )
            add(
                Event(
                    id = 400,
                    title = "MathBattle: 9-11 классы сразятся в логике",
                    description = "Командная игра по мотивам «Что? Где? Когда?». Будет 4 раунда: теория вероятности, геометрия вокруг нас, логические задачи и блиц-опрос. Победителей ждут пятёрки в журнал по желанию.",
                    eventAddress = "ул. Ленина, д.80",
                    eventPlace = "Кабинет № 203",
                    eventDate = "4 октября",
                    eventDuration = "Пятница, 14:00 - 15:30",
                    imageUrl = R.drawable.img_math
                )
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
            } as MutableList<Event>
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
            } as MutableList<Event>
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
            } as MutableList<Event>
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

    override suspend fun updateProfile(profile: Profile) {
        _profileFlow.update { profile }
    }
}