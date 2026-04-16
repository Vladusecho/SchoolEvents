package com.vladusecho.schoolevents.domain.entity

data class Profile(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val classNumber: String,
    val role: String,
    val imageUrl: String
)