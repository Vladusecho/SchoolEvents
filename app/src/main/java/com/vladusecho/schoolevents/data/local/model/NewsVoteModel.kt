package com.vladusecho.schoolevents.data.local.model

import androidx.room.Entity

@Entity(tableName = "news_votes", primaryKeys = ["userEmail", "newsId"])
data class NewsVoteModel(
    val userEmail: String,
    val newsId: Int,
    val voteType: String // "LIKE", "DISLIKE"
)
