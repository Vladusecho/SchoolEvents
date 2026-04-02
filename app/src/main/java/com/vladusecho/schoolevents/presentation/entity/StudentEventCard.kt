package com.vladusecho.schoolevents.presentation.entity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme

@Composable
fun StudentEventCard(
    modifier: Modifier = Modifier,
    event: Event,
    onEventClick: (eventId: Int) -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .fillMaxWidth()
            .height(290.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.maxresdefault),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .height(118.dp)
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Row() {
                    Text(
                        text = event.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
                Row() {
                    Text(
                        text = event.description,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    )
                }
                Row() {
                    Text(
                        text = event.address,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp,
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.5f))
                    .padding(8.dp)
            ) {
                Text(
                    text = event.eventDate,
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Box(
                modifier = modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.5f))
                    .size(42.dp)
                    .padding(8.dp)
            ) {
                IconButton({}) {
                    Icon(
                        imageVector = if (!event.isFavourite) {
                            ImageVector.vectorResource(R.drawable.ic_not_fav)
                        } else {
                            ImageVector.vectorResource(R.drawable.ic_is_fav)                        },
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun StudentEventCardPreview() {
    SchoolEventsTheme() {
        StudentEventCard(
            event = Event(
                id = 1,
                title = "Концерт 5opka в нашей школе! Не пропустите это невероятное событие",
                description = "Пострадав в результате несчастного случая на стриме, провинциальный стример 5opka объединяется с лысым негром под псевдонимом MellSher, чтобы отправиться в тур «1+1» по городам России и рассказать всем свою невыдуманную историю, о которой невозможно молчать.",
                eventDate = "10 июня",
                address = "ул. Ленина, д.80, Актовый зал",
                isFavourite = true
            ),
            onEventClick = {}
        )
    }
}