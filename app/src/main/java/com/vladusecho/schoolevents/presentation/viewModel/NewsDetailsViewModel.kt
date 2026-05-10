package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.News
import com.vladusecho.schoolevents.domain.entity.Vote
import com.vladusecho.schoolevents.domain.repository.NewsRepository
import com.vladusecho.schoolevents.domain.usecase.news.GetNewsByIdUseCase
import com.vladusecho.schoolevents.domain.usecase.profile.GetProfileByEmailUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = NewsDetailsViewModel.Factory::class)
class NewsDetailsViewModel @AssistedInject constructor(
    @Assisted private val newsId: Int,
    private val getProfileByEmailUseCase: GetProfileByEmailUseCase,
    private val getNewsByIdUseCase: GetNewsByIdUseCase,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<NewsDetailsState>(NewsDetailsState.Initial)
    val state = _state.asStateFlow()

    init {
        loadNews()
    }

    private fun loadNews() {
        viewModelScope.launch {
            _state.value = NewsDetailsState.Loading
            try {
                val news = newsRepository.getNewsById(newsId)
                val organizerName = try {
                    val profile = getProfileByEmailUseCase(news.creatorEmail)
                    "${profile.name} ${profile.surname}"
                } catch (e: Exception) {
                    "Организатор не указан"
                }
                _state.value = NewsDetailsState.Content(
                    news = news,
                    organizerName = organizerName
                )
            } catch (e: Exception) {
                _state.value = NewsDetailsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun processCommand(command: NewsDetailsCommand) {
        when (command) {
            is NewsDetailsCommand.VoteNews -> {
                viewModelScope.launch {
                    newsRepository.voteNews(newsId, command.vote)
                    loadNews()
                }
            }
        }
    }

    sealed interface NewsDetailsState {
        object Initial : NewsDetailsState
        object Loading : NewsDetailsState
        data class Content(val news: News, val organizerName: String) : NewsDetailsState
        data class Error(val message: String) : NewsDetailsState
    }

    sealed interface NewsDetailsCommand {
        data class VoteNews(val vote: Vote) : NewsDetailsCommand
    }

    @AssistedFactory
    interface Factory {
        fun create(newsId: Int): NewsDetailsViewModel
    }
}
