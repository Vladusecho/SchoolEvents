package com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder

import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.vladusecho.schoolevents.presentation.navigation.NavigationState
import com.vladusecho.schoolevents.presentation.navigation.Screen
import com.vladusecho.schoolevents.presentation.screen.EventCreationScreen
import com.vladusecho.schoolevents.presentation.screen.EventDetailsScreen
import com.vladusecho.schoolevents.presentation.screen.EventEditingScreen
import com.vladusecho.schoolevents.presentation.screen.MainScreen
import com.vladusecho.schoolevents.presentation.screen.NewsCreationScreen
import com.vladusecho.schoolevents.presentation.screen.NewsDetailsScreen
import com.vladusecho.schoolevents.presentation.screen.NewsEditingScreen
import com.vladusecho.schoolevents.presentation.screen.UserRole
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

            MainScreen(
                onEventClick = { eventId ->
                    if (userRole == UserRole.STUDENT) {
                        navigationState.navigateToDetail(eventId)
                    } else {
                        navigationState.navigateToEventEditing(eventId)
                    }
                },
                onNewsClick = { newsId ->
                    if (userRole == UserRole.STUDENT) {
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
            EventDetailsScreen(
                eventId = args.id,
                onBackClick = {
                    navigationState.navHostController.navigateUp()
                }
            )
        }
        composable<Screen.EventEditing> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EventEditing>()
            EventEditingScreen(
                eventId = args.id,
                onBackClick = { navigationState.navHostController.navigateUp() }
            )
        }
        composable<Screen.EventCreation> {
            EventCreationScreen(
                onBackClick = { navigationState.navHostController.navigateUp() }
            )
        }
        composable<Screen.NewsCreation> {
            NewsCreationScreen(
                onBackClick = { navigationState.navHostController.navigateUp() }
            )
        }
        composable<Screen.NewsDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.NewsDetails>()
            NewsDetailsScreen(
                newsId = args.id,
                onBackClick = { navigationState.navHostController.navigateUp() }
            )
        }
        composable<Screen.NewsEditing> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.NewsEditing>()
            NewsEditingScreen(
                newsId = args.id,
                onBackClick = { navigationState.navHostController.navigateUp() }
            )
        }
    }
}
