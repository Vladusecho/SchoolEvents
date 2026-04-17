package com.vladusecho.schoolevents.presentation.screen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.viewModel.EditingProfileViewModel
import java.io.File

@Composable
fun ProfileEditingScreen(
    modifier: Modifier = Modifier,
    viewModel: EditingProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {

    val state = viewModel.state.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val currentState = state.value) {
            is EditingProfileViewModel.EditingProfileState.Content -> {
                ProfileEditingContent(
                    profile = currentState.profile,
                    onSaveClick = { newProfile ->
                        viewModel.processCommand(
                            EditingProfileViewModel.EditingProfileCommand.SaveProfile(
                                newProfile
                            )
                        )
                        onBackClick()
                    },
                    onBackClick = onBackClick
                )
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
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Редактирование",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
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
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            is EditingProfileViewModel.EditingProfileState.Error -> {

            }

            EditingProfileViewModel.EditingProfileState.Initial -> {

            }

            EditingProfileViewModel.EditingProfileState.Loading -> {
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
    }
}

@Composable
fun ProfileEditingContent(
    modifier: Modifier = Modifier,
    profile: Profile,
    onSaveClick: (newProfile: Profile) -> Unit,
    onBackClick: () -> Unit
) {

    val context = LocalContext.current

    val userClass = remember { mutableStateOf(profile.classNumber) }
    val userName = remember { mutableStateOf(profile.name) }
    val userSurname = remember { mutableStateOf(profile.surname) }
    val userEmail = remember { mutableStateOf(profile.email) }

    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let { selectedImageUri.value = it }
        }
    )

    Column(
        modifier = modifier
            .padding(top = 128.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                imagePicker.launch("image/*")
            },
            modifier = Modifier.size(96.dp)
        ) {
            AsyncImage(
                model = selectedImageUri.value ?: profile.imageUrl,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_avatar),
                error = painterResource(R.drawable.ic_avatar)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        EventsTextField(
            value = userClass.value,
            onValueChange = { userClass.value = it },
            prefix = {
                Row(

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_profile),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
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
            value = userName.value,
            onValueChange = { userName.value = it },
            prefix = {
                Row(

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_profile),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        EventsTextField(
            value = userSurname.value,
            onValueChange = { userSurname.value = it },
            prefix = {
                Row(

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_profile),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        EventsTextField(
            value = userEmail.value,
            onValueChange = { userEmail.value = it },
            prefix = {
                Row(

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_mail),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                onSaveClick(
                    Profile(
                        id = profile.id,
                        name = userName.value,
                        surname = userSurname.value,
                        email = userEmail.value,
                        classNumber = userClass.value,
                        role = profile.role,
                        imageUrl = selectedImageUri.value?.toString() ?: profile.imageUrl
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = "Сохранить",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = "Вернуться",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
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
    placeholder: @Composable (() -> Unit)? = null,
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
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.secondary
        ),
        prefix = prefix,
        suffix = suffix,
        placeholder = placeholder
    )
}

@Preview()
@Composable
private fun ProfileEditingScreenPreview() {
    SchoolEventsTheme(
        darkTheme = true
    ) {
        ProfileEditingContent(
            profile = Profile(
                id = 100,
                name = "Никита",
                surname = "Княгинин",
                email = "nikitaknyaginin@yandex.ru",
                classNumber = "9",
                role = "Ученик",
                imageUrl = "",
            ),
            onBackClick = {},
            onSaveClick = {}
        )
    }
}