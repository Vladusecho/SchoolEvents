package com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.presentation.navigation.NavigationState
import com.vladusecho.schoolevents.presentation.navigation.ProfileNavType
import com.vladusecho.schoolevents.presentation.navigation.Screen
import com.vladusecho.schoolevents.presentation.screen.EventDetailsScreen
import com.vladusecho.schoolevents.presentation.screen.ProfileEditingScreen
import com.vladusecho.schoolevents.presentation.screen.ProfileScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.profileNavigation(
    navigationState: NavigationState
) {
    navigation<Screen.ProfileGraph>(
        startDestination = Screen.Profile
    ) {
        composable<Screen.Profile> {
            ProfileScreen(
                onEventClick = {
                    navigationState.navigateToDetail(it)
                },
                onEditingClick = {
                    navigationState.navigateToProfileEditing(it)
                },
                onExitClick = {
                    navigationState.navHostController.navigate(Screen.AuthGraph) {
                        popUpTo(Screen.MainGraph) { inclusive = true }
                    }
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
        composable<Screen.ProfileEditing>(
            typeMap = mapOf(
                typeOf<Profile>() to ProfileNavType
            )
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.ProfileEditing>()
            ProfileEditingScreen(
                onBackClick = {
                    navigationState.navHostController.navigateUp()
                },
                profile = args.profile
            )
        }
    }
}