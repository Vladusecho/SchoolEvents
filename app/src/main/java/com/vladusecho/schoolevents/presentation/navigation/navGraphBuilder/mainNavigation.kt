package com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.vladusecho.schoolevents.presentation.navigation.NavigationState
import com.vladusecho.schoolevents.presentation.navigation.Screen
import com.vladusecho.schoolevents.presentation.screen.EventDetailsScreen
import com.vladusecho.schoolevents.presentation.screen.MainScreen

fun NavGraphBuilder.mainNavigation(
    navigationState: NavigationState
) {
    navigation<Screen.MainGraph>(
        startDestination = Screen.Events
    ) {
        composable<Screen.Events> {
            MainScreen(
                onEventClick = { eventId ->
                    navigationState.navigateToDetail(eventId)
                }
            )
        }
        composable<Screen.EventDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EventDetails>()
            EventDetailsScreen(
                eventId = args.id,
                onBackClick = {
                    navigationState.navHostController.navigateUp()
                }
            )
        }
    }
}
