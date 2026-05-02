package com.vladusecho.schoolevents.domain.repository

import com.vladusecho.schoolevents.domain.entity.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(): Flow<List<News>>
    suspend fun addNews(news: News)

    suspend fun getNewsById(newsId: Int): News
    suspend fun saveImageToInternalStorage(uri: String): String
    suspend fun saveImagesToInternalStorage(uris: List<String>): List<String>
}
