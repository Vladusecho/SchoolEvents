package com.vladusecho.schoolevents.presentation.entity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.vladusecho.schoolevents.domain.entity.News
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme

@Composable
fun NewsCard(
    modifier: Modifier = Modifier,
    news: News,
    onNewsClick: (newsId: Int) -> Unit = {}
) {
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
                    onNewsClick(news.id)
                },
        ) {
            Box() {
                if (news.imageUrls.isNotEmpty()) {
                    AsyncImage(
                        model = news.imageUrls.first(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .height(150.dp)
                            .fillMaxWidth()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onBackground)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = news.date,
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Column(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = news.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = news.description,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun NewsCardPreview() {
    SchoolEventsTheme() {
        NewsCard(
            news = News(
                id = 1,
                title = "Школьные новости",
                description = "Описание школьной новости",
                imageUrls = listOf(""),
                date = "12.02.2023"
            )
        )
    }
}
