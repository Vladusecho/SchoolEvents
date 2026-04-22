package com.vladusecho.schoolevents.data.local.model

import androidx.room.Entity

@Entity(
    tableName = "favourite_events",
    primaryKeys = ["userEmail", "eventId"]
)
data class FavouriteEventModel(
    val userEmail: String,
    val eventId: Int,
)
