package com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.vladusecho.schoolevents.presentation.navigation.NavigationState
import com.vladusecho.schoolevents.presentation.navigation.Screen
import com.vladusecho.schoolevents.presentation.screen.newScreen.LoginScreenNew
import com.vladusecho.schoolevents.presentation.screen.newScreen.RegistrationScreenNew
import com.vladusecho.schoolevents.presentation.screen.newScreen.StartAppScreenNew

fun NavGraphBuilder.authNavigation(
    navigationState: NavigationState
) {
    navigation<Screen.AuthGraph>(
        startDestination = Screen.StartApp
    ) {
        composable<Screen.StartApp> {
            StartAppScreenNew(
                onLoginClick = {
                    navigationState.navigateToSecondary(Screen.Login)
                },
                onRegistrationClick = {
                    navigationState.navigateToSecondary(Screen.Registration)
                }
            )
        }
        composable<Screen.Login> {
            LoginScreenNew(
                onLoginClick = {
                    navigationState.navHostController.navigate(Screen.MainGraph) {
                        popUpTo(Screen.AuthGraph) { inclusive = true }
                    }
                },
                onBackClick = {
                    navigationState.navHostController.navigateUp()
                }
            )
        }
        composable<Screen.Registration> {
            RegistrationScreenNew(
                onRegistrationClick = {
                    navigationState.navHostController.navigate(Screen.MainGraph) {
                        popUpTo(Screen.AuthGraph) { inclusive = true }
                    }
                },
                onBackClick = {
                    navigationState.navHostController.navigateUp()
                }
            )
        }
    }
}
