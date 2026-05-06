package com.vladusecho.schoolevents.presentation.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.data.local.ParticipantWithAbsence
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.viewModel.ParticipantsViewModel

@Composable
fun ParticipantsScreen(
    modifier: Modifier = Modifier,
    eventId: Int,
    viewModel: ParticipantsViewModel = hiltViewModel(
        creationCallback = { factory: ParticipantsViewModel.Factory ->
            factory.create(eventId)
        }
    ),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val currentState = state) {
            is ParticipantsViewModel.ParticipantsState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is ParticipantsViewModel.ParticipantsState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = currentState.message, color = Color.Red)
                }
            }

            is ParticipantsViewModel.ParticipantsState.Content -> {
                if (currentState.participants.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Участников пока нет",
                            fontFamily = EventsFontFamily,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 128.dp, bottom = 128.dp)
                    ) {
                        items(currentState.participants) { participant ->
                            ParticipantItem(
                                participant = participant,
                                onAbsenceToggle = { wasAbsent ->
                                    viewModel.markAbsence(participant.profile.email, wasAbsent)
                                }
                            )
                        }
                    }
                }
            }

            else -> {}
        }
        Box(
            modifier = Modifier.fillMaxSize().padding(bottom = 128.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))

            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .size(128.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier.padding(bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Участники\nмероприятия",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ParticipantItem(
    participant: ParticipantWithAbsence,
    onAbsenceToggle: (Boolean) -> Unit
) {
    val profile = participant.profile
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.onBackground)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = profile.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            error = painterResource(id = R.drawable.ic_profile_screen)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(
                    text = "${profile.name} ${profile.surname}, ",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = profile.classNumber + " класс",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                )
            }
            Text(
                text = profile.email,
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Прогул",
                fontFamily = EventsFontFamily,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Checkbox(
                checked = participant.wasAbsent,
                onCheckedChange = onAbsenceToggle,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Red,
                    uncheckedColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
