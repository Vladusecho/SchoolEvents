package com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.vladusecho.schoolevents.presentation.navigation.NavigationState
import com.vladusecho.schoolevents.presentation.navigation.Screen
import com.vladusecho.schoolevents.presentation.screen.ArchiveScreen

fun NavGraphBuilder.archiveNavigation(
    navigationState: NavigationState
) {
    navigation<Screen.ArchiveGraph>(
        startDestination = Screen.Archive
    ) {
        composable<Screen.Archive> {
            ArchiveScreen(
                onEventClick = { eventId ->
                    navigationState.navigateToDetail(eventId)
                },
                onListClick = { eventId ->
                    navigationState.navigateToParticipants(eventId)
                }
            )
        }
    }
}
