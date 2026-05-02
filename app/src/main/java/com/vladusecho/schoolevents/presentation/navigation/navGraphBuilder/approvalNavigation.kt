package com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.vladusecho.schoolevents.presentation.navigation.NavigationState
import com.vladusecho.schoolevents.presentation.navigation.Screen
import com.vladusecho.schoolevents.presentation.screen.ApprovalScreen

fun NavGraphBuilder.approvalNavigation(
    navigationState: NavigationState
) {
    navigation<Screen.ApprovalGraph>(
        startDestination = Screen.Approval
    ) {
        composable<Screen.Approval> {
            ApprovalScreen(
                onEventClick = { eventId ->
                    navigationState.navigateToDetail(eventId)
                },
                onListClick = { eventId ->
                    navigationState.navigateToParticipants(eventId)
                }
            )
        }
    }
}
