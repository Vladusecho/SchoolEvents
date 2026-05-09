package com.vladusecho.schoolevents.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder.approvalNavigation
import com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder.archiveNavigation
import com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder.authNavigation
import com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder.favouriteNavigation
import com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder.mainNavigation
import com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder.profileNavigation

@Composable
fun AppNavGraph(
    navigationState: NavigationState,
    startDestination: Any
) {

    NavHost(
        navController = navigationState.navHostController,
        startDestination = startDestination,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 0))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 0))
        }
    ) {
        authNavigation(navigationState)
        mainNavigation(navigationState)
        favouriteNavigation(navigationState)
        profileNavigation(navigationState)
        archiveNavigation(navigationState)
        approvalNavigation(navigationState)
    }
}




