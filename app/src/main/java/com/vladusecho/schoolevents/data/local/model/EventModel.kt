package com.vladusecho.schoolevents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val eventAddress: String,
    val eventPlace: String,
    val eventDate: String,
    val eventDuration: String,
    val imageUrls: String, // Store as JSON or pipe-separated string
    val isArchived: Boolean,
    val creatorEmail: String = "",
    val status: String = "PENDING" // PENDING, APPROVED, REJECTED
)
