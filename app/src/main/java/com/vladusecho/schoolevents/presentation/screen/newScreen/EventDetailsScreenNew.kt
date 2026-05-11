package com.vladusecho.schoolevents.presentation.screen.newScreen

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.EventStatus
import com.vladusecho.schoolevents.domain.entity.Vote
import com.vladusecho.schoolevents.presentation.activity.LocalUserRole
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.util.UserRole
import com.vladusecho.schoolevents.presentation.viewModel.EventDetailsViewModel

@Composable
fun EventDetailsScreenNew(
    modifier: Modifier = Modifier,
    eventId: Int,
    viewModel: EventDetailsViewModel = hiltViewModel(
        creationCallback = { factory: EventDetailsViewModel.Factory ->
            factory.create(eventId)
        }
    ),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is EventDetailsViewModel.EventDetailsState.Deleted) {
            onBackClick()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val currentState = state) {
            is EventDetailsViewModel.EventDetailsState.Content -> {
                EventDetailsContent(
                    event = currentState.event,
                    organizerName = currentState.organizerName,
                    onBackClick = onBackClick,
                    onFavouriteClick = { isFavourite, id ->
                        viewModel.processCommand(
                            EventDetailsViewModel.EventDetailsCommand.SwitchFavouriteStatus(
                                isFavourite,
                                id
                            )
                        )
                    },
                    onSubscribeClick = { isSubscribed, id ->
                        viewModel.processCommand(
                            EventDetailsViewModel.EventDetailsCommand.SubscribeToEvent(
                                isSubscribed,
                                id
                            )
                        )
                    },
                    onApproveClick = {
                        viewModel.processCommand(EventDetailsViewModel.EventDetailsCommand.ApproveEvent)
                    },
                    onRejectClick = {
                        viewModel.processCommand(EventDetailsViewModel.EventDetailsCommand.RejectEvent)
                    },
                    onDeleteClick = {
                        viewModel.processCommand(EventDetailsViewModel.EventDetailsCommand.DeleteEvent)
                    },
                    onVoteClick = { vote ->
                        viewModel.processCommand(EventDetailsViewModel.EventDetailsCommand.VoteEvent(vote))
                    }
                )
            }

            is EventDetailsViewModel.EventDetailsState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xff008A00)
                )
            }

            is EventDetailsViewModel.EventDetailsState.Error -> {
                Text(
                    text = currentState.message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center),
                    fontFamily = EventsFontFamily
                )
            }

            else -> {}
        }
    }
}

@Composable
private fun EventDetailsContent(
    event: Event,
    organizerName: String,
    onBackClick: () -> Unit,
    onFavouriteClick: (Boolean, Int) -> Unit,
    onSubscribeClick: (Boolean, Int) -> Unit,
    onApproveClick: () -> Unit,
    onRejectClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onVoteClick: (Vote) -> Unit
) {
    val userRole = LocalUserRole.current
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.background)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    if (event.imageUrls.isNotEmpty()) {
                        val pagerState = rememberPagerState(pageCount = { event.imageUrls.size })
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            AsyncImage(
                                model = event.imageUrls[page],
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        if (event.imageUrls.size > 1) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(event.imageUrls.size) { iteration ->
                                    val color =
                                        if (pagerState.currentPage == iteration) Color.White else Color.White.copy(
                                            alpha = 0.5f
                                        )
                                    Box(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .size(8.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xffF2F2F2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_archive_screen),
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Gray
                            )
                        }
                    }

                    // Navigation Overlay
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_back),
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        if (userRole == UserRole.STUDENT) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                IconButton(onClick = {
                                    onFavouriteClick(
                                        event.isFavourite,
                                        event.id
                                    )
                                }) {
                                    Icon(
                                        painter = painterResource(if (event.isFavourite) R.drawable.ic_is_fav else R.drawable.ic_not_fav),
                                        contentDescription = "Favourite",
                                        tint = if (event.isFavourite) Color.Red else Color.Black,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = event.title,
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.weight(1f)
                        )
                        
                        VoteSection(
                            likes = event.likes,
                            dislikes = event.dislikes,
                            userVote = event.userVote,
                            onVoteClick = onVoteClick
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(modifier = Modifier.animateContentSize()) {
                        Text(
                            text = event.description,
                            fontFamily = EventsFontFamily,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 3,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 22.sp
                        )
                        if (event.description.length > 120) {
                            Text(
                                text = if (isDescriptionExpanded) "Скрыть" else "Читать больше...",
                                fontFamily = EventsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Organizer Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_user),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Организатор",
                                fontFamily = EventsFontFamily,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = organizerName,
                                fontFamily = EventsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Time Row
                    DetailInfoRow(
                        iconRes = R.drawable.ic_date,
                        text = event.eventDuration
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Date Row
                    DetailInfoRow(
                        iconRes = R.drawable.ic_calendar,
                        text = event.eventDate
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Location Row
                    DetailInfoRow(
                        iconRes = R.drawable.ic_location,
                        text = "${event.eventPlace}, ${event.eventAddress}"
                    )
                }
            }
            item {
                if (event.isArchived) {
                    Button(
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            "УДАЛИТЬ НАВСЕГДА",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                } else if (userRole == UserRole.DIRECTOR && event.status == EventStatus.PENDING) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onRejectClick,
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffFF3B30)),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Text(
                                "Отклонить",
                                fontFamily = EventsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        Button(
                            onClick = onApproveClick,
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff008A00)),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Text(
                                "Утвердить",
                                fontFamily = EventsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                } else {
                    val isSubscribed = event.isSubscribed
                    val isStudent = userRole == UserRole.STUDENT

                    if (isStudent && event.status == EventStatus.APPROVED) {
                        Button(
                            onClick = { onSubscribeClick(isSubscribed, event.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSubscribed) Color.LightGray else Color(
                                    0xff008A00
                                ),
                                contentColor = if (isSubscribed) Color.Black else Color.White
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            val text = if (isSubscribed) "Отписаться" else "Записаться"
                            Text(
                                text = text,
                                fontFamily = EventsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .height(136.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun DetailInfoRow(iconRes: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEventDetails() {
    SchoolEventsTheme(
        darkTheme = true
    ) {
        EventDetailsContent(
            event = Event(
                id = 1,
                title = "Founders Meet and greet",
                description = "Here's a place for the Even description... Founders gather to connect and discuss this topic in detail to find common ground.",
                eventAddress = "NO 24 Ajah lekki Epe, Lagos Nigeria",
                eventPlace = "School Hall",
                eventDate = "May 23 - May 24",
                eventDuration = "2:00 PM - 4:00 PM",
                isArchived = false,
                isFavourite = true,
                isSubscribed = false,
                creatorEmail = "test@test.com",
                likes = 10,
                dislikes = 2,
                userVote = Vote.LIKE
            ),
            organizerName = "Sponsor name",
            onBackClick = {},
            onFavouriteClick = { _, _ -> },
            onSubscribeClick = { _, _ -> },
            onApproveClick = {},
            onRejectClick = {},
            onDeleteClick = {},
            onVoteClick = {}
        )
    }
}
