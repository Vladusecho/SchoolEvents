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

@HiltViewModel(assistedFactory = NewsEditingViewModel.Factory::class)
class NewsEditingViewModel @AssistedInject constructor(
    @Assisted private val newsId: Int,
    private val getProfileUseCase: GetProfileUseCase,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<NewsEditingState>(NewsEditingState.Initial)
    val state = _state.asStateFlow()

    init {
        loadNews()
    }

    private fun loadNews() {
        viewModelScope.launch {
            _state.value = NewsEditingState.Loading
            try {
                val newsList = newsRepository.getNews().first()
                val news = newsList.find { it.id == newsId } ?: throw Exception("News not found")
                val profile = getProfileUseCase().first()
                _state.value = NewsEditingState.Content(
                    news = news,
                    organizerName = "${profile.name} ${profile.surname}"
                )
            } catch (e: Exception) {
                _state.value = NewsEditingState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateNews(updatedNews: News, imageUri: String?) {
        viewModelScope.launch {
            try {
                val finalImageUrl = imageUri?.let { newsRepository.saveImageToInternalStorage(it) } ?: updatedNews.imageUrl
                newsRepository.addNews(updatedNews.copy(imageUrl = finalImageUrl))
                _state.value = NewsEditingState.Saved
            } catch (e: Exception) {
                _state.value = NewsEditingState.Error(e.message ?: "Failed to save news")
            }
        }
    }

    fun deleteNews() {
        // TODO: Implement delete news in repository if needed
        // For now just back
        _state.value = NewsEditingState.Saved
    }

    sealed interface NewsEditingState {
        object Initial : NewsEditingState
        object Loading : NewsEditingState
        data class Content(val news: News, val organizerName: String) : NewsEditingState
        data class Error(val message: String) : NewsEditingState
        object Saved : NewsEditingState
    }

    @AssistedFactory
    interface Factory {
        fun create(newsId: Int): NewsEditingViewModel
    }
}
