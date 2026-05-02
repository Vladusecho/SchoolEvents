package com.vladusecho.schoolevents.data.local.model

import androidx.room.Entity

@Entity(
    tableName = "subscribed_events",
    primaryKeys = ["userEmail", "eventId"]
)
data class SubscribedEventModel(
    val userEmail: String,
    val eventId: Int
)