package com.vladusecho.schoolevents.data.repository

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.data.local.EventsAppDao
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ExampleEventsRepositoryImpl @Inject constructor(
    private val dao: EventsAppDao,
    @param:ApplicationContext private val context: Context
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
                    description = "В этот день старшеклассники полностью берут на себя управление школой. Ученики 10-11 классов проведут уроки вместо учителей, а дублёр директора будет решать организационные вопросы и даже подписывать «виртуальные» приказы. Каждый желающий может попробовать себя в роли преподавателя математики, русского языка, физкультуры или даже школьного психолога. Уроки сокращены до 20 минут, чтобы дублёры не слишком уставали. В конце дня состоится «педсовет наоборот», где ученики-учителя дадут обратную связь настоящим педагогам. Расписание дублёров будет вывешено у входа в учительскую за день до мероприятия. Приветствуется строгий деловой стиль одежды и заранее подготовленные конспекты мини-уроков.",
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
                    description = "Самое яркое танцевальное событие этой осени! В программе: зажигательные сеты от школьного диджея (ученик 9 «Б»), конкурс на лучший осенний образ с отдельным призом зрительских симпатий, караоке-баттл между параллелями классов и медляк для тех, кто наконец решится пригласить кого-то на танец. Dress code: обязательный аксессуар жёлтого, оранжевого или красного цвета — это может быть галстук, заколка, браслет или даже осенний лист на лацкане. В холле будет работать буфет с лимонадом, мини-пиццей и печеньем в виде листьев. Фотозона с тыквами и зонтиками — для идеального школьного сториз. Победители конкурсов получат пригласительные на «сладкий стол» с директором.",
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
                    description = "Командная интеллектуальная игра для учеников 9-11 классов, построенная по мотивам телепередачи «Что? Где? Когда?» с математическим уклоном. Участников ждут четыре раунда: теория вероятности в реальной жизни (какова вероятность, что учитель вызовет именно тебя?), геометрия вокруг нас (почему крыши домов треугольные?), логические задачи на переливание и взвешивание, а также блиц-опрос из пяти суперсложных вопросов за 60 секунд. Команды формируются по 6 человек от каждого класса. Капитану разрешается один раз взять «помощь зала» — любой зритель может подсказать. Главный приз для команды-победителя — три пятёрки по математике в журнал по желанию каждого участника и безграничное уважение среди одноклассников.",
                    eventAddress = "ул. Ленина, д.80",
                    eventPlace = "Кабинет № 203",
                    eventDate = "4 октября",
                    eventDuration = "Пятница, 14:00 - 15:30",
                    imageUrl = R.drawable.img_math
                )
            )
        }
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
}