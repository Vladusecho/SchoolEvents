package com.vladusecho.schoolevents.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vladusecho.schoolevents.presentation.navigation.AppNavGraph
import com.vladusecho.schoolevents.presentation.navigation.NavigationState
import com.vladusecho.schoolevents.presentation.navigation.StudentNavItem
import com.vladusecho.schoolevents.presentation.navigation.rememberNavigationState
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily
import com.vladusecho.schoolevents.presentation.ui.theme.SchoolEventsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolEventsTheme {

                val navState = rememberNavigationState()

                Scaffold(
                    bottomBar = {
                        EventsNavigationBottom(navState)
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(bottom = paddingValues.calculateBottomPadding()),
                    ) {
                        AppNavGraph(navState)
                    }
                }
            }
        }
    }
}

@Composable
fun EventsNavigationBottom(
    navState: NavigationState
) {
    val navBackStackEntry by navState.navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navItems = listOf(
        StudentNavItem.Events,
        StudentNavItem.Favourite,
        StudentNavItem.Profile,
    )

    Column() {
        HorizontalDivider(thickness = 0.5.dp, color = Color(0xff0DCDAA))
        NavigationBar(
            containerColor = Color.White
        ) {
            navItems.forEach { navItem ->

                val isSelected =
                    navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == navItem.screen.route
                    } ?: false

                // Delete default ripple indication
                CompositionLocalProvider(LocalRippleConfiguration provides null) {
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            navState.navigateTo(navItem.screen.route)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(navItem.iconId),
                                "",
                                modifier = Modifier.offset(y = (5.dp)),
                                tint = if (isSelected) Color.Black else Color(
                                    0xffBDBDBD
                                )
                            )
                        },
                        label = {
                            Text(
                                text = navItem.title,
                                fontFamily = EventsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (isSelected) Color.Black else Color(
                                    0xffBDBDBD
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}