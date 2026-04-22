package com.vladusecho.schoolevents.presentation.navigation.navGraphBuilder

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.vladusecho.schoolevents.presentation.navigation.NavigationState
import com.vladusecho.schoolevents.presentation.navigation.Screen
import com.vladusecho.schoolevents.presentation.screen.LoginScreen
import com.vladusecho.schoolevents.presentation.screen.RegistrationScreen
import com.vladusecho.schoolevents.presentation.screen.StartAppScreen

fun NavGraphBuilder.authNavigation(
    navigationState: NavigationState
) {
    navigation<Screen.AuthGraph>(
        startDestination = Screen.StartApp
    ) {
        composable<Screen.StartApp> {

            StartAppScreen(
                onNavigateToLogin = {
                    navigationState.navigateToSecondary(Screen.Login(it))
                },
                onNavigateToRegistration = {
                    navigationState.navigateToSecondary(Screen.Registration(it))
                }
            )
        }
        composable<Screen.Login> { backStackEntry ->

            val args = backStackEntry.toRoute<Screen.Login>()

            LoginScreen(
                email = args.email,
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
        composable<Screen.Registration> { backStackEntry ->
            RegistrationScreen(
                email = backStackEntry.toRoute<Screen.Registration>().email,
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