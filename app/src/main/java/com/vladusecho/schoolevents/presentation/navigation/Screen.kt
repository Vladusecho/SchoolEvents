package com.vladusecho.schoolevents.presentation.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.vladusecho.schoolevents.domain.entity.Profile
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class Screen {

    @Serializable
    data class ProfileEditing(val profile: com.vladusecho.schoolevents.domain.entity.Profile) : Screen()
    @Serializable
    object Events : Screen()
    @Serializable
    object Favourite : Screen()
    @Serializable
    object Profile : Screen()

    @Serializable
    data class Login(val email: String) : Screen()

    @Serializable
    data class Registration(val email: String) : Screen()

    @Serializable
    object StartApp : Screen()
    @Serializable
    object AuthGraph : Screen()
    @Serializable
    object MainGraph : Screen()

    @Serializable
    object FavouriteGraph : Screen()

    @Serializable
    object ProfileGraph : Screen()
    @Serializable
    data class EventDetails(
        val id: Int
    ) : Screen()

    @Serializable
    data class EventEditing(
        val id: Int
    ) : Screen()

    @Serializable
    object EventCreation : Screen()

    @Serializable
    object NewsCreation : Screen()

    @Serializable
    data class NewsDetails(
        val id: Int
    ) : Screen()

    @Serializable
    data class NewsEditing(
        val id: Int
    ) : Screen()

    @Serializable
    object ArchiveGraph : Screen()
    @Serializable
    object Archive : Screen()

    @Serializable
    data class Participants(
        val eventId: Int
    ) : Screen()
}


val ProfileNavType = object : NavType<Profile>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Profile? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): Profile {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: Profile): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: Profile) {
        bundle.putString(key, Json.encodeToString(value))
    }
}
