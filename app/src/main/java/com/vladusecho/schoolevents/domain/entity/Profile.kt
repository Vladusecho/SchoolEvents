package com.vladusecho.schoolevents.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val classNumber: String,
    val role: String,
    val imageUrl: String
)