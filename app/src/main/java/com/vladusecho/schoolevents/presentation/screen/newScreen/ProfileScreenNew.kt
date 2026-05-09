package com.vladusecho.schoolevents.presentation.screen.newScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.presentation.activity.LocalUserRole
import com.vladusecho.schoolevents.presentation.screen.UserRole
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme

@Composable
fun ProfileScreenNew(
    modifier: Modifier = Modifier,
    onEventClick: (eventId: Int) -> Unit,
    onListClick: (eventId: Int) -> Unit = {},
    onEditingClick: (profile: Profile) -> Unit,
    onExitClick: () -> Unit
) {

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
        absentCount = 5,
        attendedCount = 3,
        onExitClick = onExitClick,
        onEditingClick = {
            onEditingClick(
                Profile(
                    id = 100,
                    name = "Никита",
                    surname = "Княгинин",
                    email = "nikitaknyaginin@yandex.ru",
                    password = "",
                    classNumber = "9",
                    role = "Ученик",
                    imageUrl = "",
                )
            )
        }
    )
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    profile: Profile,
    attendedCount: Int,
    absentCount: Int,
    onEditingClick: (profile: Profile) -> Unit,
    onExitClick: () -> Unit
) {

    val role = LocalUserRole.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(64.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Профиль",
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                    )
                    Button(
                        onClick = {
                            onEditingClick(profile)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, Color(0xffEBEBEB))
                    ) {
                        Text(
                            text = "Редактировать"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        item {
            UserProfile(
                profile = profile,
                attendedCount = attendedCount,
                absentCount = absentCount,
                onExitClick = onExitClick
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
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
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
    onExitClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
            val classLabel = if (profile.role == UserRole.STUDENT.label) ", ${profile.classNumber} класс" else ""
            Column() {
                Text(
                    text = profile.name + " " + profile.surname + classLabel,
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Text(
                    text = profile.email,
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (profile.role == UserRole.STUDENT.label) {
            UserAttendance(
                modifier = Modifier.fillMaxWidth(),
                attendedCount = attendedCount.toString(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            UserAttendance(
                modifier = Modifier.fillMaxWidth(),
                absentCount = absentCount.toString(),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    onExitClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color(0xffEBEBEB))
            ) {
                Text(
                    text = "Выйти из аккаунта"
                )
            }
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun UserAttendance(
    modifier: Modifier = Modifier,
    attendedCount: String? = null,
    absentCount: String? = null
) {
    Box(
        modifier = modifier
            .background(Color.White)
            .clip(RoundedCornerShape(20))
            .border(1.dp, Color(0xffEBEBEB), RoundedCornerShape(20))
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (attendedCount != null) "Посещено мероприятий:" else "Пропущено мероприятий:",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = attendedCount ?: absentCount!!,
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
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
            attendedCount = 5,
            absentCount = 2,
            onEditingClick = {},
            onExitClick = {}
        )
    }
}
