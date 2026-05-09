package com.vladusecho.schoolevents.presentation.screen.newScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.ImeAction
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

private enum class EventCreationStep {
    BASIC_DETAILS,
    DATE_AND_LOCATION
}

@Composable
fun EventCreationScreenNew(
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

    when (val currentState = state) {
        is EventCreationViewModel.EventCreationState.Content -> {
            EventCreationContent(
                onBackClick = onBackClick,
                onSaveClick = { event, uris ->
                    viewModel.createEvent(event, uris)
                }
            )
        }

        is EventCreationViewModel.EventCreationState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = currentState.message, color = Color.Red, fontFamily = EventsFontFamily)
            }
        }

        EventCreationViewModel.EventCreationState.Initial -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        else -> {}
    }
}

@Composable
private fun EventCreationContent(
    onBackClick: () -> Unit,
    onSaveClick: (Event, List<String>) -> Unit
) {
    var currentStep by remember { mutableStateOf(EventCreationStep.BASIC_DETAILS) }

    // Form data
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    var address by remember { mutableStateOf("") }
    var schoolPlace by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            if (uris.isNotEmpty()) {
                selectedImageUris = (selectedImageUris + uris).distinct()
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                item {
                    Spacer(modifier = Modifier.height(64.dp))
                    IconButton(
                        onClick = {
                            if (currentStep == EventCreationStep.DATE_AND_LOCATION) {
                                currentStep = EventCreationStep.BASIC_DETAILS
                            } else {
                                onBackClick()
                            }
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (currentStep == EventCreationStep.BASIC_DETAILS) {
                    item {
                        Step1UI(
                            title = title,
                            onTitleChange = { title = it },
                            description = description,
                            onDescriptionChange = { description = it },
                            selectedImageUris = selectedImageUris,
                            onImagePick = {
                                imagePicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            onRemoveImage = { uri ->
                                selectedImageUris = selectedImageUris.filter { it != uri }
                            }
                        )
                    }
                } else {
                    item {
                        Step2UI(
                            address = address,
                            onAddressChange = { address = it },
                            schoolPlace = schoolPlace,
                            onSchoolPlaceChange = { schoolPlace = it },
                            dateText = dateText,
                            onDateTextChange = { dateText = it },
                            startTime = startTime,
                            onStartTimeChange = { startTime = it },
                            endTime = endTime,
                            onEndTimeChange = { endTime = it }
                        )
                    }
                }
                item {
                    Button(
                        onClick = {
                            if (currentStep == EventCreationStep.BASIC_DETAILS) {
                                currentStep = EventCreationStep.DATE_AND_LOCATION
                            } else {
                                onSaveClick(
                                    Event(
                                        id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                                        title = title,
                                        description = description,
                                        eventAddress = address,
                                        eventPlace = schoolPlace,
                                        eventDate = dateText,
                                        eventDuration = if (endTime.isNotEmpty()) "$startTime - $endTime" else startTime,
                                        imageUrls = selectedImageUris.map { it.toString() },
                                        isArchived = false,
                                        isFavourite = false,
                                        isSubscribed = false
                                    ),
                                    selectedImageUris.map { it.toString() }
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff008A00),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(28.dp),
                        enabled = if (currentStep == EventCreationStep.BASIC_DETAILS) {
                            title.isNotBlank() && description.isNotBlank() && selectedImageUris.isNotEmpty()
                        } else {
                            address.isNotBlank() && schoolPlace.isNotBlank() && dateText.isNotBlank() && startTime.isNotBlank()
                        }
                    ) {
                        Text(
                            text = if (currentStep == EventCreationStep.BASIC_DETAILS) "Продолжить" else "Создать",
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .background(Color.White)
                            .height(136.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun Step1UI(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    selectedImageUris: List<Uri>,
    onImagePick: () -> Unit,
    onRemoveImage: (Uri) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Основная информация",
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.Black
        )
        Text(
            text = "Введите название и описание мероприятия, а также загрузите фотогорафию, чтобы продолжить",
            fontFamily = EventsFontFamily,
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Название мероприятия *", fontFamily = EventsFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xffEBEBEB)
            ),
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Описание мероприятия *", fontFamily = EventsFontFamily) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xffEBEBEB),
            ),
            textStyle = TextStyle(
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xffF2F2F2))
                .clickable { onImagePick() },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUris.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(selectedImageUris) { uri ->
                        Box(
                            modifier = Modifier
                                .size(184.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { onRemoveImage(uri) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(32.dp)
                                    .padding(4.dp)
                                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            ) {
                                Text(
                                    text = "X"
                                )
                            }
                        }
                    }
                    item {
                        Surface(
                            modifier = Modifier
                                .size(184.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onImagePick() },
                            color = Color.White.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_plus_circle),
                                    contentDescription = "Add more",
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.ic_archive_screen),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Загрузить фотографии *",
                        fontFamily = EventsFontFamily,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
        }
        Text(
            text = "Рекомендуется загружать горизонтальные фотографии в высоком разрешении.",
            fontFamily = EventsFontFamily,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Step2UI(
    address: String,
    onAddressChange: (String) -> Unit,
    schoolPlace: String,
    onSchoolPlaceChange: (String) -> Unit,
    dateText: String,
    onDateTextChange: (String) -> Unit,
    startTime: String,
    onStartTimeChange: (String) -> Unit,
    endTime: String,
    onEndTimeChange: (String) -> Unit
) {
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
                        val format = SimpleDateFormat("d MMMM", Locale.forLanguageTag("ru"))
                        onDateTextChange(format.format(date))
                    }
                    showDatePicker = false
                }) {
                    Text("OK", fontFamily = EventsFontFamily)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePickerStart) {
        LocalTimePickerDialog(
            onDismissRequest = { showTimePickerStart = false },
            onConfirm = {
                onStartTimeChange(
                    String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        timePickerStateStart.hour,
                        timePickerStateStart.minute
                    )
                )
                showTimePickerStart = false
            }
        ) {
            TimePicker(state = timePickerStateStart)
        }
    }

    if (showTimePickerEnd) {
        LocalTimePickerDialog(
            onDismissRequest = { showTimePickerEnd = false },
            onConfirm = {
                onEndTimeChange(
                    String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        timePickerStateEnd.hour,
                        timePickerStateEnd.minute
                    )
                )
                showTimePickerEnd = false
            }
        ) {
            TimePicker(state = timePickerStateEnd)
        }
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Дата и место",
            fontFamily = EventsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.Black
        )
        Text(
            text = "Укажите место проведения и время начала",
            fontFamily = EventsFontFamily,
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text("Адрес *", fontFamily = EventsFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xffEBEBEB)
            ),
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = schoolPlace,
            onValueChange = onSchoolPlaceChange,
            label = {
                Text(
                    "Место в школе * (например, Актовый зал)",
                    fontFamily = EventsFontFamily
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xffEBEBEB)
            ),
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = dateText,
            onValueChange = {},
            label = { Text("Дата *", fontFamily = EventsFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = false,
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_date),
                        contentDescription = null
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color(0xffEBEBEB),
                disabledLabelColor = Color.Gray,
                disabledTextColor = Color.Black,
                disabledTrailingIconColor = Color.Black
            ),
            textStyle = TextStyle(
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = startTime,
            onValueChange = {},
            label = { Text("Время начала *", fontFamily = EventsFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = false,
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                IconButton(onClick = { showTimePickerStart = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_date),
                        contentDescription = null
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color(0xffEBEBEB),
                disabledLabelColor = Color.Gray,
                disabledTextColor = Color.Black,
                disabledTrailingIconColor = Color.Black
            ),
            textStyle = TextStyle(
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = endTime,
            onValueChange = {},
            label = { Text("Время окончания", fontFamily = EventsFontFamily) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = false,
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                IconButton(onClick = { showTimePickerEnd = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_date),
                        contentDescription = null
                    )
                }
            },
            textStyle = TextStyle(
                fontFamily = EventsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color(0xffEBEBEB),
                disabledLabelColor = Color.Gray,
                disabledTextColor = Color.Black,
                disabledTrailingIconColor = Color.Black
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocalTimePickerDialog(
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

@Preview(showBackground = true)
@Composable
private fun PreviewEventCreation() {
    SchoolEventsTheme {
        EventCreationContent(onBackClick = {}, onSaveClick = { _, _ -> })
    }
}
