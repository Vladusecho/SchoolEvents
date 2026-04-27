package com.vladusecho.schoolevents.presentation.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.viewModel.EventEditingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                    organizerName = currentState.organizerName,
                    onBackClick = onBackClick,
                    onSaveClick = { updatedEvent, uri ->
                        viewModel.updateEvent(updatedEvent, uri)
                    },
                    onArchiveClick = {
                        viewModel.updateEvent(currentState.event.copy(isArchived = true), null)
                    }
                )
            }

            is EventEditingViewModel.EventEditingState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is EventEditingViewModel.EventEditingState.Error -> {
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
private fun EventEditingContent(
    event: Event,
    organizerName: String,
    onBackClick: () -> Unit,
    onSaveClick: (Event, String?) -> Unit,
    onArchiveClick: () -> Unit
) {
    var title by remember { mutableStateOf(event.title) }
    var dateText by remember { mutableStateOf(event.eventDate) }
    var durationStart by remember {
        mutableStateOf(
            event.eventDuration.split(" - ").getOrElse(0) { "" })
    }
    var durationEnd by remember {
        mutableStateOf(
            event.eventDuration.split(" - ").getOrElse(1) { "" })
    }
    var place by remember { mutableStateOf(event.eventPlace) }
    var address by remember { mutableStateOf(event.eventAddress) }
    var description by remember { mutableStateOf(event.description) }
    var imageUrl by remember { mutableStateOf(event.imageUrl) }
    var newImageUri by remember { mutableStateOf<String?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePickerStart by remember { mutableStateOf(false) }
    var showTimePickerEnd by remember { mutableStateOf(false) }

    val sdf = SimpleDateFormat("d MMMM", Locale("ru"))
    val initialDate = try {
        sdf.parse(dateText)?.time
    } catch (e: Exception) {
        null
    }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate)

    val startParts = durationStart.split(":")
    val timePickerStateStart = rememberTimePickerState(
        initialHour = startParts.getOrNull(0)?.toIntOrNull() ?: 0,
        initialMinute = startParts.getOrNull(1)?.toIntOrNull() ?: 0
    )

    val endParts = durationEnd.split(":")
    val timePickerStateEnd = rememberTimePickerState(
        initialHour = endParts.getOrNull(0)?.toIntOrNull() ?: 0,
        initialMinute = endParts.getOrNull(1)?.toIntOrNull() ?: 0
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            imageUrl = it.toString()
            newImageUri = it.toString()
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = Date(it)
                        dateText = sdf.format(date)
                    }
                    showDatePicker = false
                }) {
                    Text(
                        "OK",
                        fontWeight = FontWeight.Bold,
                        fontFamily = EventsFontFamily,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContentColor = MaterialTheme.colorScheme.secondary,
                    todayContentColor = MaterialTheme.colorScheme.secondary,
                    todayDateBorderColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }

    if (showTimePickerStart) {
        CustomTimePickerDialog(
            onDismissRequest = { showTimePickerStart = false },
            onConfirm = {
                durationStart = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    timePickerStateStart.hour,
                    timePickerStateStart.minute
                )
                showTimePickerStart = false
            }
        ) {
            TimePicker(state = timePickerStateStart)
        }
    }

    if (showTimePickerEnd) {
        CustomTimePickerDialog(
            onDismissRequest = { showTimePickerEnd = false },
            onConfirm = {
                durationEnd = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    timePickerStateEnd.hour,
                    timePickerStateEnd.minute
                )
                showTimePickerEnd = false
            }
        ) {
            TimePicker(state = timePickerStateEnd)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 156.dp)
    ) {
        item {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                    .clickable {
                        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EditField(
                    value = title,
                    placeholder = "Заголовок мероприятия",
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
                    onClick = { showDatePicker = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = dateText,
                        color = Color.Black,
                        fontFamily = EventsFontFamily,
                        fontSize = 18.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
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
                    onClick = { showTimePickerStart = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = durationStart,
                        color = Color.Black,
                        fontFamily = EventsFontFamily,
                        fontSize = 18.sp
                    )
                }
                Text("-")
                Button(
                    onClick = { showTimePickerEnd = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = durationEnd,
                        color = Color.Black,
                        fontFamily = EventsFontFamily,
                        fontSize = 18.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    "",
                    tint = Color(0xff0DCDAA)
                )
                EditField(
                    value = place,
                    onValueChange = { place = it },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    "",
                    tint = Color(0xff0DCDAA)
                )
                EditField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                    text = "О мероприятии:",
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
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
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
                onClick = { onArchiveClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "В АРХИВ",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
            IconButton(onClick = {
                onSaveClick(
                    event.copy(
                        title = title,
                        eventDate = dateText,
                        eventDuration = "$durationStart - $durationEnd",
                        eventPlace = place,
                        eventAddress = address,
                        description = description,
                        imageUrl = imageUrl
                    ),
                    newImageUri
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

@Composable
fun EditField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(fontFamily = EventsFontFamily, fontSize = 18.sp)
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
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun EditPreview() {
    SchoolEventsTheme(
        darkTheme = true
    ) {
        EventEditingContent(
            event = Event(
                id = 1,
                title = "Школьные мероприятия",
                description = "Школьные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятияШкольные мероприятия",
                imageUrl = "null",
                eventAddress = "ул. Ленина, д.80, Актовый зал",
                eventDate = "12 марта",
                eventPlace = "ул. Ленина, д.80, Актовый зал",
                eventDuration = "8:00 - 13:00",
                isArchived = false,
                isFavourite = false,
                isSubscribed = false
            ),
            organizerName = "Иванов Иван Иванович",
            onBackClick = {},
            onSaveClick = { _, _ -> },
            onArchiveClick = {}
        )
    }
}
