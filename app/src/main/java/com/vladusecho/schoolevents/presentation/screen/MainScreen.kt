package com.vladusecho.schoolevents.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.presentation.entity.StudentEventCard
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
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
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(top = 128.dp)
    ) {
        items(events) {
            StudentEventCard(
                event = it,
                onEventClick = {},
                onFavClick = {}
            )
        }

    }
    Box(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            .background(Color(0xff0DCDAA))
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = "Последние мероприятия нашей школы",
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}
