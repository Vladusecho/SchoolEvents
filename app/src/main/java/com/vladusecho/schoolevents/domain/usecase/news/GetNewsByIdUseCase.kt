package com.vladusecho.schoolevents.domain.usecase.news

import com.vladusecho.schoolevents.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsByIdUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(newsId: Int) = newsRepository.getNewsById(newsId)
}