package com.vladusecho.schoolevents.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vladusecho.schoolevents.presentation.navigation.AppNavGraph
import com.vladusecho.schoolevents.presentation.navigation.NavigationState
import com.vladusecho.schoolevents.presentation.navigation.Screen
import com.vladusecho.schoolevents.presentation.navigation.StudentNavItem
import com.vladusecho.schoolevents.presentation.navigation.rememberNavigationState
import com.vladusecho.schoolevents.presentation.screen.UserRole
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

val LocalUserRole = staticCompositionLocalOf<UserRole> {
    UserRole.STUDENT
}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolEventsTheme {

                val authViewModel: AuthViewModel = hiltViewModel()
                val isAuth by authViewModel.isAuth.collectAsState()
                val userRole by authViewModel.userRole.collectAsState()

                val navState = rememberNavigationState()
                val navBackStackEntry by navState.navHostController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                if (isAuth != null) {
                    Scaffold(
                        containerColor = Color.Transparent,

                        bottomBar = {
                            val showBottomBar = currentDestination?.hierarchy?.any {
                                it.hasRoute(Screen.AuthGraph::class)
                            } == false

                            if (showBottomBar) {
                                Column() {
                                    if (userRole != UserRole.STUDENT) {
                                        Button(
                                            onClick = {},
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 32.dp)
                                        ) {
                                            Text(
                                                text = "Добавить мероприятие",
                                                fontFamily = EventsFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp,
                                                color = Color.White
                                            )
                                        }
                                    }

                                    EventsNavigationBottom(
                                        navState, userRole
                                    )
                                }
                            }
                        }
                    ) { paddingValues ->
                        val padding = paddingValues.calculateBottomPadding()
                        Box(
                            modifier = Modifier
                                .padding(),
                        ) {
                            CompositionLocalProvider(
                                LocalUserRole provides userRole
                            ) {
                                AppNavGraph(
                                    navigationState = navState,
                                    startDestination = if (isAuth == true) Screen.MainGraph else Screen.AuthGraph
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EventsNavigationBottom(
    navState: NavigationState,
    userRole: UserRole
) {
    val navBackStackEntry by navState.navHostController.currentBackStackEntryAsState()

    val studentNavItems = listOf(
        StudentNavItem.Events,
        StudentNavItem.Favourite,
        StudentNavItem.Profile,
    )

    val organizerNavItems = listOf(
        StudentNavItem.Events,
        StudentNavItem.Archive,
        StudentNavItem.Profile,
    )

    val directorNavItems = listOf(
        StudentNavItem.Events,
        StudentNavItem.Archive,
        StudentNavItem.Profile,
    )

    val currentNavItems = when (userRole) {
        UserRole.STUDENT -> studentNavItems
        UserRole.ORGANIZER -> organizerNavItems
        UserRole.DIRECTOR -> directorNavItems
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
    ) {
        currentNavItems.forEach { navItem ->

            val isSelected =
                navBackStackEntry?.destination?.hierarchy?.any {
                    it.hasRoute(navItem.screen::class)
                } ?: false

            // Delete default ripple indication
            CompositionLocalProvider(LocalRippleConfiguration provides null) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navState.navigateToTab(navItem.screen)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(navItem.iconId),
                            "",
                            modifier = Modifier.offset(y = (5.dp)),
                            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f)
                        )
                    },
                    label = {
                        Text(
                            text = navItem.title,
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f)
                        )
                    }
                )
            }
        }
    }
}
