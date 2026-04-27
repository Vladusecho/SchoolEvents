package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.News
import com.vladusecho.schoolevents.domain.repository.NewsRepository
import com.vladusecho.schoolevents.domain.usecase.events.GetEventsUseCase
import com.vladusecho.schoolevents.domain.usecase.events.SwitchEventFavouriteStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val switchFavouriteStatusUseCase: SwitchEventFavouriteStatusUseCase,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Initial)
    val state = _state.asStateFlow()

    private val _selectedTab = MutableStateFlow(MainTab.EVENTS)
    val selectedTab = _selectedTab.asStateFlow()

    fun selectTab(tab: MainTab) {
        _selectedTab.value = tab
    }

    fun processCommand(command: MainCommand) {
        when(command) {
            is MainCommand.SwitchFavouriteStatus -> {
                viewModelScope.launch {
                    switchFavouriteStatusUseCase(command.isFavourite, command.eventId)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            _state.value = MainState.Loading
            delay(500)
            combine(
                getEventsUseCase(),
                newsRepository.getNews()
            ) { events, news ->
                MainState.Content(events, news)
            }.collect {
                _state.value = it
            }
        }
    }

    enum class MainTab {
        EVENTS, NEWS
    }

    sealed interface MainState {
        object Initial : MainState
        object Loading : MainState
        data class Error(val message: String) : MainState
        data class Content(
            val events: List<Event>,
            val news: List<News>
        ) : MainState
    }

    sealed interface MainCommand {
        data class SwitchFavouriteStatus(
            val isFavourite: Boolean,
            val eventId: Int
        ) : MainCommand
    }
}
