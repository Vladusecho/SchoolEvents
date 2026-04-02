package com.vladusecho.schoolevents.presentation.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.presentation.entity.StudentEventCard
import kotlin.random.Random

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {

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

    LazyColumn() {
        items(events) {
            StudentEventCard(
                event = it,
                onEventClick = {}
            )
        }

    }
}