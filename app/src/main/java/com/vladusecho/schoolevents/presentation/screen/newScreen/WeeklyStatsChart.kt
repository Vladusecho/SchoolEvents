package com.vladusecho.schoolevents.presentation.screen.newScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.viewModel.ProfileViewModel

@Composable
fun WeeklyStatsChart(
    stats: List<ProfileViewModel.DayStat>,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val maxCount = (stats.maxOfOrNull { maxOf(it.eventsCount, it.newsCount) } ?: 0).coerceAtLeast(5)
    
    val eventColor = Color(0xFF4CAF50) // Зеленый для ивентов
    val newsColor = Color(0xFF2196F3)  // Синий для новостей
    val labelColor = MaterialTheme.colorScheme.tertiary

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Активность за неделю",
            fontFamily = EventsFontFamily,
            fontSize = 18.sp,
            color = labelColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val paddingBottom = 30.dp.toPx()
            val chartHeight = canvasHeight - paddingBottom
            
            val barWidth = 12.dp.toPx()
            val spaceBetweenDays = (canvasWidth) / stats.size
            
            stats.forEachIndexed { index, dayStat ->
                val xCenter = index * spaceBetweenDays + spaceBetweenDays / 2
                
                // Draw Events Bar
                val eventBarHeight = (dayStat.eventsCount.toFloat() / maxCount) * chartHeight
                drawRoundRect(
                    color = eventColor,
                    topLeft = Offset(xCenter - barWidth - 2.dp.toPx(), chartHeight - eventBarHeight),
                    size = Size(barWidth, eventBarHeight),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
                
                // Draw News Bar
                val newsBarHeight = (dayStat.newsCount.toFloat() / maxCount) * chartHeight
                drawRoundRect(
                    color = newsColor,
                    topLeft = Offset(xCenter + 2.dp.toPx(), chartHeight - newsBarHeight),
                    size = Size(barWidth, newsBarHeight),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
                
                // Draw Day Label
                val textLayoutResult = textMeasurer.measure(
                    text = dayStat.day,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 10.sp,
                        fontFamily = EventsFontFamily,
                        color = labelColor
                    )
                )
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(
                        xCenter - textLayoutResult.size.width / 2,
                        chartHeight + 8.dp.toPx()
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            LegendItem(color = eventColor, label = "Ивенты")
            Spacer(modifier = Modifier.width(16.dp))
            LegendItem(color = newsColor, label = "Новости")
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontFamily = EventsFontFamily,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}
