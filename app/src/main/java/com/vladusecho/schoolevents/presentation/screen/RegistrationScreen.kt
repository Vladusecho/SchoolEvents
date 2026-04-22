package com.vladusecho.schoolevents.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.viewModel.AuthViewModel
import kotlin.random.Random

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    email: String,
    viewModel: AuthViewModel = hiltViewModel(),
    onRegistrationClick: () -> Unit,
    onBackClick: () -> Unit
) {

    val isLoading by viewModel.isLoading.collectAsState()

    val name = remember { mutableStateOf("") }
    val surname = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val organizationCode = remember { mutableStateOf("") }

    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }

    LaunchedEffect(Unit) {
        viewModel.authResult.collect { result ->
            if (result) {
                onRegistrationClick()
            }
        }
    }

    val isFormValid = name.value.isNotBlank() &&
            surname.value.isNotBlank() &&
            password.value.isNotBlank() &&
            password.value == confirmPassword.value &&
            (if (selectedRole == UserRole.ORGANIZER || selectedRole == UserRole.DIRECTOR)
                organizationCode.value.isNotBlank() else true)



    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Создайте аккаунт",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = MaterialTheme.colorScheme.secondary,
                lineHeight = 30.sp
            )
            Text(
                text = email,
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.secondary,
                lineHeight = 30.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        EventsTextField(
            value = name.value,
            onValueChange = { input ->
                name.value = input.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            },
            prefix = {
                Row(

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_user),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            },
            placeholder = {
                Text(
                    text = "Имя",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        EventsTextField(
            value = surname.value,
            onValueChange = { input ->
                surname.value = input.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            },
            prefix = {
                Row(

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_user),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            },
            placeholder = {
                Text(
                    text = "Фамилия",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ваша роль:",
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.secondary
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
                    enabled = role != UserRole.DIRECTOR,
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
        Spacer(modifier = Modifier.height(16.dp))
        EventsTextField(
            value = password.value,
            onValueChange = { password.value = it },
            visualTransformation = PasswordVisualTransformation(),
            prefix = {
                Row(

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_lock),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            },
            placeholder = {
                Text(
                    text = "Введите пароль",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        EventsTextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            visualTransformation = PasswordVisualTransformation(),
            prefix = {
                Row(

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_lock),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            },
            placeholder = {
                Text(
                    text = "Повторите пароль",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (selectedRole == UserRole.ORGANIZER || selectedRole == UserRole.DIRECTOR) {
            EventsTextField(
                value = organizationCode.value,
                onValueChange = { organizationCode.value = it },
                visualTransformation = PasswordVisualTransformation(),
                prefix = {
                    Row(

                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_lock),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                },
                placeholder = {
                    Text(
                        text = "Код организации",
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
                }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Button(
                enabled = isFormValid,
                onClick = {
                    if (isFormValid) {
                        val profile = Profile(
                            id = Random.nextInt(100, 10000000),
                            name = name.value,
                            surname = surname.value,
                            email = email,
                            password = password.value,
                            classNumber = "Не указан",
                            role = selectedRole.label,
                            imageUrl = ""
                        )
                        viewModel.registerUser(profile)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    text = if (password.value != confirmPassword.value && confirmPassword.value.isNotEmpty())
                        "Пароли не совпадают"
                    else "Создать",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(42.dp))
    }
}

enum class UserRole(val label: String) {
    STUDENT("Ученик"),
    ORGANIZER("Организатор"),
    DIRECTOR("Директор")
}

@Preview
@Composable
private fun RegistrationScreenPreview() {
    SchoolEventsTheme() {
        RegistrationScreen(
            email = "",
            onRegistrationClick = {},
            onBackClick = {}
        )
    }
}