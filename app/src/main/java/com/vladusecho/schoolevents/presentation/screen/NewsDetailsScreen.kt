package com.vladusecho.schoolevents.presentation.screen

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.News
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.viewModel.NewsDetailsViewModel

@Composable
fun NewsDetailsScreen(
    modifier: Modifier = Modifier,
    newsId: Int,
    viewModel: NewsDetailsViewModel = hiltViewModel(
        creationCallback = { factory: NewsDetailsViewModel.Factory ->
            factory.create(newsId)
        }
    ),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val currentState = state) {
            is NewsDetailsViewModel.NewsDetailsState.Content -> {
                NewsDetailsContent(
                    news = currentState.news,
                    organizerName = currentState.organizerName,
                    onBackClick = onBackClick
                )
            }

            is NewsDetailsViewModel.NewsDetailsState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is NewsDetailsViewModel.NewsDetailsState.Error -> {
                Text(
                    text = currentState.message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {}
        }
    }
}

@Composable
private fun NewsDetailsContent(
    news: News,
    organizerName: String,
    onBackClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            ) {
                if (news.imageUrls.isNotEmpty()) {
                    val pagerState = rememberPagerState(pageCount = { news.imageUrls.size })
                    
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = news.imageUrls[page],
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    
                    if (news.imageUrls.size > 1) {
                        Row(
                            Modifier
                                .height(50.dp)
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(news.imageUrls.size) { iteration ->
                                val color = if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
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
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_archive_screen),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(8.dp)
                ) {
                    Text(
                        text = news.title,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_date),
                        contentDescription = null,
                        tint = Color(0xFF0DCDAA)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = news.date,
                        fontFamily = EventsFontFamily,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Автор:",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = organizerName,
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = news.description,
                    fontFamily = EventsFontFamily,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsPreview() {
    SchoolEventsTheme(
        darkTheme = false
    ) {
        NewsDetailsScreen(
            newsId = 1,
            onBackClick = {}
        )
    }
}
