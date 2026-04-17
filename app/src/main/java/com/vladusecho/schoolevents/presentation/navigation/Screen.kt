package com.vladusecho.schoolevents.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    object ProfileEditing : Screen()
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
}
