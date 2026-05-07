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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme

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
    onRegistrationClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val surname = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val organizationCode = remember { mutableStateOf("") }

    val isVisiblePassword = remember { mutableStateOf(false) }

    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }

    val focusManager = LocalFocusManager.current

    val isFormValid = name.value.isNotBlank() &&
            surname.value.isNotBlank() &&
            password.value.isNotBlank() &&
            email.value.isNotBlank() &&
            password.value == confirmPassword.value &&
            (if (selectedRole == UserRole.ORGANIZER || selectedRole == UserRole.DIRECTOR)
                organizationCode.value.isNotBlank() else true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(120.dp))
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
            contentPadding = PaddingValues(bottom = 64.dp)
        ) {
            item {
                OutlinedTextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                    },
                    label = {
                        Text(
                            text = "Почта",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
                    },
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
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_mail),
                            contentDescription = null
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        unfocusedLeadingIconColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it
                    },
                    label = {
                        Text(
                            text = "Пароль",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
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
                        focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                        unfocusedTrailingIconColor = Color.Black,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        unfocusedLeadingIconColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it
                    },
                    label = {
                        Text(
                            text = "Ваше имя",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
                    },
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
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_user),
                            contentDescription = null
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        unfocusedLeadingIconColor = Color.Black
                    )
                )
                OutlinedTextField(
                    value = surname.value,
                    onValueChange = {
                        surname.value = it
                    },
                    label = {
                        Text(
                            text = "Ваша фамилия",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        )
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
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_user),
                            contentDescription = null
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
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
                            onClick = { selectedRole = role },
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
                        },
                        label = {
                            Text(
                                text = "Код организации",
                                fontFamily = EventsFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                            )
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
                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                            unfocusedLeadingIconColor = Color.Black,
                            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                            unfocusedTrailingIconColor = Color.Black
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    enabled = isFormValid,
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
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

enum class UserRole(val label: String) {
    STUDENT("Ученик"),
    ORGANIZER("Организатор"),
    DIRECTOR("Директор")
}

@Preview
@Composable
private fun RegistrationScreenNewPreview() {
    SchoolEventsTheme() {
        RegistrationScreenNew(
            onRegistrationClick = {},
            onBackClick = {}
        )
    }
}