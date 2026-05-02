package com.vladusecho.schoolevents.presentation.entity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.presentation.activity.LocalUserRole
import com.vladusecho.schoolevents.presentation.screen.UserRole
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme

@Composable
fun StudentEventCard(
    modifier: Modifier = Modifier,
    event: Event,
    onFavouriteClick: (isFavourite: Boolean, eventId: Int) -> Unit = { _, _ -> },
    onListClick: (eventId: Int) -> Unit = {},
    onEventClick: (eventId: Int) -> Unit
) {

    val role = LocalUserRole.current
    val isNotStudent = role != UserRole.STUDENT

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(2.dp, RoundedCornerShape(20.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.onBackground)
                .clickable {
                    onEventClick(event.id)
                },
        ) {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .height(150.dp)
                    .fillMaxWidth()
            )
            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Row {
                    Text(
                        text = event.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Row {
                    Text(
                        text = event.description,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
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
                        text = event.eventAddress,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
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
                    .border(1.dp, MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(8.dp)
            ) {
                Text(
                    text = event.eventDate,
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Column(
                modifier = Modifier.padding(end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = modifier
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            if (isNotStudent) {
                                onListClick(event.id)
                            } else {
                                onFavouriteClick(
                                    event.isFavourite,
                                    event.id
                                )
                            }
                        }
                        .background(MaterialTheme.colorScheme.background)
                        .size(42.dp)
                        .border(1.dp, MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = if (isNotStudent) {
                        ImageVector.vectorResource(R.drawable.ic_users)
                    } else {
                        ImageVector.vectorResource(R.drawable.ic_not_fav)
                    }
                    
                    val tint = if (isNotStudent) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        if (event.isFavourite) Color.Red else Color.Gray
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )
                }
                if (event.isSubscribed) {
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.onBackground)
                            .size(42.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_calendar),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun StudentEventCardPreview() {
    SchoolEventsTheme(
        darkTheme = false
    ) {
        StudentEventCard(
            event = Event(
                id = 1,
                title = "Концерт 5opka в нашей школе!",
                description = "Описание...",
                eventAddress = "ул. Ленина, д.80",
                eventPlace = "Актовый зал",
                eventDate = "10 июня",
                eventDuration = "8:00 - 13:00",
                isArchived = false,
                isFavourite = false,
                isSubscribed = false,
                creatorEmail = "",
                imageUrls = emptyList()
            ),
            onEventClick = {},
            onFavouriteClick = { _, _ -> }
        )
    }
}
