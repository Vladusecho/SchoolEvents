package com.vladusecho.schoolevents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("profile")
data class ProfileModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val email: String,
    val name: String,
    val surname: String,
    val password: String,
    val classNumber: String,
    val role: String,
    val imageUrl: String
)