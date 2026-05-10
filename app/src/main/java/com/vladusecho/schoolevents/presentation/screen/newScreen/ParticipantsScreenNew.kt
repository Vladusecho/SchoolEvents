package com.vladusecho.schoolevents.presentation.screen.newScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.data.local.ParticipantWithAbsence
import com.vladusecho.schoolevents.data.local.model.ProfileModel
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.viewModel.ParticipantsViewModel

@Composable
fun ParticipantsScreenNew(
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
    val searchQuery by viewModel.searchQuery.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        onResult = { uri: Uri? ->
            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    viewModel.exportToExcel(outputStream)
                }
            }
        }
    )

    when (val currentState = state) {
        is ParticipantsViewModel.ParticipantsState.Content -> {
            ParticipantsScreenContent(
                participants = currentState.participants,
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                onBackClick = onBackClick,
                onExportClick = {
                    launcher.launch("Participants_Event_$eventId.xlsx")
                },
                onAbsenceToggle = { email, wasAbsent ->
                    viewModel.markAbsence(email, wasAbsent)
                }
            )
        }

        is ParticipantsViewModel.ParticipantsState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = currentState.message, color = Color.Red)
            }
        }

        ParticipantsViewModel.ParticipantsState.Initial -> {}
        ParticipantsViewModel.ParticipantsState.Loading -> {
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun ParticipantsScreenContent(
    modifier: Modifier = Modifier,
    participants: List<ParticipantWithAbsence> = emptyList(),
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onExportClick: () -> Unit = {},
    onAbsenceToggle: (String, Boolean) -> Unit = { _, _ -> }
) {
    val filteredParticipants = if (searchQuery.isBlank()) {
        participants
    } else {
        participants.filter {
            it.profile.name.contains(searchQuery, ignoreCase = true) ||
            it.profile.surname.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(64.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Участники",
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = {
                        Text(
                            text = "Поиск по имени или фамилии",
                            fontFamily = EventsFontFamily,
                            color = Color.Gray
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedTextColor = MaterialTheme.colorScheme.tertiary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onExportClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Выгрузить в Excel",
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                )
            }
        }

        if (filteredParticipants.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isEmpty()) "Список участников пуст" else "Ничего не найдено",
                        fontFamily = EventsFontFamily,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            items(filteredParticipants, key = { it.profile.email }) { participant ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    ParticipantCard(
                        participant = participant,
                        onAbsenceToggle = { wasAbsent ->
                            onAbsenceToggle(participant.profile.email, wasAbsent)
                        }
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
fun ParticipantCard(
    modifier: Modifier = Modifier,
    participant: ParticipantWithAbsence,
    onAbsenceToggle: (Boolean) -> Unit
) {
    val profile = participant.profile
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = profile.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                error = painterResource(id = R.drawable.ic_avatar),
                placeholder = painterResource(id = R.drawable.ic_avatar)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${profile.name} ${profile.surname}",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${profile.classNumber} класс",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = profile.email,
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = "Прогул",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    color = if (participant.wasAbsent) Color.Red else MaterialTheme.colorScheme.tertiary
                )
                Checkbox(
                    checked = participant.wasAbsent,
                    onCheckedChange = onAbsenceToggle,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Red,
                        uncheckedColor = MaterialTheme.colorScheme.tertiary
                    )
                )
            }
        }
    }
}

@Composable
@Preview
fun ParticipantsPreview() {
    SchoolEventsTheme(
        darkTheme = true
    ) {
        ParticipantsScreenContent(
            participants = listOf(
                ParticipantWithAbsence(
                    profile = ProfileModel(
                        id = 1,
                        email = "test@mail.ru",
                        name = "Иван",
                        surname = "Иванов",
                        classNumber = "10А",
                        imageUrl = "",
                        password = "123",
                        role = "STUDENT"
                    ),
                    wasAbsent = false
                )
            )
        )
    }
}
