package com.vladusecho.schoolevents.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.data.local.ParticipantWithAbsence
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayOutputStream
import java.io.OutputStream

@HiltViewModel(
    assistedFactory = ParticipantsViewModel.Factory::class
)
class ParticipantsViewModel @AssistedInject constructor(
    private val eventsRepository: EventsRepository,
    @Assisted("eventId") private val eventId: Int
) : ViewModel() {

    private val _state = MutableStateFlow<ParticipantsState>(ParticipantsState.Initial)
    val state = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        loadParticipants()
    }

    private fun loadParticipants() {
        viewModelScope.launch {
            _state.value = ParticipantsState.Loading
            try {
                eventsRepository.getParticipantsWithAbsence(eventId).collect { participants ->
                    _state.value = ParticipantsState.Content(participants)
                }
            } catch (e: Exception) {
                _state.value = ParticipantsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun markAbsence(userEmail: String, wasAbsent: Boolean) {
        viewModelScope.launch {
            eventsRepository.updateAbsenceStatus(userEmail, eventId, wasAbsent)
        }
    }

    fun exportToExcel(outputStream: OutputStream) {
        val currentState = _state.value
        if (currentState is ParticipantsState.Content) {
            try {
                val workbook = XSSFWorkbook()
                val sheet = workbook.createSheet("Участники мероприятия")

                // 1. Заголовки
                val headerRow = sheet.createRow(0)
                val headers = arrayOf("Имя", "Фамилия", "Класс", "Почта", "Прогул")
                
                headers.forEachIndexed { index, title ->
                    val cell = headerRow.createCell(index)
                    cell.setCellValue(title)
                }

                // 2. Данные
                currentState.participants.forEachIndexed { index, participant ->
                    val rowNum = index + 1
                    val row = sheet.createRow(rowNum)
                    val p = participant.profile
                    
                    row.createCell(0).setCellValue(p.name)
                    row.createCell(1).setCellValue(p.surname)
                    row.createCell(2).setCellValue(p.classNumber)
                    row.createCell(3).setCellValue(p.email)
                    row.createCell(4).setCellValue(if (participant.wasAbsent) "Да" else "Нет")
                }

                // 3. Запись
                val tempBuffer = ByteArrayOutputStream()
                workbook.write(tempBuffer)
                val bytes = tempBuffer.toByteArray()
                outputStream.write(bytes)
                outputStream.flush()
                
                workbook.close()
            } catch (e: Exception) {
                Log.e("ExcelExport", "Ошибка при создании Excel", e)
            }
        } else {
            Log.w("ExcelExport", "Состояние не Content, выгрузка невозможна")
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("eventId") eventId: Int
        ): ParticipantsViewModel
    }

    sealed interface ParticipantsState {
        object Initial : ParticipantsState
        object Loading : ParticipantsState
        data class Error(val message: String) : ParticipantsState
        data class Content(val participants: List<ParticipantWithAbsence>) : ParticipantsState
    }
}
