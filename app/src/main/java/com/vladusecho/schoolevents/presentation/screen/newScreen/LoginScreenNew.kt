package com.vladusecho.schoolevents.presentation.screen.newScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.viewModel.AuthViewModel

@Composable
fun LoginScreenNew(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit
) {
    LoginScreenContent(
        modifier = modifier,
        onLoginClick = onLoginClick,
        onBackClick = onBackClick
    )
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit
) {

    val password = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }

    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val isVisiblePassword = remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.authResult.collect { success ->
            if (success) {
                onLoginClick()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
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
                text = "Войдите в свой аккаунт",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
            )
        }
        Text(
            text = "Введите почту и пароль, чтобы продолжить",
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(40.dp))
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
            isError = emailError != null,
            supportingText = {
                if (emailError != null) {
                    Text(
                        text = emailError!!,
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
            isError = passwordError != null,
            supportingText = {
                if (passwordError != null) {
                    Text(
                        text = passwordError!!,
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
            visualTransformation = if (isVisiblePassword.value) VisualTransformation.None else PasswordVisualTransformation(),
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
        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = !isLoading && email.value.isNotBlank() && password.value.isNotBlank(),
            onClick = { viewModel.login(email.value.trim().lowercase(), password.value) },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    "Войти",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreviewNew() {
    SchoolEventsTheme() {
        LoginScreenNew(
            onLoginClick = {}, onBackClick = {}
        )
    }
}
