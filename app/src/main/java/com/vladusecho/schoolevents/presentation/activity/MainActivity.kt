package com.vladusecho.schoolevents.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
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
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import com.vladusecho.schoolevents.presentation.util.UserRole
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
            val authViewModel: AuthViewModel = hiltViewModel()
            val isAuth by authViewModel.isAuth.collectAsState()
            val userRole by authViewModel.userRole.collectAsState()
            val isDarkThemePref by authViewModel.isDarkTheme.collectAsState()

            val darkTheme = isDarkThemePref ?: isSystemInDarkTheme()

            SchoolEventsTheme(darkTheme = darkTheme) {

                val navState = rememberNavigationState()
                val navBackStackEntry by navState.navHostController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                if (isAuth != null) {
                    Box(
                        modifier = Modifier,
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        val showBottomBar = currentDestination?.hierarchy?.any {
                            it.hasRoute(Screen.AuthGraph::class)
                        } == false

                        if (showBottomBar) {
                            EventsNavigationBottom(
                                navState = navState, userRole = userRole
                            )
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
    modifier: Modifier = Modifier,
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
        StudentNavItem.Approval,
        StudentNavItem.Profile,
    )

    val currentNavItems = when (userRole) {
        UserRole.STUDENT -> studentNavItems
        UserRole.ORGANIZER -> organizerNavItems
        UserRole.DIRECTOR -> directorNavItems
    }

    Box(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 60.dp)
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Row(
            modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
        ) {
            currentNavItems.forEach { tab ->
                // Check if current route matches main screen or any related sub-screens
                val isSelected =
                    navBackStackEntry?.destination?.hierarchy?.any {
                        it.hasRoute(tab.screen::class)
                    } ?: false

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .weight(1f)
                        .background(if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background)
                        .clickable {
                            navState.navigateToTab(tab.screen)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(tab.iconId),
                            contentDescription = "tab",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = tab.title,
                            fontFamily = EventsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            }
        }
    }
}