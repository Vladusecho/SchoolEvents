package com.vladusecho.schoolevents.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import coil3.compose.AsyncImage
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
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (currentState) {
            is EventDetailsViewModel.EventDetailsState.Content -> {
                EventDetailsContent(
                    event = currentState.event,
                    onBackClick = onBackClick,
                    onFavouriteClick = { isFavourite, eventId ->
                        viewModel.processCommand(
                            EventDetailsViewModel.EventDetailsCommand.SwitchFavouriteStatus(
                                isFavourite = isFavourite,
                                eventId = eventId
                            )
                        )
                    },
                    onSubscribeClick = { isSubscribed, eventId ->
                        viewModel.processCommand(
                            EventDetailsViewModel.EventDetailsCommand.SubscribeToEvent(
                                isSubscribed, eventId
                            )
                        )
                    }
                )
            }

            is EventDetailsViewModel.EventDetailsState.Error -> {

            }

            EventDetailsViewModel.EventDetailsState.Initial -> {

            }

            EventDetailsViewModel.EventDetailsState.Loading -> {

            }
        }
    }
}

@Composable
fun EventDetailsContent(
    modifier: Modifier = Modifier,
    event: Event,
    onBackClick: () -> Unit,
    onFavouriteClick: (isFavourite: Boolean, eventId: Int) -> Unit,
    onSubscribeClick: (isSubscribed: Boolean ,eventId: Int) -> Unit
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 128.dp),
    ) {
        item {
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                        .fillMaxWidth()

                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp)
            ) {
                Text(
                    text = event.title,
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
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
                Column(

                ) {
                    Text(
                        text = event.eventDate,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = event.eventDuration,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
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
                Column() {
                    Text(
                        text = event.eventPlace,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = event.eventAddress,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Организатор:",
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Милютина Виктория",
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "О мероприятии:",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.secondary
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
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 108.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))

        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .size(128.dp)
                )
            }
            Button(
                onClick = { onSubscribeClick(event.isSubscribed, event.id) },
                colors = ButtonDefaults.buttonColors(
                    containerColor =  if (event.isSubscribed) Color.Red else MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = if (!event.isSubscribed) "Посетить" else "Не пойду",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
            IconButton(onClick = { onFavouriteClick(event.isFavourite, event.id) }) {
                Icon(
                    imageVector =
                        ImageVector.vectorResource(R.drawable.ic_not_fav),
                    contentDescription = null,
                    tint = if (event.isFavourite) Color.Red else Color.White,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(8.dp)
                        .size(128.dp)
                )
            }
        }
    }
}


@Composable
@Preview(
    showBackground = true
)
fun Preview() {
    SchoolEventsTheme(
        darkTheme = true
    ) {
        EventDetailsContent(
            event = Event(
                id = 1,
                title = "Концерт 5opka в нашей школе! Не пропустите это невероятное событие",
                description = "Пострадав в результате несчастного случая на стриме, провинциальный стример 5opka объединяется с лысым негром под псевдонимом MellSher, чтобы отправиться в тур «1+1» по городам России и рассказать всем свою невыдуманную историю, о которой невозможно молчать. 1+1 = 11 городов. Победители всех музыкальных премий, авторы хитов «XXL» и «Мерси», люди, которые не нуждаются в представлении, но мы их все равно представили, в твоём городе. Приходи на их самые большие концерты или будешь жалеть всю жизнь!",
                eventAddress = "ул. Ленина, д.80, Актовый зал",
                eventDate = "10 июня",
                isFavourite = false,
                eventPlace = "Актовый зал",
                eventDuration = "Вторник, 8:00 - 13:00",
                isSubscribed = true,
                imageUrl = "",
                isArchived = false
            ),
            onBackClick = {},
            onFavouriteClick = { isFavourite, eventId -> },
            onSubscribeClick = { isSubscribed, eventId -> }
        )
    }
}