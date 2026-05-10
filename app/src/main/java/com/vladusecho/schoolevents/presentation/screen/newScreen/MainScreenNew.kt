package com.vladusecho.schoolevents.presentation.screen.newScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.News
import com.vladusecho.schoolevents.presentation.activity.LocalUserRole
import com.vladusecho.schoolevents.presentation.entity.NewsCard
import com.vladusecho.schoolevents.presentation.entity.StudentEventCard
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.util.UserRole
import com.vladusecho.schoolevents.presentation.viewModel.MainViewModel

@Composable
fun MainScreenNew(
    viewModel: MainViewModel = hiltViewModel(),
    onEventClick: (eventId: Int) -> Unit,
    onListClick: (eventId: Int) -> Unit,
    onNewsClick: (newsId: Int) -> Unit,
    onAddEventClick: () -> Unit,
    onAddNewsClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()

    when (val currentState = state) {
        is MainViewModel.MainState.Content -> {
            MainScreenContent(
                events = currentState.events,
                news = currentState.news,
                selectedTab = selectedTab,
                onTabClick = { viewModel.selectTab(it) },
                onEventClick = onEventClick,
                onListClick = onListClick,
                onNewsClick = onNewsClick,
                onFavouriteClick = { isFavourite, eventId ->
                    viewModel.processCommand(
                        MainViewModel.MainCommand.SwitchFavouriteStatus(
                            isFavourite = isFavourite,
                            eventId = eventId
                        )
                    )
                },
                onAddEventClick = onAddEventClick,
                onAddNewsClick = onAddNewsClick
            )
        }

        is MainViewModel.MainState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = currentState.message, color = Color.Red)
            }
        }

        MainViewModel.MainState.Initial -> {}
        MainViewModel.MainState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun MainScreenContent(
    events: List<Event>,
    news: List<News>,
    selectedTab: MainViewModel.MainTab,
    onTabClick: (MainViewModel.MainTab) -> Unit,
    onEventClick: (eventId: Int) -> Unit,
    onListClick: (eventId: Int) -> Unit,
    onNewsClick: (newsId: Int) -> Unit,
    onFavouriteClick: (Boolean, Int) -> Unit,
    onAddEventClick: () -> Unit,
    onAddNewsClick: () -> Unit,

    ) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            MainTitle(
                text = "Главная лента",
                selectedTab = selectedTab,
                onTabClick = onTabClick
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            )
        }

        when (selectedTab) {
            MainViewModel.MainTab.DISCOVER -> {
                item {
                    MainEventsRow(
                        events = events,
                        onEventClick = onEventClick,
                        onListClick = onListClick,
                        onFavouriteClick = onFavouriteClick,
                        onAddEventClick = onAddEventClick
                    )
                    Spacer(
                        modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                    )
                }
                item {
                    MainNewsHeader(
                        onAddNewsClick = onAddNewsClick
                    )
                }
                if (news.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Здесь пока ничего нет...",
                                fontFamily = EventsFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                } else {
                    items(news, key = { it.id }) { newsItem ->
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            NewsCard(news = newsItem, onNewsClick = onNewsClick)
                        }
                    }
                }
            }

            MainViewModel.MainTab.NEWS -> {
                item {
                    MainNewsHeader(
                        onAddNewsClick = onAddNewsClick
                    )
                }
                items(news, key = { it.id }) { newsItem ->
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        NewsCard(news = newsItem, onNewsClick = onNewsClick)
                    }
                }
            }

            MainViewModel.MainTab.EVENTS -> {
                item {
                    MainEventsHeader(
                        onAddEventClick = onAddEventClick
                    )
                }
                items(events, key = { it.id }) { event ->
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        StudentEventCard(
                            event = event,
                            onEventClick = onEventClick,
                            onListClick = onListClick,
                            onFavouriteClick = onFavouriteClick
                        )
                    }
                }
            }
        }
        item {
            Spacer(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .height(136.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun MainNewsHeader(
    modifier: Modifier = Modifier,
    onAddNewsClick: () -> Unit
) {
    val userRole = LocalUserRole.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Последние новости",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
            if (userRole == UserRole.ORGANIZER) {
                IconButton(onAddNewsClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus_circle),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MainEventsHeader(
    modifier: Modifier = Modifier,
    onAddEventClick: () -> Unit
) {
    val userRole = LocalUserRole.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Последние мероприятия",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
            if (userRole == UserRole.ORGANIZER) {
                IconButton(onAddEventClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus_circle),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MainEventsRow(
    modifier: Modifier = Modifier,
    events: List<Event>,
    onEventClick: (eventId: Int) -> Unit,
    onListClick: (eventId: Int) -> Unit,
    onFavouriteClick: (Boolean, Int) -> Unit,
    onAddEventClick: () -> Unit
) {
    val userRole = LocalUserRole.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Последние мероприятия",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.tertiary
            )
            if (userRole == UserRole.ORGANIZER) {
                IconButton(onAddEventClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus_circle),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (events.isEmpty()) {
                item {
                    Text(
                        text = "Здесь пока ничего нет...",
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            } else {
                items(events, key = { it.id }) { event ->
                    StudentEventCard(
                        modifier = Modifier.width(280.dp),
                        event = event,
                        onEventClick = onEventClick,
                        onListClick = onListClick,
                        onFavouriteClick = onFavouriteClick
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MainTitle(
    modifier: Modifier = Modifier,
    text: String,
    selectedTab: MainViewModel.MainTab,
    onTabClick: (MainViewModel.MainTab) -> Unit
) {
    val tabsEnumEntries = MainViewModel.MainTab.entries

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        Text(
            text = text,
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.tertiary
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
                        onTabClick = { onTabClick(it) }
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
            .background(if (isSelected) Color(0xff151B23) else MaterialTheme.colorScheme.surface)
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontFamily = EventsFontFamily,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
@Preview
fun MainPreview() {
    SchoolEventsTheme(
        darkTheme = true
    ) {
        MainScreenContent(
            events = emptyList(),
            news = emptyList(),
            selectedTab = MainViewModel.MainTab.DISCOVER,
            onTabClick = {},
            onEventClick = {},
            onListClick = {},
            onNewsClick = {},
            onFavouriteClick = { _, _ -> },
            onAddEventClick = {},
            onAddNewsClick = {}
        )
    }
}
