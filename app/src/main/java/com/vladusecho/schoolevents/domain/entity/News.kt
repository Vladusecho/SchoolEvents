package com.vladusecho.schoolevents.domain.entity

data class News(
    val id: Int = 0,
    val title: String,
    val description: String,
    val imageUrls: List<String> = emptyList(),
    val date: String,
    val creatorEmail: String = ""
) {
    // Keep this for backward compatibility or simple usage where only one image is needed
    val imageUrl: String? get() = imageUrls.firstOrNull()
}
