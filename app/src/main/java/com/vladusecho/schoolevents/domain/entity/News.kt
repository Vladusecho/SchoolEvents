package com.vladusecho.schoolevents.domain.entity

data class News(
    val id: Int = 0,
    val title: String,
    val description: String,
    val imageUrls: List<String> = emptyList(),
    val date: String,
    val creatorEmail: String = "",
    val likes: Int = 0,
    val dislikes: Int = 0,
    val userVote: Vote = Vote.NONE,
    val timestamp: Long = System.currentTimeMillis()
) {
    // Keep this for backward compatibility or simple usage where only one image is needed
    val imageUrl: String? get() = imageUrls.firstOrNull()
}
