package com.vladusecho.schoolevents.domain.entity

import com.vladusecho.schoolevents.R

data class Event(
    val id: Int,
    val title: String,
    val imageUrl: Int = R.drawable.maxresdefault,
    val description: String,
    val eventAddress: String,
    val eventPlace: String,
    val eventDate: String,
    val eventDuration: String,
    val isFavourite: Boolean = false,
)
