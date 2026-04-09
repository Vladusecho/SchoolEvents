package com.vladusecho.schoolevents.presentation.navigation

sealed class Screen(
    val route: String
) {

    object Events : Screen(EVENTS_ROUTE)
    object Favourite : Screen(FAVOURITE_ROUTE)
    object Profile : Screen(PROFILE_ROUTE)

    companion object {

        const val EVENTS_ROUTE = "events"
        const val FAVOURITE_ROUTE = "favourite"
        const val PROFILE_ROUTE = "profile"
    }
}
