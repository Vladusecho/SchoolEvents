package com.vladusecho.schoolevents.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.viewModel.EventDetailsViewModel

@Composable
fun EventDetailsScreen(
    modifier: Modifier = Modifier,
    eventId: Int,
    viewModel: EventDetailsViewModel = hiltViewModel(
        creationCallback = { factory: EventDetailsViewModel.Factory ->
            factory.create(eventId)
        }
    ),
    onBackClick: () -> Unit
) {

    val state = viewModel.state.collectAsState()
    val currentState = state.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (currentState) {
            is EventDetailsViewModel.EventsDetailsState.Content -> {
                EventDetailsContent(
                    event = currentState.event,
                    onBackClick = onBackClick,
                    onFavouriteClick = { isFavourite, eventId ->
                    }
                )
            }

            is EventDetailsViewModel.EventsDetailsState.Error -> {

            }

            EventDetailsViewModel.EventsDetailsState.Initial -> {

            }

            EventDetailsViewModel.EventsDetailsState.Loading -> {

            }
        }
    }
}

@Composable
fun EventDetailsContent(
    modifier: Modifier = Modifier,
    event: Event,
    onBackClick: () -> Unit,
    onFavouriteClick: (isFavourite: Boolean, eventId: Int) -> Unit
) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = painterResource(event.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .height(240.dp)
                    .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))

            )
            Box(
                modifier = modifier
                    .padding(16.dp)
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
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
            Text(
                text = event.title,
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_date),
                "",
                tint = Color(0xff0DCDAA)
            )
            Text(
                text = event.eventDate,
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_location),
                "",
                tint = Color(0xff0DCDAA)
            )
            Text(
                text = event.address,
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "О мероприятии:",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = event.description,
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xff0DCDAA)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Записаться на мероприятие",
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun Preview() {
    SchoolEventsTheme() {
        EventDetailsContent(
            event = Event(
                id = 1,
                title = "Концерт 5opka в нашей школе! Не пропустите это невероятное событие",
                description = "Пострадав в результате несчастного случая на стриме, провинциальный стример 5opka объединяется с лысым негром под псевдонимом MellSher, чтобы отправиться в тур «1+1» по городам России и рассказать всем свою невыдуманную историю, о которой невозможно молчать.",
                address = "ул. Ленина, д.80, Актовый зал",
                eventDate = "10 июня",
                isFavourite = true,
                imageUrl = R.drawable.maxresdefault,
            ),
            onBackClick = {},
            onFavouriteClick = { isFavourite, eventId -> }
        )
    }
}