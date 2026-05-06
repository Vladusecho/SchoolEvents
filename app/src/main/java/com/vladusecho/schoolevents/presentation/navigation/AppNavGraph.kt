package com.vladusecho.schoolevents.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.presentation.navigation.ProfileNavType
import com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder.approvalNavigation
import com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder.archiveNavigation
import com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder.authNavigation
import com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder.favouriteNavigation
import com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder.mainNavigation
import com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder.profileNavigation
import com.vladusecho.schoolevents.presentation.screen.EventDetailsScreen
import com.vladusecho.schoolevents.presentation.screen.FavouriteScreen
import com.vladusecho.schoolevents.presentation.screen.LoginScreen
import com.vladusecho.schoolevents.presentation.screen.MainScreen
import com.vladusecho.schoolevents.presentation.screen.ProfileEditingScreen
import com.vladusecho.schoolevents.presentation.screen.ProfileScreen
import com.vladusecho.schoolevents.presentation.screen.RegistrationScreen
import com.vladusecho.schoolevents.presentation.screen.StartAppScreen
import kotlin.reflect.typeOf

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




