package com.vladusecho.schoolevents.presentation.entity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
    onFavouriteClick: (isFavourite: Boolean, eventId: Int) -> Unit,
    onEventClick: (eventId: Int) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(10.dp, RoundedCornerShape(20.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
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
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Row() {
                    Text(
                        text = event.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                }
                Row() {
                    Text(
                        text = event.description,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        lineHeight = 16.sp
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_event_place),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)

                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = event.address,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
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
                    .clickable {
                        onFavouriteClick(
                            event.isFavourite,
                            event.id
                        )
                    }
                    .background(Color.White.copy(alpha = 0.5f))
                    .size(42.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (!event.isFavourite) {
                        ImageVector.vectorResource(R.drawable.ic_not_fav)
                    } else {
                        ImageVector.vectorResource(R.drawable.ic_is_fav)
                    },
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
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
            onEventClick = {},
            onFavouriteClick = {
                isFav, eventId ->
                println("isFav: $isFav, eventId: $eventId")
            }
        )
    }
}