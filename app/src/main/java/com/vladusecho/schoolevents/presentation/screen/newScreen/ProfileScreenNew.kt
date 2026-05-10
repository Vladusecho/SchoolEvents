package com.vladusecho.schoolevents.presentation.screen.newScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.presentation.activity.LocalUserRole
import com.vladusecho.schoolevents.presentation.entity.StudentEventCard
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.util.UserRole
import com.vladusecho.schoolevents.presentation.viewModel.ProfileViewModel

@Composable
fun ProfileScreenNew(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onEventClick: (eventId: Int) -> Unit,
    onListClick: (eventId: Int) -> Unit = {},
    onEditingClick: (profile: Profile) -> Unit,
    onThemeToggle: () -> Unit,
    onExitClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        onResult = { uri: Uri? ->
            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    viewModel.exportWeeklyStatsToExcel(outputStream)
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.isExit.collect {
            if (it) {
                onExitClick()
            }
        }
    }

    when (val currentState = state) {
        is ProfileViewModel.ProfileState.Content -> {
            ProfileContent(
                profile = currentState.profile,
                events = currentState.events,
                attendedCount = currentState.attendedCount,
                absentCount = currentState.absentCount,
                weeklyStats = currentState.weeklyStats,
                onExitClick = {
                    viewModel.processCommand(ProfileViewModel.ProfileCommand.Exit)
                },
                onThemeToggle = onThemeToggle,
                onEditingClick = {
                    onEditingClick(currentState.profile)
                },
                onEventClick = onEventClick,
                onListClick = onListClick,
                onExportClick = {
                    launcher.launch("Weekly_Stats.xlsx")
                },
                onFavouriteClick = { isFavourite, eventId ->
                    viewModel.processCommand(
                        ProfileViewModel.ProfileCommand.SwitchFavouriteStatus(
                            isFavourite = isFavourite,
                            eventId = eventId
                        )
                    )
                }
            )
        }

        is ProfileViewModel.ProfileState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = currentState.message, color = Color.Red)
            }
        }

        ProfileViewModel.ProfileState.Initial -> {}
        ProfileViewModel.ProfileState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    profile: Profile,
    events: List<Event>,
    attendedCount: Int,
    absentCount: Int,
    weeklyStats: List<ProfileViewModel.DayStat>,
    onEditingClick: (profile: Profile) -> Unit,
    onThemeToggle: () -> Unit,
    onExitClick: () -> Unit,
    onEventClick: (Int) -> Unit,
    onListClick: (Int) -> Unit,
    onExportClick: () -> Unit,
    onFavouriteClick: (Boolean, Int) -> Unit
) {

    val role = LocalUserRole.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Column() {
                Spacer(modifier = Modifier.height(64.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Профиль",
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Button(
                        onClick = {
                            onEditingClick(profile)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, Color(0xffEBEBEB))
                    ) {
                        Text(
                            text = "Редактировать",
                            fontFamily = EventsFontFamily,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
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
        item {
            UserProfile(
                profile = profile,
                attendedCount = attendedCount,
                absentCount = absentCount,
                onThemeToggle = onThemeToggle,
                onExitClick = onExitClick
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            val titleText = when (role) {
                UserRole.STUDENT -> "Вы записаны на мероприятия:"
                UserRole.ORGANIZER -> "Ваши мероприятия:"
                UserRole.DIRECTOR -> "Статистика активности:"
            }
            if (titleText.isNotEmpty()) {
                Text(
                    text = titleText,
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (role == UserRole.DIRECTOR) {
            item {
                WeeklyStatsChart(
                    stats = weeklyStats,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                Button(
                    onClick = onExportClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Выгрузить статистику в Excel",
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else if (events.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_sadface),
                        "",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(32.dp)
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
                        onEventClick = onEventClick,
                        onListClick = onListClick,
                        onFavouriteClick = onFavouriteClick
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
fun UserProfile(
    modifier: Modifier = Modifier,
    attendedCount: Int,
    absentCount: Int,
    profile: Profile,
    onThemeToggle: () -> Unit,
    onExitClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = profile.imageUrl,
                contentDescription = "",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_avatar),
                placeholder = painterResource(R.drawable.ic_avatar)
            )
            Spacer(modifier = Modifier.width(8.dp))
            val classLabel =
                if (profile.role == UserRole.STUDENT.label) ", ${profile.classNumber} класс" else ""
            Column() {
                Text(
                    text = profile.name + " " + profile.surname + classLabel,
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = profile.email,
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (profile.role == UserRole.STUDENT.label) {
            UserAttendance(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                attendedCount = attendedCount.toString(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            UserAttendance(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                absentCount = absentCount.toString(),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onThemeToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color(0xffEBEBEB))
            ) {
                Text(
                    text = "Переключить тему",
                    fontFamily = EventsFontFamily,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Button(
                onClick = {
                    onExitClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color(0xffEBEBEB))
            ) {
                Text(
                    text = "Выйти из аккаунта",
                    fontFamily = EventsFontFamily,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

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

@Composable
fun UserAttendance(
    modifier: Modifier = Modifier,
    attendedCount: String = "",
    absentCount: String = ""
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = if (attendedCount.isNotEmpty()) "Посещено мероприятий" else "Пропущено мероприятий",
            fontFamily = EventsFontFamily,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = attendedCount.ifEmpty { absentCount },
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}
