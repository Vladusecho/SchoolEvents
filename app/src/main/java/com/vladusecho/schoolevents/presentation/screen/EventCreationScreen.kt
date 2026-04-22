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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import com.vladusecho.schoolevents.presentation.viewModel.EventCreationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCreationScreen(
    modifier: Modifier = Modifier,
    viewModel: EventCreationViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is EventCreationViewModel.EventCreationState.Saved) {
            onBackClick()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val currentState = state) {
            is EventCreationViewModel.EventCreationState.Content -> {
                EventCreationContent(
                    organizerName = currentState.organizerName,
                    onBackClick = onBackClick,
                    onSaveClick = { event ->
                        viewModel.createEvent(event)
                    }
                )
            }
            is EventCreationViewModel.EventCreationState.Error -> {
                Text(text = currentState.message, color = Color.Red, modifier = Modifier.align(Alignment.Center))
            }
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventCreationContent(
    organizerName: String,
    onBackClick: () -> Unit,
    onSaveClick: (Event) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }
    var durationStart by remember { mutableStateOf("") }
    var durationEnd by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePickerStart by remember { mutableStateOf(false) }
    var showTimePickerEnd by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerStateStart = rememberTimePickerState()
    val timePickerStateEnd = rememberTimePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = Date(it)
                        val format = SimpleDateFormat("d MMMM", Locale("ru"))
                        dateText = format.format(date)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePickerStart) {
        TimePickerDialog(
            onDismissRequest = { showTimePickerStart = false },
            onConfirm = {
                durationStart = String.format("%02d:%02d", timePickerStateStart.hour, timePickerStateStart.minute)
                showTimePickerStart = false
            }
        ) {
            TimePicker(state = timePickerStateStart)
        }
    }

    if (showTimePickerEnd) {
        TimePickerDialog(
            onDismissRequest = { showTimePickerEnd = false },
            onConfirm = {
                durationEnd = String.format("%02d:%02d", timePickerStateEnd.hour, timePickerStateEnd.minute)
                showTimePickerEnd = false
            }
        ) {
            TimePicker(state = timePickerStateEnd)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl.isNotEmpty()) {
                    AsyncImage(model = imageUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                } else {
                    IconButton(onClick = { /* Логика выбора фото */ }) {
                        Icon(painterResource(R.drawable.ic_archive_screen), null, modifier = Modifier.size(48.dp))
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
                    placeholder = "Название мероприятия",
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(fontFamily = EventsFontFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date and Time
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(painterResource(R.drawable.ic_date), null, tint = Color(0xFF0DCDAA))
                }
                Text(text = if(dateText.isEmpty()) "Дата" else dateText, modifier = Modifier.width(100.dp), fontFamily = EventsFontFamily)
                
                Button(onClick = { showTimePickerStart = true }, colors = ButtonDefaults.buttonColors(containerColor = Color.White), shape = RoundedCornerShape(8.dp)) {
                    Text(text = if(durationStart.isEmpty()) "От" else durationStart, color = Color.Black, fontFamily = EventsFontFamily)
                }
                Text("-")
                Button(onClick = { showTimePickerEnd = true }, colors = ButtonDefaults.buttonColors(containerColor = Color.White), shape = RoundedCornerShape(8.dp)) {
                    Text(text = if(durationEnd.isEmpty()) "До" else durationEnd, color = Color.Black, fontFamily = EventsFontFamily)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Location
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_location), null, modifier = Modifier.size(24.dp), tint = Color(0xFF0DCDAA))
                EditField(value = place, onValueChange = { place = it }, placeholder = "Место", modifier = Modifier.weight(1f))
                EditField(value = address, onValueChange = { address = it }, placeholder = "Адрес", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = organizerName, fontWeight = FontWeight.Bold, fontFamily = EventsFontFamily)
                Text(text = "Организатор", fontSize = 12.sp, color = Color.Gray, fontFamily = EventsFontFamily)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "О мероприятии:", modifier = Modifier.padding(horizontal = 16.dp), fontWeight = FontWeight.Bold, fontFamily = EventsFontFamily)
            
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(150.dp).clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                placeholder = { Text("Описание мероприятия...") }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = onBackClick, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.Red), shape = RoundedCornerShape(12.dp)) {
                    Text("ОТМЕНИТЬ", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {
                        onSaveClick(
                            Event(
                                id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                                title = title,
                                description = description,
                                eventAddress = address,
                                eventPlace = place,
                                eventDate = dateText,
                                eventDuration = "$durationStart - $durationEnd",
                                imageUrl = imageUrl,
                                isArchived = false,
                                isFavourite = false,
                                isSubscribed = false
                            )
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0DCDAA)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("СОЗДАТЬ", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = { Button(onClick = onConfirm) { Text("OK") } }
    ) {
        content()
    }
}

@Composable
fun EditField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(fontFamily = EventsFontFamily)
) {
    Box(modifier = modifier.clip(RoundedCornerShape(8.dp)).background(Color.White).padding(8.dp)) {
        if (value.isEmpty()) {
            Text(text = placeholder, style = textStyle.copy(color = Color.Gray))
        }
        BasicTextField(value = value, onValueChange = onValueChange, textStyle = textStyle, modifier = Modifier.fillMaxWidth())
    }
}
