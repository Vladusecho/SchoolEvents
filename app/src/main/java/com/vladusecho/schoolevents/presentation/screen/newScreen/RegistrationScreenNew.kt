package com.vladusecho.schoolevents.presentation.screen.newScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.util.UserRole
import com.vladusecho.schoolevents.presentation.viewModel.AuthViewModel
import kotlin.random.Random

@Composable
fun RegistrationScreenNew(
    modifier: Modifier = Modifier,
    onRegistrationClick: () -> Unit,
    onBackClick: () -> Unit
) {
    RegistrationScreenContent(
        modifier = modifier,
        onRegistrationClick = onRegistrationClick,
        onBackClick = onBackClick
    )
}

@Composable
fun RegistrationScreenContent(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onRegistrationClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val surname = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val organizationCode = remember { mutableStateOf("") }
    val isAgreed = remember { mutableStateOf(false) }

    val isVisiblePassword = remember { mutableStateOf(false) }

    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }

    val focusManager = LocalFocusManager.current

    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var orgCodeError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.authResult.collect { success ->
            if (success) {
                onRegistrationClick()
            }
        }
    }

    val emailPattern = android.util.Patterns.EMAIL_ADDRESS
    val isEmailValid by remember {
        derivedStateOf { emailPattern.matcher(email.value.text).matches() }
    }
    val isPasswordValid = password.value.length >= 8 &&
            password.value.any { it.isDigit() } &&
            password.value.any { it.isLetter() }
    val isOrgCodeValid =
        if (selectedRole == UserRole.STUDENT) true else organizationCode.value == "1991"

    val isFormValid = name.value.text.isNotBlank() && name.value.text.length <= 20 &&
            surname.value.text.isNotBlank() && surname.value.text.length <= 20 &&
            isEmailValid &&
            isPasswordValid &&
            password.value == confirmPassword.value &&
            isOrgCodeValid &&
            isAgreed.value

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null
                )
            }
            Text(
                text = "Добро пожаловать!",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
            )
        }
        Text(
            text = "Введите все необходимые данные, чтобы начать пользоваться Внеурочной деятельностью",
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                        viewModel.clearErrors()
                    },
                    label = {
                        Text(
                            text = "Почта",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
                    },
                    isError = emailError != null || (email.value.text.isNotEmpty() && !isEmailValid),
                    supportingText = {
                        if (emailError != null) {
                            Text(
                                text = emailError!!,
                                color = MaterialTheme.colorScheme.error,
                                fontFamily = EventsFontFamily
                            )
                        } else if (email.value.text.isNotEmpty() && !isEmailValid) {
                            Text(
                                text = "Неверный формат почты",
                                color = MaterialTheme.colorScheme.error,
                                fontFamily = EventsFontFamily
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                email.value = email.value.copy(
                                    selection = TextRange(0, email.value.text.length)
                                )
                            }
                        },
                    textStyle = TextStyle(
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_mail),
                            contentDescription = null
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xffEBEBEB),
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        unfocusedLeadingIconColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it
                        viewModel.clearErrors()
                    },
                    label = {
                        Text(
                            text = "Пароль",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
                    },
                    isError = passwordError != null || (password.value.isNotEmpty() && !isPasswordValid),
                    supportingText = {
                        if (passwordError != null) {
                            Text(
                                text = passwordError!!,
                                color = MaterialTheme.colorScheme.error,
                                fontFamily = EventsFontFamily
                            )
                        } else if (password.value.isNotEmpty() && !isPasswordValid) {
                            Text(
                                text = "Минимум 8 символов, буквы и цифры",
                                color = MaterialTheme.colorScheme.error,
                                fontFamily = EventsFontFamily
                            )
                        }
                    },
                    visualTransformation = if (isVisiblePassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isVisiblePassword.value = !isVisiblePassword.value
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_eye),
                                contentDescription = null,
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_lock),
                            contentDescription = null
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xffEBEBEB),
                        focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                        unfocusedTrailingIconColor = Color.Black,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        unfocusedLeadingIconColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value = confirmPassword.value,
                    onValueChange = {
                        confirmPassword.value = it
                    },
                    label = {
                        Text(
                            text = "Повторите пароль",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
                    },
                    isError = confirmPassword.value.isNotEmpty() && confirmPassword.value != password.value,
                    supportingText = {
                        if (confirmPassword.value.isNotEmpty() && confirmPassword.value != password.value) {
                            Text(
                                text = "Пароли не совпадают",
                                color = MaterialTheme.colorScheme.error,
                                fontFamily = EventsFontFamily
                            )
                        }
                    },
                    visualTransformation = if (isVisiblePassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isVisiblePassword.value = !isVisiblePassword.value
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_eye),
                                contentDescription = null,
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_lock),
                            contentDescription = null
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xffEBEBEB),
                        focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                        unfocusedTrailingIconColor = Color.Black,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        unfocusedLeadingIconColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { newValue ->
                        if (newValue.text.length <= 20) {
                            val formattedText = newValue.text.replaceFirstChar { char ->
                                if (char.isLowerCase()) char.titlecase() else char.toString()
                            }
                            name.value = newValue.copy(text = formattedText)
                        }
                    },
                    label = {
                        Text(
                            text = "Ваше имя",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
                    },
                    isError = name.value.text.isNotEmpty() && name.value.text.length > 20,
                    supportingText = {
                        if (name.value.text.isNotEmpty() && name.value.text.length > 20) {
                            Text(
                                text = "Максимум 20 символов",
                                color = MaterialTheme.colorScheme.error,
                                fontFamily = EventsFontFamily
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                name.value = name.value.copy(
                                    selection = TextRange(0, name.value.text.length)
                                )
                            }
                        },
                    textStyle = TextStyle(
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_user),
                            contentDescription = null
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xffEBEBEB),
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        unfocusedLeadingIconColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value = surname.value,
                    onValueChange = { newValue ->
                        if (newValue.text.length <= 20) {
                            val formattedText = newValue.text.replaceFirstChar { char ->
                                if (char.isLowerCase()) char.titlecase() else char.toString()
                            }
                            surname.value = newValue.copy(text = formattedText)
                        }
                    },
                    label = {
                        Text(
                            text = "Ваша фамилия",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
                    },
                    isError = surname.value.text.isNotEmpty() && surname.value.text.length > 20,
                    supportingText = {
                        if (surname.value.text.isNotEmpty() && surname.value.text.length > 20) {
                            Text(
                                text = "Максимум 20 символов",
                                color = MaterialTheme.colorScheme.error,
                                fontFamily = EventsFontFamily
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                surname.value = surname.value.copy(
                                    selection = TextRange(0, surname.value.text.length)
                                )
                            }
                        },
                    textStyle = TextStyle(
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_user),
                            contentDescription = null
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xffEBEBEB),
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        unfocusedLeadingIconColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ваша роль:",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    UserRole.entries.forEach { role ->
                        val isSelected = selectedRole == role

                        Button(
                            onClick = {
                                selectedRole = role
                                orgCodeError = null
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                                contentColor = if (isSelected)
                                    Color.White
                                else
                                    MaterialTheme.colorScheme.secondary
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = role.label,
                                fontSize = 18.sp,
                                fontFamily = EventsFontFamily,
                                maxLines = 1
                            )
                        }
                    }
                }
                if (selectedRole != UserRole.STUDENT) {
                    OutlinedTextField(
                        value = organizationCode.value,
                        onValueChange = {
                            organizationCode.value = it
                            orgCodeError =
                                if (it != "1991" && it.isNotEmpty()) "Неверный код" else null
                        },
                        label = {
                            Text(
                                text = "Код организации",
                                fontFamily = EventsFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                            )
                        },
                        isError = orgCodeError != null,
                        supportingText = {
                            if (orgCodeError != null) {
                                Text(
                                    text = orgCodeError!!,
                                    color = MaterialTheme.colorScheme.error,
                                    fontFamily = EventsFontFamily
                                )
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        visualTransformation = if (isVisiblePassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_lock),
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    isVisiblePassword.value = !isVisiblePassword.value
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_eye),
                                    contentDescription = null,
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xffEBEBEB),
                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                            unfocusedLeadingIconColor = Color.Black,
                            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                            unfocusedTrailingIconColor = Color.Black
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isAgreed.value,
                        onCheckedChange = { isAgreed.value = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "Я согласен на обработку и хранение персональных данных",
                        fontFamily = EventsFontFamily,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Button(
                    enabled = !isLoading && isFormValid,
                    onClick = {
                        val profile = Profile(
                            id = Random.nextInt(100, 10000000),
                            name = name.value.text,
                            surname = surname.value.text,
                            email = email.value.text.trim().lowercase(),
                            password = password.value,
                            classNumber = "Не указан",
                            role = selectedRole.label,
                            imageUrl = ""
                        )
                        viewModel.registerUser(profile, organizationCode.value)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 64.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Создать аккаунт",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrationScreenNewPreview() {
    SchoolEventsTheme() {
        RegistrationScreenNew(
            onRegistrationClick = {},
            onBackClick = {}
        )
    }
}
