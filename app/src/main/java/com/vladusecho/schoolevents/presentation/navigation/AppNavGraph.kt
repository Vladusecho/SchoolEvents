package com.vladusecho.schoolevents.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vladusecho.schoolevents.presentation.screen.FavouriteScreen
import com.vladusecho.schoolevents.presentation.screen.MainScreen
import com.vladusecho.schoolevents.presentation.screen.ProfileScreen

@Composable
fun AppNavGraph(
    navigationState: NavigationState
) {

    NavHost(
        navController = navigationState.navHostController,
        startDestination = Screen.Events.route,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 0))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 0))
        }
    ) {
        composable(Screen.Events.route) {
            MainScreen()
        }
        composable(Screen.Favourite.route) {
            FavouriteScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }

}