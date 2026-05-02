package com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder

import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.presentation.navigation.NavigationState
import com.vladusecho.schoolevents.presentation.navigation.ProfileNavType
import com.vladusecho.schoolevents.presentation.navigation.Screen
import com.vladusecho.schoolevents.presentation.screen.EventDetailsScreen
import com.vladusecho.schoolevents.presentation.screen.EventEditingScreen
import com.vladusecho.schoolevents.presentation.screen.ParticipantsScreen
import com.vladusecho.schoolevents.presentation.screen.ProfileEditingScreen
import com.vladusecho.schoolevents.presentation.screen.ProfileScreen
import com.vladusecho.schoolevents.presentation.screen.UserRole
import com.vladusecho.schoolevents.presentation.viewModel.AuthViewModel
import kotlin.reflect.typeOf

fun NavGraphBuilder.profileNavigation(
    navigationState: NavigationState
) {
    navigation<Screen.ProfileGraph>(
        startDestination = Screen.Profile
    ) {
        composable<Screen.Profile> {

            val authViewModel: AuthViewModel = hiltViewModel()
            val userRole = authViewModel.userRole.collectAsState().value

            ProfileScreen(
                onEventClick = {
                    if (userRole == UserRole.STUDENT) {
                        navigationState.navigateToDetail(it)
                    } else {
                        navigationState.navigateToEventEditing(it)
                    }
                }, onListClick = { eventId: Int ->
                    navigationState.navigateToParticipants(eventId)
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
        composable<Screen.EventEditing> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EventEditing>()
            EventEditingScreen(
                eventId = args.id,
                onBackClick = { navigationState.navHostController.navigateUp() }
            )
        }
        composable<Screen.Participants> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.Participants>()
            ParticipantsScreen(
                eventId = args.eventId,
                onBackClick = {
                    navigationState.navHostController.navigateUp()
                }
            )
        }
    }
}