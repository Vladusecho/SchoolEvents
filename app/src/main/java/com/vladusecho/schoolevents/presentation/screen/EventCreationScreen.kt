package com.vladusecho.schoolevents.presentation.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.TimePickerDefaults
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
import com.vladusecho.schoolevents.presentation.viewModel.EventCreationViewModel
import java.text.SimpleDateFormat
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
                    onSaveClick = { event, uri ->
                        viewModel.createEvent(event, uri)
                    }
                )
            }

            is EventCreationViewModel.EventCreationState.Error -> {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventCreationContent(
    organizerName: String,
    onBackClick: () -> Unit,
    onSaveClick: (Event, String?) -> Unit
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

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { imageUrl = it.toString() }
    }

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
            TimePicker(
                state = timePickerStateStart,
                colors = TimePickerDefaults.colors(
                    clockDialSelectedContentColor = MaterialTheme.colorScheme.secondary
                )
            )
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
            TimePicker(
                state = timePickerStateEnd,
                colors = TimePickerDefaults.colors(
                    clockDialSelectedContentColor = MaterialTheme.colorScheme.secondary
                )
            )
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
                    .height(250.dp)
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                    .background(Color.LightGray)
                    .clickable {
                        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_archive_screen),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EditField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Заголовок мероприятия",
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
                        text = dateText.ifEmpty { "Дата" },
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
                        text = durationStart.ifEmpty { "От" },
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
                        text = durationEnd.ifEmpty { "До" },
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
                    modifier = Modifier.weight(1f),
                    placeholder = "Место",
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
                    placeholder = "Адрес",
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
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = { Text("Описание мероприятия...") }
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
                        ),
                        if (imageUrl.isNotEmpty()) imageUrl else null
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "СОЗДАТЬ",
                    fontFamily = EventsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    "OK",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    fontFamily = EventsFontFamily
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
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
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        if (value.isEmpty()) {
            Text(text = placeholder, style = textStyle.copy(color = Color.Gray))
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCreation() {
    SchoolEventsTheme(
        darkTheme = true
    ) {
        EventCreationContent(
            organizerName = "Vladusecho",
            onBackClick = { },
            onSaveClick = { _, _ -> }
        )
    }
}
