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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.viewModel.EventEditingViewModel

@Composable
fun EventEditingScreen(
    modifier: Modifier = Modifier,
    eventId: Int,
    viewModel: EventEditingViewModel = hiltViewModel(
        creationCallback = { factory: EventEditingViewModel.Factory ->
            factory.create(eventId)
        }
    ),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is EventEditingViewModel.EventEditingState.Saved) {
            onBackClick()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val currentState = state) {
            is EventEditingViewModel.EventEditingState.Content -> {
                EventEditingContent(
                    event = currentState.event,
                    onBackClick = onBackClick,
                    onSaveClick = { updatedEvent ->
                        viewModel.updateEvent(updatedEvent)
                    }
                )
            }
            is EventEditingViewModel.EventEditingState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is EventEditingViewModel.EventEditingState.Error -> {
                Text(text = currentState.message, modifier = Modifier.align(Alignment.Center), color = Color.Red)
            }
            else -> {}
        }
    }
}

@Composable
private fun EventEditingContent(
    event: Event,
    onBackClick: () -> Unit,
    onSaveClick: (Event) -> Unit
) {
    var title by remember { mutableStateOf(event.title) }
    var date by remember { mutableStateOf(event.eventDate) }
    var durationStart by remember { mutableStateOf(event.eventDuration.split(" - ").getOrElse(0) { "" }) }
    var durationEnd by remember { mutableStateOf(event.eventDuration.split(" - ").getOrElse(1) { "" }) }
    var place by remember { mutableStateOf(event.eventPlace) }
    var address by remember { mutableStateOf(event.eventAddress) }
    var description by remember { mutableStateOf(event.description) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            )
            
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
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(
                        fontFamily = EventsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date and Time
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_date), null, modifier = Modifier.size(24.dp), tint = Color.Gray)
                EditField(value = date, onValueChange = { date = it }, modifier = Modifier.width(100.dp))
                EditField(value = durationStart, onValueChange = { durationStart = it }, modifier = Modifier.weight(1f))
                Text("-")
                EditField(value = durationEnd, onValueChange = { durationEnd = it }, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Location
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_location), null, modifier = Modifier.size(24.dp), tint = Color.Gray)
                EditField(value = place, onValueChange = { place = it }, modifier = Modifier.weight(1f))
                EditField(value = address, onValueChange = { address = it }, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = "Милютина Виктория", fontWeight = FontWeight.Bold, fontFamily = EventsFontFamily)
                Text(text = "Организатор", fontSize = 12.sp, color = Color.Gray, fontFamily = EventsFontFamily)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "О мероприятии:",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontWeight = FontWeight.Bold,
                fontFamily = EventsFontFamily
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("ОТМЕНИТЬ", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {
                        onSaveClick(
                            event.copy(
                                title = title,
                                eventDate = date,
                                eventDuration = "$durationStart - $durationEnd",
                                eventPlace = place,
                                eventAddress = address,
                                description = description
                            )
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0DCDAA)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("СОХРАНИТЬ", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EditField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(fontFamily = EventsFontFamily)
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
