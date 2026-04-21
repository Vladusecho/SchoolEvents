package com.vladusecho.schoolevents.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.vladusecho.schoolevents.domain.entity.Profile
import com.vladusecho.schoolevents.presentation.navigation.ProfileNavType
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
        navigation<Screen.MainGraph>(
            startDestination = Screen.Events
        ) {
            composable<Screen.Events> {
                MainScreen(
                    onEventClick = { eventId ->
                        navigationState.navigateToDetail(eventId)
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
        }
        navigation<Screen.FavouriteGraph>(
            startDestination = Screen.Favourite
        ) {
            composable<Screen.Favourite> {
                FavouriteScreen(
                    onEventClick = {
                        navigationState.navigateToDetail(it)
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
        }
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
}
