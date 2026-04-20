package com.vladusecho.schoolevents.data.mapper

import com.vladusecho.schoolevents.data.local.ProfileModel
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
