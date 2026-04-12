package com.vladusecho.schoolevents.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.vladusecho.schoolevents.presentation.screen.EventDetailsScreen
import com.vladusecho.schoolevents.presentation.screen.FavouriteScreen
import com.vladusecho.schoolevents.presentation.screen.MainScreen
import com.vladusecho.schoolevents.presentation.screen.ProfileScreen

@Composable
fun AppNavGraph(
    navigationState: NavigationState
) {

    NavHost(
        navController = navigationState.navHostController,
        startDestination = Screen.MainGraph,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 0))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 0))
        }
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
        composable<Screen.Favourite> {
            FavouriteScreen()
        }
        composable<Screen.Profile> {
            ProfileScreen()
        }
    }
}
