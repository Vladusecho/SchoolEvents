package com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder

import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.vladusecho.schoolevents.presentation.navigation.NavigationState
import com.vladusecho.schoolevents.presentation.navigation.Screen
import com.vladusecho.schoolevents.presentation.screen.newScreen.EventCreationScreenNew
import com.vladusecho.schoolevents.presentation.screen.newScreen.EventDetailsScreenNew
import com.vladusecho.schoolevents.presentation.screen.newScreen.EventEditingScreenNew
import com.vladusecho.schoolevents.presentation.screen.newScreen.MainScreenNew
import com.vladusecho.schoolevents.presentation.screen.newScreen.NewsCreationScreenNew
import com.vladusecho.schoolevents.presentation.screen.newScreen.NewsDetailsScreenNew
import com.vladusecho.schoolevents.presentation.screen.newScreen.NewsEditingScreenNew
import com.vladusecho.schoolevents.presentation.screen.newScreen.ParticipantsScreenNew
import com.vladusecho.schoolevents.presentation.util.UserRole
import com.vladusecho.schoolevents.presentation.viewModel.AuthViewModel

fun NavGraphBuilder.mainNavigation(
    navigationState: NavigationState
) {
    navigation<Screen.MainGraph>(
        startDestination = Screen.Events
    ) {
        composable<Screen.Events> {
            val authViewModel: AuthViewModel = hiltViewModel()
            val userRole = authViewModel.userRole.collectAsState().value

            MainScreenNew(
                onEventClick = { eventId: Int ->
                    if (userRole == UserRole.STUDENT || userRole == UserRole.DIRECTOR) {
                        navigationState.navigateToDetail(eventId)
                    } else {
                        navigationState.navigateToEventEditing(eventId)
                    }
                },
                onListClick = { eventId: Int ->
                    navigationState.navigateToParticipants(eventId)
                },
                onNewsClick = { newsId: Int ->
                    if (userRole == UserRole.STUDENT || userRole == UserRole.DIRECTOR) {
                        navigationState.navigateToNewsDetail(newsId)
                    } else {
                        navigationState.navigateToNewsEditing(newsId)
                    }
                },
                onAddEventClick = {
                    navigationState.navigateToEventCreation()
                },
                onAddNewsClick = {
                    navigationState.navigateToNewsCreation()
                }
            )
        }
        composable<Screen.EventDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EventDetails>()
            EventDetailsScreenNew(
                eventId = args.id,
                onBackClick = {
                    navigationState.navHostController.navigateUp()
                }
            )
        }
        composable<Screen.EventEditing> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EventEditing>()
            EventEditingScreenNew(
                eventId = args.id,
                onBackClick = { navigationState.navHostController.navigateUp() }
            )
        }
        composable<Screen.EventCreation> {
            EventCreationScreenNew(
                onBackClick = { navigationState.navHostController.navigateUp() }
            )
        }
        composable<Screen.NewsCreation> {
            NewsCreationScreenNew(
                onBackClick = { navigationState.navHostController.navigateUp() }
            )
        }
        composable<Screen.NewsDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.NewsDetails>()
            NewsDetailsScreenNew(
                newsId = args.id,
                onBackClick = { navigationState.navHostController.navigateUp() }
            )
        }
        composable<Screen.NewsEditing> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.NewsEditing>()
            NewsEditingScreenNew(
                newsId = args.id,
                onBackClick = { navigationState.navHostController.navigateUp() }
            )
        }
        composable<Screen.Participants> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.Participants>()
            ParticipantsScreenNew(
                eventId = args.eventId,
                onBackClick = {
                    navigationState.navHostController.navigateUp()
                }
            )
        }
    }
}
