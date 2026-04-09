package com.vladusecho.schoolevents.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.presentation.entity.StudentEventCard
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.viewModel.MainViewModel
import kotlin.random.Random

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState()

    when(val currentState = state.value) {
        is MainViewModel.MainState.Content -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 128.dp)
            ) {
                items(currentState.events) {
                    StudentEventCard(
                        event = it,
                        onEventClick = {},
                        onFavClick = {}
                    )
                }

            }
        }
        is MainViewModel.MainState.Error -> {

        }
        MainViewModel.MainState.Initial -> {

        }
        MainViewModel.MainState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xff0DCDAA)
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            .background(Color(0xff0DCDAA))
            .padding(start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column() {
            Text(
                text = "Последние мероприятия нашей школы",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "--- Создано Vladusecho (Владислав Корзун) ---",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    color = Color.White
                )}
        }
    }
}
