package com.vladusecho.schoolevents.domain.entity

import com.vladusecho.schoolevents.R

data class Event(
    val id: Int,
    val title: String,
    val imageUrl: Int = R.drawable.maxresdefault,
    val description: String,
    val address: String,
    val eventDate: String,
    val isFavourite: Boolean = false,
)
