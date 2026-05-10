package com.vladusecho.schoolevents.data.repository

import android.content.Context
import androidx.core.net.toUri
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vladusecho.schoolevents.data.local.EventsAppDao
import com.vladusecho.schoolevents.data.local.model.NewsVoteModel
import com.vladusecho.schoolevents.data.mapper.toNewsEntity
import com.vladusecho.schoolevents.data.mapper.toNewsModel
import com.vladusecho.schoolevents.data.mapper.toNewsWithStatusEntityListFlow
import com.vladusecho.schoolevents.domain.entity.News
import com.vladusecho.schoolevents.domain.entity.Vote
import com.vladusecho.schoolevents.domain.repository.NewsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val dao: EventsAppDao,
    @param:ApplicationContext private val context: Context
) : NewsRepository {

    private val currentUserEmailKey = stringPreferencesKey("current_user_email")

    private val userEmailFlow: Flow<String> = context.dataStoreSettings.data
        .map { preferences -> preferences[currentUserEmailKey] ?: "" }

    private suspend fun getCurrentUserEmail(): String {
        return userEmailFlow.first()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getNews(): Flow<List<News>> {
        return userEmailFlow.flatMapLatest { email ->
            dao.getNews(email).toNewsWithStatusEntityListFlow()
        }
    }

    override suspend fun addNews(news: News) {
        val email = getCurrentUserEmail()
        dao.insertNews(news.copy(creatorEmail = email).toNewsModel())
    }

    override suspend fun getNewsById(newsId: Int): News {
        val email = getCurrentUserEmail()
        return dao.getNewsWithStatusById(newsId, email).toNewsEntity()
    }

    override suspend fun saveImageToInternalStorage(uri: String): String {
        return withContext(Dispatchers.IO) {
            val contentUri = uri.toUri()
            val inputStream = context.contentResolver.openInputStream(contentUri)
            val fileName = "news_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)

            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        }
    }

    override suspend fun saveImagesToInternalStorage(uris: List<String>): List<String> {
        return uris.map { uri ->
            if (uri.startsWith("/")) uri else saveImageToInternalStorage(uri)
        }
    }

    override suspend fun deleteNews(newsId: Int) {
        dao.deleteNews(newsId)
    }

    override suspend fun voteNews(newsId: Int, vote: Vote) {
        val email = getCurrentUserEmail()
        if (vote == Vote.NONE) {
            dao.deleteNewsVote(email, newsId)
        } else {
            dao.insertNewsVote(NewsVoteModel(userEmail = email, newsId = newsId, voteType = vote.name))
        }
    }
}
