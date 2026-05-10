package com.vladusecho.schoolevents.data.local.model

import androidx.room.Entity

@Entity(tableName = "event_votes", primaryKeys = ["userEmail", "eventId"])
data class EventVoteModel(
    val userEmail: String,
    val eventId: Int,
    val voteType: String // "LIKE", "DISLIKE"
)
