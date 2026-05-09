package com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.vladusecho.schoolevents.presentation.navigation.NavigationState
import com.vladusecho.schoolevents.presentation.navigation.Screen
import com.vladusecho.schoolevents.presentation.screen.ArchiveScreen
import com.vladusecho.schoolevents.presentation.screen.newScreen.ArchiveScreenContent
import com.vladusecho.schoolevents.presentation.screen.newScreen.ArchiveScreenNew

fun NavGraphBuilder.archiveNavigation(
    navigationState: NavigationState
) {
    navigation<Screen.ArchiveGraph>(
        startDestination = Screen.Archive
    ) {
        composable<Screen.Archive> {
            ArchiveScreenNew(
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
