package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.schoolevents.domain.entity.News
import com.vladusecho.schoolevents.domain.repository.NewsRepository
import com.vladusecho.schoolevents.domain.usecase.profile.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsCreationViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<NewsCreationState>(NewsCreationState.Initial)
    val state = _state.asStateFlow()

    init {
        loadOrganizerProfile()
    }

    private fun loadOrganizerProfile() {
        viewModelScope.launch {
            try {
                val profile = getProfileUseCase().first()
                _state.value = NewsCreationState.Content(
                    organizerName = "${profile.name} ${profile.surname}"
                )
            } catch (e: Exception) {
                _state.value = NewsCreationState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun createNews(news: News, imageUris: List<String>) {
        viewModelScope.launch {
            try {
                val finalImageUrls = newsRepository.saveImagesToInternalStorage(imageUris)
                newsRepository.addNews(news.copy(imageUrls = finalImageUrls))
                _state.value = NewsCreationState.Saved
            } catch (e: Exception) {
                _state.value = NewsCreationState.Error(e.message ?: "Failed to save news")
            }
        }
    }

    sealed interface NewsCreationState {
        object Initial : NewsCreationState
        data class Content(val organizerName: String) : NewsCreationState
        data class Error(val message: String) : NewsCreationState
        object Saved : NewsCreationState
    }
}
