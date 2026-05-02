package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.usecase.events.GetArchivedEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val getArchivedEventsUseCase: GetArchivedEventsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ArchiveState>(ArchiveState.Initial)
    val state = _state.asStateFlow()

    init {
        loadArchivedEvents()
    }

    private fun loadArchivedEvents() {
        viewModelScope.launch {
            _state.value = ArchiveState.Loading
            getArchivedEventsUseCase().collect { events ->
                _state.value = ArchiveState.Content(events)
            }
        }
    }

    sealed interface ArchiveState {
        object Initial : ArchiveState
        object Loading : ArchiveState
        data class Content(val events: List<Event>) : ArchiveState
        data class Error(val message: String) : ArchiveState
    }
}
