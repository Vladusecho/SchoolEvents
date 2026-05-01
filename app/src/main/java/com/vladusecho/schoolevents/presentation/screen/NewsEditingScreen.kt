package com.vladusecho.schoolevents.presentation.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.News
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.viewModel.NewsEditingViewModel

@Composable
fun NewsEditingScreen(
    modifier: Modifier = Modifier,
    newsId: Int,
    viewModel: NewsEditingViewModel = hiltViewModel(
        creationCallback = { factory: NewsEditingViewModel.Factory ->
            factory.create(newsId)
        }
    ),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is NewsEditingViewModel.NewsEditingState.Saved) {
            onBackClick()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val currentState = state) {
            is NewsEditingViewModel.NewsEditingState.Content -> {
                NewsEditingContent(
                    news = currentState.news,
                    organizerName = currentState.organizerName,
                    onBackClick = onBackClick,
                    onSaveClick = { updatedNews, uris ->
                        viewModel.updateNews(updatedNews, uris)
                    },
                    onDeleteClick = {
                        viewModel.deleteNews()
                    }
                )
            }

            is NewsEditingViewModel.NewsEditingState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is NewsEditingViewModel.NewsEditingState.Error -> {
                Text(
                    text = currentState.message,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }

            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsEditingContent(
    news: News,
    organizerName: String,
    onBackClick: () -> Unit,
    onSaveClick: (News, List<String>) -> Unit,
    onDeleteClick: () -> Unit
) {
    var title by remember { mutableStateOf(news.title) }
    var description by remember { mutableStateOf(news.description) }
    val imageUris = remember { mutableStateListOf<String>().apply { addAll(news.imageUrls) } }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)
    ) { uris ->
        imageUris.addAll(uris.map { it.toString() })
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 156.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (imageUris.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_archive_screen),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Gray
                        )
                        Text("Добавить фото", color = Color.Gray, fontFamily = EventsFontFamily)
                    }
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(imageUris) { uri ->
                            Box {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(180.dp, 220.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                )
                                IconButton(
                                    onClick = { imageUris.remove(uri) },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                        .size(24.dp)
                                ) {
                                    Text("✕", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable {
                                        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(painterResource(R.drawable.ic_back), contentDescription = null)
                }
                EditField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Заголовок новости",
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_date),
                    "",
                    tint = Color(0xff0DCDAA)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    enabled = false
                ) {
                    Text(
                        text = news.date,
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = EventsFontFamily,
                        fontSize = 18.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
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
                    text = organizerName,
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Текст новости:",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
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
                onClick = { onDeleteClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "УДАЛИТЬ",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
            IconButton(onClick = {
                onSaveClick(
                    news.copy(
                        title = title,
                        description = description,
                        imageUrls = imageUris.toList()
                    ),
                    imageUris.toList()
                )
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
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
