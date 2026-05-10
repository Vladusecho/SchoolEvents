package com.vladusecho.schoolevents.presentation.screen.newScreen

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme

@Composable
fun StartAppScreenNew(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onRegistrationClick: () -> Unit
) {
    StartAppContent(
        modifier = modifier,
        onLoginClick = onLoginClick,
        onRegistrationClick = onRegistrationClick
    )
}

@Composable
fun StartAppContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onRegistrationClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(120.dp))
        Text(
            text = "Внеурочная деятельность",
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 46.sp,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(R.drawable.img_startapp),
            contentDescription = "",
            modifier = Modifier.size(320.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Не пропусти главное в школьной жизни.\n" +
                    "Мероприятия и новости — всё в одном месте.",
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onRegistrationClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xff008A00)
            )
        ) {
            Text(
                "Создать аккаунт",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
        }
        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = Color.Black
            )
        ) {
            Row(

            ) {
                Text(
                    "Есть аккаунт?",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.tertiary

                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Войти",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary

                )
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview(
    showBackground = true
)
@Composable
fun StartPreviewNew() {
    SchoolEventsTheme(
        darkTheme = true
    ) {
        StartAppScreenNew(
            onLoginClick = {},
            onRegistrationClick = {}
        )
    }
}