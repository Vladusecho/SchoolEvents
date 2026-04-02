package com.vladusecho.schoolevents.domain.entity

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val address: String,
    val eventDate: String,
    val isFavourite: Boolean = false,
)
