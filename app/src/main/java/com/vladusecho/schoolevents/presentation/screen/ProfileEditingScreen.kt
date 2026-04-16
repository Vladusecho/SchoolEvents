package com.vladusecho.schoolevents.presentation.screen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme

@Composable
fun ProfileEditingScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
//        when (val currentState = state.value) {
//            is MainViewModel.MainState.Content -> {
//                
//            }
//
//            is MainViewModel.MainState.Error -> {
//
//            }
//
//            MainViewModel.MainState.Initial -> {
//
//            }
//
//            MainViewModel.MainState.Loading -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(
//                        color = Color(0xff0DCDAA)
//                    )
//                }
//            }
//        }
        ProfileEditingContent(
            Profile(
                id = 100,
                name = "Никита",
                surname = "Княгинин",
                email = "nikitaknyaginin@yandex.ru",
                classNumber = "9",
                role = "Ученик",
                imageUrl = "",
            )
        )
        Box(
            modifier = Modifier
                .height(110.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .background(Color(0xff0DCDAA))
                .padding(start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null,
                            tint = Color(0xff0DCDAA),
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color.White)
                                .size(32.dp)
                        )
                    }
                    Text(
                        text = "Редактирование",
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "--- Создано Vladusecho (Владислав Корзун) ---",
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileEditingContent(
    profile: Profile
) {
    Column(
        modifier = Modifier
            .padding(top = 128.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier.size(96.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_avatar),
                contentDescription = "",
                tint = Color(0xff0DCDAA)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        EventsTextField(
            value = profile.classNumber,
            onValueChange = {},
            prefix = {
                Row(

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_profile),
                        contentDescription = "",
                        tint = Color(0xff0DCDAA)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            },
            suffix = {
                Text(
                    text = "класс"
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        EventsTextField(
            value = profile.name,
            onValueChange = {},
            prefix = {
                Row(

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_profile),
                        contentDescription = "",
                        tint = Color(0xff0DCDAA)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        EventsTextField(
            value = profile.surname,
            onValueChange = {},
            prefix = {
                Row(

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_profile),
                        contentDescription = "",
                        tint = Color(0xff0DCDAA)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        EventsTextField(
            value = profile.email,
            onValueChange = {},
            prefix = {
                Row(

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_mail),
                        contentDescription = "",
                        tint = Color(0xff0DCDAA)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xff0DCDAA)
            ),
            modifier = Modifier
        ) {
            Text(
                text = "Сохранить",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun EventsTextField(
    modifier: Modifier = Modifier,
    value: String,
    prefix: @Composable (() -> Unit),
    suffix: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .border(1.dp, Color(0xff0DCDAA), RoundedCornerShape(20)),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        prefix = prefix,
        suffix = suffix
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileEditingScreenPreview() {
    SchoolEventsTheme {
        ProfileEditingScreen() {}
    }
}