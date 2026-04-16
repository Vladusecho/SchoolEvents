package com.vladusecho.schoolevents.presentation.navigation

import com.vladusecho.schoolevents.R

sealed class StudentNavItem(
    val screen: Any,
    val title: String,
    val iconId: Int
) {


    object Events : StudentNavItem(
        screen = Screen.MainGraph,
        title = "Мероприятия",
        iconId = R.drawable.ic_events_screen
    )

    object Favourite : StudentNavItem(
        screen = Screen.FavouriteGraph,
        title = "Избранное",
        iconId = R.drawable.ic_favourite_screen
    )

    object Profile : StudentNavItem(
        screen = Screen.ProfileGraph,
        title = "Профиль",
        iconId = R.drawable.ic_profile_screen
    )
}