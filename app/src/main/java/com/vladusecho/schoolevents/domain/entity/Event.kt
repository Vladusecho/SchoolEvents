package com.vladusecho.schoolevents.domain.entity

data class Event(
    val id: Int,
    val title: String,
    val imageUrls: List<String> = emptyList(),
    val description: String,
    val eventAddress: String,
    val eventPlace: String,
    val eventDate: String,
    val eventDuration: String,
    val isArchived: Boolean,
    val isFavourite: Boolean,
    val isSubscribed: Boolean,
    val creatorEmail: String = ""
) {
    val imageUrl: String get() = imageUrls.firstOrNull() ?: ""
}
