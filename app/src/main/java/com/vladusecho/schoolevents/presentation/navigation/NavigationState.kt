package com.vladusecho.schoolevents.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vladusecho.schoolevents.domain.entity.Profile

class NavigationState(
    val navHostController: NavHostController
) {

    fun navigateToTab(route: Any) {
        navHostController.navigate(route) {
            // only one copy of screen we can use
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            // only one same screen at the top
            launchSingleTop = true
            // save screen state after another screen
            restoreState = true
        }
    }

    fun navigateToDetail(eventId: Int) {
        navigateToSecondary(Screen.EventDetails(eventId))
    }

    fun navigateToEventEditing(eventId: Int) {
        navigateToSecondary(Screen.EventEditing(eventId))
    }

    fun navigateToEventCreation() {
        navigateToSecondary(Screen.EventCreation)
    }

    fun navigateToNewsCreation() {
        navigateToSecondary(Screen.NewsCreation)
    }

    fun navigateToProfileEditing(profile: Profile) {
        navigateToSecondary(Screen.ProfileEditing(profile))
    }

    fun navigateToSecondary(route: Any) {
        navHostController.navigate(route) {
            launchSingleTop = true
        }
    }
}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return NavigationState(navHostController)
}
