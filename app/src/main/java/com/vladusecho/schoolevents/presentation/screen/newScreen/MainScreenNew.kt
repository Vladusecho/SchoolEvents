package com.vladusecho.schoolevents.presentation.screen.newScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.News
import com.vladusecho.schoolevents.presentation.activity.LocalUserRole
import com.vladusecho.schoolevents.presentation.entity.NewsCard
import com.vladusecho.schoolevents.presentation.entity.StudentEventCard
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme

@Composable
fun MainScreenNew(
    modifier: Modifier = Modifier,
    onEventClick: (eventId: Int) -> Unit,
    onListClick: (eventId: Int) -> Unit,
    onNewsClick: (newsId: Int) -> Unit,
    onAddEventClick: () -> Unit,
    onAddNewsClick: () -> Unit,
) {

    val userRole = LocalUserRole.current

    MainScreenContent()
}

@Composable
fun MainScreenContent() {
    val newsList = remember { List(1) { index ->
        News(id = index, title = "Новость #$index", description = "Описание...", imageUrls = listOf(""), date = "12.02.2023")
    } }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffEBEBEB))
    ) {
        item {
            MainTitle(
                text = "Главная лента",
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            MainEvents()
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            MainNews()
        }
        items(newsList) { newsItem ->
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp))
            {
                NewsCard(news = newsItem)
            }
        }
        item {
            Spacer(modifier = Modifier
                .background(Color.White)
                .height(136.dp)
            )
        }
    }
}

@Composable
fun MainNews(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Последние новости",
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MainEvents(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Последние мероприятия",
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                StudentEventCard(
                    event = Event(
                        id = 1,
                        title = "Концерт 5opka в нашей школе!",
                        description = "Описание...",
                        eventAddress = "ул. Ленина, д.80",
                        eventPlace = "Актовый зал",
                        eventDate = "10 июня",
                        eventDuration = "8:00 - 13:00",
                        isArchived = false,
                        isFavourite = false,
                        isSubscribed = false,
                        creatorEmail = "",
                        imageUrls = emptyList()
                    )
                ) { }
            }
            item {
                StudentEventCard(
                    event = Event(
                        id = 1,
                        title = "Концерт 5opka в нашей школе!",
                        description = "Описание...",
                        eventAddress = "ул. Ленина, д.80",
                        eventPlace = "Актовый зал",
                        eventDate = "10 июня",
                        eventDuration = "8:00 - 13:00",
                        isArchived = false,
                        isFavourite = false,
                        isSubscribed = false,
                        creatorEmail = "",
                        imageUrls = emptyList()
                    )
                ) { }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MainTitle(
    modifier: Modifier = Modifier,
    text: String
) {

    val tabsEnumEntries = MainTabs.entries
    var selectedTab by remember { mutableStateOf(MainTabs.DISCOVER) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        Text(
            text = text,
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabsEnumEntries.forEach {
                item {
                    MainTab(
                        text = it.title,
                        isSelected = it == selectedTab,
                        onTabClick = { selectedTab = it }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MainTab(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onTabClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable {
                onTabClick()
            }
            .border(1.dp, Color(0xffEBEBEB), RoundedCornerShape(50))
            .background(if (isSelected) Color(0xff151B23) else Color(0xffF8F8F9))
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontFamily = EventsFontFamily,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

enum class MainTabs(val title: String) {
    DISCOVER("Все"),
    NEWS("Новости"),
    EVENTS("Ивенты")
}

@Composable
@Preview
fun MainPreview() {
    SchoolEventsTheme() {
        MainScreenContent()
    }
}