package com.vladusecho.schoolevents.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object Events : Screen()
    @Serializable
    object Favourite : Screen()
    @Serializable
    object Profile : Screen()

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
