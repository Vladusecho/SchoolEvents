package com.vladusecho.schoolevents.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily

@Composable
fun FavouriteScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "В разработке...",
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.Red,
        )
    }
}