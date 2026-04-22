package com.vladusecho.schoolevents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventModel(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val eventAddress: String,
    val eventPlace: String,
    val eventDate: String,
    val eventDuration: String,
    val imageUrl: String,
    val isArchived: Boolean
)