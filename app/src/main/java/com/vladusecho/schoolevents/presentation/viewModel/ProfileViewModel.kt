package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.News
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.repository.EventsRepository
import com.vladusecho.schoolevents.domain.repository.NewsRepository
import com.vladusecho.schoolevents.domain.usecase.auth.ChangeUserIsAuthUseCase
import com.vladusecho.schoolevents.domain.usecase.events.GetEventsByCreatorUseCase
import com.vladusecho.schoolevents.domain.usecase.events.GetSubscribedEventsUseCase
import com.vladusecho.schoolevents.domain.usecase.events.SwitchEventFavouriteStatusUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.GetProfileUseCase
import com.vladusecho.schoolevents.presentation.util.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getSubscribedEventsUseCase: GetSubscribedEventsUseCase,
    private val getEventsByCreatorUseCase: GetEventsByCreatorUseCase,
    private val changeUserIsAuthUseCase: ChangeUserIsAuthUseCase,
    private val switchEventFavouriteStatusUseCase: SwitchEventFavouriteStatusUseCase,
    private val eventsRepository: EventsRepository,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val state = _state.asStateFlow()

    private val _isExit = MutableSharedFlow<Boolean>()
    val isExit = _isExit.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = ProfileState.Loading

            getProfileUseCase().flatMapLatest { profile ->
                val statsFlow = if (profile.role == UserRole.STUDENT.label) {
                    combine(
                        eventsRepository.getAttendedEventsCount(profile.email),
                        eventsRepository.getAbsentEventsCount(profile.email)
                    ) { attended, absent -> attended to absent }
                } else {
                    flowOf(0 to 0)
                }

                val eventsFlow = when (profile.role) {
                    UserRole.STUDENT.label -> getSubscribedEventsUseCase()
                    UserRole.ORGANIZER.label -> getEventsByCreatorUseCase(profile.email)
                    else -> flowOf(emptyList())
                }

                val weeklyStatsFlow = if (profile.role == UserRole.DIRECTOR.label) {
                    combine(
                        eventsRepository.getEvents(),
                        newsRepository.getNews()
                    ) { events, news ->
                        calculateWeeklyStats(events, news)
                    }
                } else {
                    flowOf(emptyList())
                }

                combine(eventsFlow, statsFlow, weeklyStatsFlow) { events, stats, weeklyStats ->
                    ProfileData(profile, events.sortedByDescending { it.id }, stats, weeklyStats)
                }
            }.collect { data ->
                _state.value = ProfileState.Content(
                    profile = data.profile,
                    events = data.events,
                    attendedCount = data.stats.first,
                    absentCount = data.stats.second,
                    weeklyStats = data.weeklyStats
                )
            }
        }
    }

    private fun calculateWeeklyStats(events: List<Event>, news: List<News>): List<DayStat> {
        val dateFormat = SimpleDateFormat("d MMMM", Locale.forLanguageTag("ru"))
        val dayFormat = SimpleDateFormat("EE", Locale.forLanguageTag("ru"))
        
        val stats = mutableListOf<DayStat>()
        
        for (i in 6 downTo 0) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val date = calendar.time
            
            val dateString = dateFormat.format(date)
            val dayName = dayFormat.format(date).replaceFirstChar { it.uppercase() }
            
            val eventsCount = events.count { it.eventDate == dateString }
            val newsCount = news.count { it.date == dateString }
            
            stats.add(DayStat(dayName, eventsCount, newsCount))
        }
        
        return stats
    }

    private data class ProfileData(
        val profile: Profile,
        val events: List<Event>,
        val stats: Pair<Int, Int>,
        val weeklyStats: List<DayStat>
    )

    fun processCommand(command: ProfileCommand) {
        when (command) {
            is ProfileCommand.Exit -> {
                viewModelScope.launch {
                    _isLoading.value = true
                    changeUserIsAuthUseCase()
                    _isExit.emit(true)
                    _isLoading.value = false
                }
            }

            is ProfileCommand.SwitchFavouriteStatus -> {
                viewModelScope.launch {
                    switchEventFavouriteStatusUseCase(command.isFavourite, command.eventId)
                }
            }
        }
    }

    sealed interface ProfileState {
        object Initial : ProfileState
        object Loading : ProfileState
        data class Error(val message: String) : ProfileState
        data class Content(
            val profile: Profile,
            val events: List<Event>,
            val attendedCount: Int = 0,
            val absentCount: Int = 0,
            val weeklyStats: List<DayStat> = emptyList()
        ) : ProfileState
    }

    data class DayStat(
        val day: String,
        val eventsCount: Int,
        val newsCount: Int
    )

    sealed interface ProfileCommand {
        object Exit : ProfileCommand
        data class SwitchFavouriteStatus(val isFavourite: Boolean, val eventId: Int) :
            ProfileCommand
    }
}
