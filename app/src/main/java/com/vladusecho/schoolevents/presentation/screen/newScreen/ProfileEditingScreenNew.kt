package com.vladusecho.schoolevents.presentation.screen.newScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.util.UserRole
import com.vladusecho.schoolevents.presentation.viewModel.EditingProfileViewModel

@Composable
fun ProfileEditingScreenNew(
    modifier: Modifier = Modifier,
    viewModel: EditingProfileViewModel = hiltViewModel(),
    profile: Profile,
    onBackClick: () -> Unit
) {

    ProfileEditingContent(
        profile = profile,
        onSaveClick = { newProfile ->
            viewModel.processCommand(
                EditingProfileViewModel.EditingProfileCommand.SaveProfile(newProfile)
            )
            onBackClick()
        },
        onBackClick = onBackClick
    )
}

@Composable
fun ProfileEditingContent(
    modifier: Modifier = Modifier,
    profile: Profile,
    onSaveClick: (newProfile: Profile) -> Unit,
    onBackClick: () -> Unit
) {

    val userClass = remember { mutableStateOf(TextFieldValue(profile.classNumber)) }
    val userName = remember { mutableStateOf(TextFieldValue(profile.name)) }
    val userSurname = remember { mutableStateOf(TextFieldValue(profile.surname)) }
    val userEmail = remember { mutableStateOf(TextFieldValue(profile.email)) }

    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    val emailPattern = android.util.Patterns.EMAIL_ADDRESS
    val classRegex = Regex("^(1[0-1]|[1-9])[a-яё]?$")

    val isEmailValid by remember {
        derivedStateOf { emailPattern.matcher(userEmail.value.text).matches() }
    }
    val isNameValid by remember {
        derivedStateOf { userName.value.text.isNotBlank() && userName.value.text.length <= 20 }
    }
    val isSurnameValid by remember {
        derivedStateOf { userSurname.value.text.isNotBlank() && userSurname.value.text.length <= 20 }
    }
    val isClassValid by remember {
        derivedStateOf {
            if (profile.role == UserRole.STUDENT.label) {
                classRegex.matches(userClass.value.text.lowercase())
            } else true
        }
    }

    val isFormValid = isEmailValid && isNameValid && isSurnameValid && isClassValid

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let { selectedImageUri.value = it }
        }
    )

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        Text(
            text = "Редактирование",
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
                .background(Color(0xffEBEBEB))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
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
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (profile.role == UserRole.STUDENT.label) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            userClass.value = userClass.value.copy(
                                selection = TextRange(0, userClass.value.text.length)
                            )
                        }
                    },
                value = userClass.value,
                shape = RoundedCornerShape(12.dp),
                onValueChange = { userClass.value = it },
                isError = !isClassValid && userClass.value.text.isNotEmpty(),
                supportingText = {
                    if (!isClassValid && userClass.value.text.isNotEmpty()) {
                        Text(
                            text = "Пример: 9 или 11а",
                            color = MaterialTheme.colorScheme.error,
                            fontFamily = EventsFontFamily
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_user),
                        contentDescription = null,
                    )
                },
                suffix = {
                    Text(
                        text = "класс",
                        fontFamily = EventsFontFamily
                    )
                },
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xffEBEBEB),
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedLeadingIconColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        userName.value = userName.value.copy(
                            selection = TextRange(0, userName.value.text.length)
                        )
                    }
                },
            value = userName.value,
            shape = RoundedCornerShape(12.dp),
            onValueChange = { newValue ->
                if (newValue.text.length <= 20) {
                    val formattedText = newValue.text.replaceFirstChar { char ->
                        if (char.isLowerCase()) char.titlecase() else char.toString()
                    }
                    userName.value = newValue.copy(text = formattedText)
                }
            },
            isError = !isNameValid && userName.value.text.isNotEmpty(),
            supportingText = {
                if (!isNameValid && userName.value.text.isNotEmpty()) {
                    Text(
                        text = if (userName.value.text.isEmpty()) "Имя не может быть пустым" else "Максимум 20 символов",
                        color = MaterialTheme.colorScheme.error,
                        fontFamily = EventsFontFamily
                    )
                }
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_user),
                    contentDescription = "",
                )
            },
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xffEBEBEB),
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedLeadingIconColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            label = {
                Text(
                    text = "Имя",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Normal,
                )
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        userSurname.value = userSurname.value.copy(
                            selection = TextRange(0, userSurname.value.text.length)
                        )
                    }
                },
            value = userSurname.value,
            shape = RoundedCornerShape(12.dp),
            onValueChange = { newValue ->
                if (newValue.text.length <= 20) {
                    val formattedText = newValue.text.replaceFirstChar { char ->
                        if (char.isLowerCase()) char.titlecase() else char.toString()
                    }
                    userSurname.value = newValue.copy(text = formattedText)
                }
            },
            isError = !isSurnameValid && userSurname.value.text.isNotEmpty(),
            supportingText = {
                if (!isSurnameValid && userSurname.value.text.isNotEmpty()) {
                    Text(
                        text = if (userSurname.value.text.isEmpty()) "Фамилия не может быть пустой" else "Максимум 20 символов",
                        color = MaterialTheme.colorScheme.error,
                        fontFamily = EventsFontFamily
                    )
                }
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_user),
                    contentDescription = "",
                )
            },
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xffEBEBEB),
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedLeadingIconColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            label = {
                Text(
                    text = "Фамилия",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Normal,
                )
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        userEmail.value = userEmail.value.copy(
                            selection = TextRange(0, userEmail.value.text.length)
                        )
                    }
                },
            shape = RoundedCornerShape(12.dp),
            value = userEmail.value,
            onValueChange = { userEmail.value = it },
            isError = !isEmailValid && userEmail.value.text.isNotEmpty(),
            supportingText = {
                if (!isEmailValid && userEmail.value.text.isNotEmpty()) {
                    Text(
                        text = "Некорректный формат почты",
                        color = MaterialTheme.colorScheme.error,
                        fontFamily = EventsFontFamily
                    )
                }
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_mail),
                    contentDescription = "",
                )
            },
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xffEBEBEB),
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedLeadingIconColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            label = {
                Text(
                    text = "Почта",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Normal,
                )
            },
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Button(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    onBackClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color(0xffEBEBEB))
            ) {
                Text(
                    text = "Вернуться",
                    fontFamily = EventsFontFamily,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Button(
                modifier = Modifier
                    .weight(1f),
                enabled = isFormValid,
                onClick = {
                    onSaveClick(
                        profile.copy(
                            name = userName.value.text,
                            surname = userSurname.value.text,
                            email = userEmail.value.text,
                            classNumber = userClass.value.text,
                            imageUrl = selectedImageUri.value?.toString() ?: profile.imageUrl
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) Color(0xff008A00) else Color.White.copy(alpha = 0.5f),
                    contentColor = if (isFormValid) Color.White else Color.Black.copy(alpha = 0.5f)
                ),
                border = BorderStroke(1.dp, Color(0xffEBEBEB))
            ) {
                Text(
                    text = "Сохранить",
                    fontFamily = EventsFontFamily,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun ProfileEditingScreenPreview() {
    SchoolEventsTheme(
        darkTheme = false
    ) {
        ProfileEditingContent(
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
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
