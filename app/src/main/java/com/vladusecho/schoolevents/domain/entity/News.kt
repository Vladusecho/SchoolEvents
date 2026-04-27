package com.vladusecho.schoolevents.domain.entity

data class News(
    val id: Int = 0,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val date: String,
    val creatorEmail: String = ""
)
