package com.vladusecho.schoolevents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val imageUrls: String, // Store as JSON or comma-separated string
    val date: String,
    val creatorEmail: String
)
