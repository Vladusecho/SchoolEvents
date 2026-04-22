package com.vladusecho.schoolevents.data.mapper

import com.vladusecho.schoolevents.data.local.EventWithStatus
import com.vladusecho.schoolevents.data.local.model.EventModel
import com.vladusecho.schoolevents.data.local.model.ProfileModel
import com.vladusecho.schoolevents.data.local.model.SubscribedEventModel
import com.vladusecho.schoolevents.domain.entity.Event
import com.vladusecho.schoolevents.domain.entity.Profile
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
        imageUrl = imageUrl,
        isArchived = isArchived,
        creatorEmail = creatorEmail
    )
}

fun EventModel.toEventEntity(isFavourite: Boolean = false, isSubscribed: Boolean = false): Event {
    return Event(
        id = id,
        title = title,
        description = description,
        eventAddress = eventAddress,
        eventPlace = eventPlace,
        eventDate = eventDate,
        eventDuration = eventDuration,
        imageUrl = imageUrl,
        isArchived = isArchived,
        isFavourite = isFavourite,
        isSubscribed = isSubscribed,
        creatorEmail = creatorEmail
    )
}

fun EventWithStatus.toEventEntity(): Event {
    return event.toEventEntity(isFavourite = isFavourite, isSubscribed = isSubscribed)
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
