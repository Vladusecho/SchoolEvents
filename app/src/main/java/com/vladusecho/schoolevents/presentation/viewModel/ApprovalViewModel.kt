package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.usecase.events.GetPendingEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApprovalViewModel @Inject constructor(
    private val getPendingEventsUseCase: GetPendingEventsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ApprovalState>(ApprovalState.Initial)
    val state = _state.asStateFlow()

    init {
        loadPendingEvents()
    }

    private fun loadPendingEvents() {
        viewModelScope.launch {
            _state.value = ApprovalState.Loading
            try {
                getPendingEventsUseCase().collect { events ->
                    _state.value = ApprovalState.Content(events)
                }
            } catch (e: Exception) {
                _state.value = ApprovalState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed interface ApprovalState {
        object Initial : ApprovalState
        object Loading : ApprovalState
        data class Error(val message: String) : ApprovalState
        data class Content(val events: List<Event>) : ApprovalState
    }
}
