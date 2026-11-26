package com.example.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shared.presentation.navigation.Screen
import com.example.shared.presentation.ui.theme.AppTheme
import com.example.shared.screens.ScreenA
import com.example.shared.screens.ScreenB
import com.example.shared.screens.ScreenC

@Composable
fun App() {
    AppTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        fun navigateToTab(screen: Screen) {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Screen.bottomBarItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = { navigateToTab(screen) }
                        )
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                composable(Screen.Home.route) {
                    ScreenA(onNavigateToTrade = { navigateToTab(Screen.Trade) })
                }
                composable(Screen.Trade.route) {
                    ScreenB(
                        onNavigateToProfile = { navigateToTab(Screen.Profile) },
                        onNavigateBack = { navigateToTab(Screen.Home) }
                    )
                }
                composable(Screen.Profile.route) {
                    ScreenC(
                        onNavigateToHome = { navigateToTab(Screen.Home) },
                        onNavigateBack = { navigateToTab(Screen.Trade) }
                    )
                }
                composable(Screen.Settings.route) {
                    ScreenA(onNavigateToTrade = { navigateToTab(Screen.Trade) })
                }
            }
        }
    }
}
