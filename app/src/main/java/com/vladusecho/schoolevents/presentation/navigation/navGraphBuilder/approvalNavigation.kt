package com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.vladusecho.schoolevents.presentation.navigation.NavigationState
import com.vladusecho.schoolevents.presentation.navigation.Screen
import com.vladusecho.schoolevents.presentation.screen.newScreen.ApprovalScreenNew
import com.vladusecho.schoolevents.presentation.screen.newScreen.EventDetailsScreenNew

fun NavGraphBuilder.approvalNavigation(
    navigationState: NavigationState
) {
    navigation<Screen.ApprovalGraph>(
        startDestination = Screen.Approval
    ) {
        composable<Screen.Approval> {
            ApprovalScreenNew(
                onEventClick = { eventId ->
                    navigationState.navigateToDetail(eventId)
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
    }
}
