package com.vladusecho.schoolevents.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.presentation.activity.LocalUserRole
import com.vladusecho.schoolevents.presentation.entity.StudentEventCard
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.viewModel.ProfileViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onEventClick: (eventId: Int) -> Unit,
    onListClick: (eventId: Int) -> Unit = {},
    onEditingClick: (profile: Profile) -> Unit,
    onExitClick: () -> Unit
) {

    val state = viewModel.state.collectAsState()
    val role = LocalUserRole.current

    LaunchedEffect(Unit) {
        viewModel.isExit.collect {
            if (it) {
                onExitClick()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val currentState = state.value) {
            is ProfileViewModel.ProfileState.Content -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 128.dp)
                ) {
                    item {
                        ProfileContent(
                            profile = currentState.profile,
                            onEditingClick = { onEditingClick(currentState.profile) },
                            onExitClick = {
                                viewModel.processCommand(ProfileViewModel.ProfileCommand.Exit)
                            }
                        )
                    }
                    item {
                        Text(
                            text = when (role) {
                                UserRole.STUDENT -> "Вы записаны на мероприятия:"
                                UserRole.ORGANIZER -> "Ваши мероприятия:"
                                UserRole.DIRECTOR -> ""
                            },
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    if (currentState.events.isEmpty() && role != UserRole.DIRECTOR) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_sadface),
                                    "",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    } else {
                        items(
                            items = currentState.events,
                            key = { it.id }
                        ) {
                            Box(
                                modifier = Modifier.animateItem()
                            ) {
                                StudentEventCard(
                                    event = it,
                                    onEventClick = { eventId ->
                                        onEventClick(eventId)
                                    },
                                    onListClick = onListClick,
                                    onFavouriteClick = { isFavourite, eventId ->
                                        viewModel.processCommand(
                                            ProfileViewModel.ProfileCommand.SwitchFavouriteStatus(
                                                isFavourite,
                                                eventId
                                            )
                                        )

                                    }
                                )
                            }
                        }
                    }
                }
            }

            is ProfileViewModel.ProfileState.Error -> {

            }

            ProfileViewModel.ProfileState.Initial -> {

            }

            ProfileViewModel.ProfileState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .height(110.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Профиль",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    profile: Profile,
    onEditingClick: () -> Unit,
    onExitClick: () -> Unit
) {

    val isLoading = viewModel.isLoading.collectAsState()

    Box(
        modifier = modifier
            .padding(top = 128.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = profile.imageUrl,
                contentDescription = "",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_avatar),
                placeholder = painterResource(R.drawable.ic_avatar)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = profile.name + " " + profile.surname,
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = profile.email,
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            val classVisible =
                if (profile.role == UserRole.STUDENT.label) " | " + profile.classNumber + " класс" else ""
            Text(
                text = profile.role.uppercase() + classVisible,
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onEditingClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                ) {
                    Text(
                        text = "Редактировать",
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
                Button(
                    enabled = !isLoading.value,
                    onClick = onExitClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier
                ) {
                    Text(
                        text = "Выйти",
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_exit),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfPrev() {
    SchoolEventsTheme() {
        ProfileContent(
            profile = Profile(
                id = 100,
                name = "Никита",
                surname = "Княгинин",
                email = "nikitaknyaginin@yandex.ru",
                password = "",
                classNumber = "9",
                role = "Ученик",
                imageUrl = "",
            ),
            onEditingClick = {},
            onExitClick = {}
        )
    }
}
