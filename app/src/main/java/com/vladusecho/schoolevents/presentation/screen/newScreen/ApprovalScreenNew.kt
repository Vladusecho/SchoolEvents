package com.vladusecho.schoolevents.presentation.screen.newScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.presentation.entity.StudentEventCard
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.viewModel.ApprovalViewModel

@Composable
fun ApprovalScreenNew(
    modifier: Modifier = Modifier,
    viewModel: ApprovalViewModel = hiltViewModel(),
    onEventClick: (eventId: Int) -> Unit
) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is ApprovalViewModel.ApprovalState.Content -> {
            ApprovalScreenContent(
                events = currentState.events,
                onEventClick = onEventClick
            )
        }

        is ApprovalViewModel.ApprovalState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = currentState.message, color = Color.Red)
            }
        }

        ApprovalViewModel.ApprovalState.Initial -> {}
        ApprovalViewModel.ApprovalState.Loading -> {
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun ApprovalScreenContent(
    modifier: Modifier = Modifier,
    events: List<Event> = emptyList(),
    onEventClick: (eventId: Int) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Column(modifier = Modifier) {
                Spacer(modifier = Modifier.height(64.dp))
                Text(
                    text = "На утверждение",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                )
            }
        }

        if (events.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нет мероприятий на утверждение",
                        fontFamily = EventsFontFamily,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            items(events, key = { it.id }) { event ->
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    StudentEventCard(
                        event = event,
                        onEventClick = onEventClick
                    )
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
@Preview
fun ApprovalPreview() {
    SchoolEventsTheme(
        darkTheme = true
    ) {
        ApprovalScreenContent()
    }
}
