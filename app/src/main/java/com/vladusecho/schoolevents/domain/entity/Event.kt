package com.vladusecho.schoolevents.domain.entity

data class Event(
    val id: Int,
    val title: String,
    val imageUrl: Int,
    val description: String,
    val eventAddress: String,
    val eventPlace: String,
    val eventDate: String,
    val eventDuration: String,
    val isFavourite: Boolean = false,
    val isSubscribed: Boolean = false
)
