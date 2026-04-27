package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.News
import com.vladusecho.schoolevents.domain.repository.NewsRepository
import com.vladusecho.schoolevents.domain.usecase.profile.GetProfileUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = NewsDetailsViewModel.Factory::class)
class NewsDetailsViewModel @AssistedInject constructor(
    @Assisted private val newsId: Int,
    private val getProfileUseCase: GetProfileUseCase,
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
                val newsList = newsRepository.getNews().first()
                val news = newsList.find { it.id == newsId } ?: throw Exception("News not found")
                val profile = getProfileUseCase().first()
                _state.value = NewsDetailsState.Content(
                    news = news,
                    organizerName = "${profile.name} ${profile.surname}"
                )
            } catch (e: Exception) {
                _state.value = NewsDetailsState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed interface NewsDetailsState {
        object Initial : NewsDetailsState
        object Loading : NewsDetailsState
        data class Content(val news: News, val organizerName: String) : NewsDetailsState
        data class Error(val message: String) : NewsDetailsState
    }

    @AssistedFactory
    interface Factory {
        fun create(newsId: Int): NewsDetailsViewModel
    }
}
