package com.vladusecho.schoolevents.data.mapper

import com.vladusecho.schoolevents.data.local.EventWithStatus
import com.vladusecho.schoolevents.data.local.NewsWithStatus
import com.vladusecho.schoolevents.data.local.model.EventModel
import com.vladusecho.schoolevents.data.local.model.NewsModel
import com.vladusecho.schoolevents.data.local.model.ProfileModel
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.EventStatus
import com.vladusecho.schoolevents.domain.entity.News
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.domain.entity.Vote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun ProfileModel.toProfileEntity(): Profile {
    return Profile(
        id = id,
        name = name,
        surname = surname,
        email = email,
        password = password,
        classNumber = classNumber,
        role = role,
        imageUrl = imageUrl
    )
}

fun Flow<ProfileModel>.toProfileEntityFlow(): Flow<Profile> {
    return map { it.toProfileEntity() }
}

fun Profile.toProfileModel(): ProfileModel {
    return ProfileModel(
        id = id,
        name = name,
        surname = surname,
        email = email,
        password = password,
        classNumber = classNumber,
        role = role,
        imageUrl = imageUrl
    )
}

fun Event.toEventModel(): EventModel {
    return EventModel(
        id = id,
        title = title,
        description = description,
        eventAddress = eventAddress,
        eventPlace = eventPlace,
        eventDate = eventDate,
        eventDuration = eventDuration,
        imageUrls = imageUrls.joinToString("|"),
        isArchived = isArchived,
        creatorEmail = creatorEmail,
        status = status.name,
        timestamp = timestamp
    )
}

fun EventModel.toEventEntity(
    isFavourite: Boolean = false,
    isSubscribed: Boolean = false,
    likes: Int = 0,
    dislikes: Int = 0,
    userVote: Vote = Vote.NONE
): Event {
    return Event(
        id = id,
        title = title,
        description = description,
        eventAddress = eventAddress,
        eventPlace = eventPlace,
        eventDate = eventDate,
        eventDuration = eventDuration,
        imageUrls = if (imageUrls.isEmpty()) emptyList() else imageUrls.split("|"),
        isArchived = isArchived,
        isFavourite = isFavourite,
        isSubscribed = isSubscribed,
        creatorEmail = creatorEmail,
        status = try { EventStatus.valueOf(status) } catch (e: Exception) { EventStatus.PENDING },
        likes = likes,
        dislikes = dislikes,
        userVote = userVote,
        timestamp = timestamp
    )
}

fun EventWithStatus.toEventEntity(): Event {
    return event.toEventEntity(
        isFavourite = isFavourite,
        isSubscribed = isSubscribed,
        likes = likes,
        dislikes = dislikes,
        userVote = userVote?.let { Vote.valueOf(it) } ?: Vote.NONE
    )
}

fun Flow<List<EventModel>>.toEventEntityListFlow(): Flow<List<Event>> {
    return map { list ->
        list.map { it.toEventEntity() }
    }
}

fun Flow<List<EventWithStatus>>.toEventWithStatusEntityListFlow(): Flow<List<Event>> {
    return map { list ->
        list.map { it.toEventEntity() }
    }
}

fun News.toNewsModel(): NewsModel {
    return NewsModel(
        id = id,
        title = title,
        description = description,
        imageUrls = imageUrls.joinToString("|"),
        date = date,
        creatorEmail = creatorEmail,
        timestamp = timestamp
    )
}

fun NewsModel.toNewsEntity(
    likes: Int = 0,
    dislikes: Int = 0,
    userVote: Vote = Vote.NONE
): News {
    return News(
        id = id,
        title = title,
        description = description,
        imageUrls = if (imageUrls.isEmpty()) emptyList() else imageUrls.split("|"),
        date = date,
        creatorEmail = creatorEmail,
        likes = likes,
        dislikes = dislikes,
        userVote = userVote,
        timestamp = timestamp
    )
}

fun NewsWithStatus.toNewsEntity(): News {
    return news.toNewsEntity(
        likes = likes,
        dislikes = dislikes,
        userVote = userVote?.let { Vote.valueOf(it) } ?: Vote.NONE
    )
}

fun Flow<List<NewsModel>>.toNewsEntityListFlow(): Flow<List<News>> {
    return map { list ->
        list.map { it.toNewsEntity() }
    }
}

fun Flow<List<NewsWithStatus>>.toNewsWithStatusEntityListFlow(): Flow<List<News>> {
    return map { list ->
        list.map { it.toNewsEntity() }
    }
}
