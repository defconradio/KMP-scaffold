package com.example.shared

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.example.shared.presentation.navigation.Screen
import com.example.shared.presentation.ui.theme.AppTheme
import com.example.shared.screens.ScreenA
import com.example.shared.screens.ScreenB
import com.example.shared.screens.ScreenC

sealed class RootScreen {
    object Home : RootScreen()
    object Trade : RootScreen()
    object Profile : RootScreen()
    object Settings : RootScreen()
}

@Composable
fun App() {
    AppTheme {
        var currentScreen by remember { mutableStateOf<RootScreen>(RootScreen.Home) }
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    val items = listOf(
                        Screen.Home to RootScreen.Home,
                        Screen.Trade to RootScreen.Trade,
                        Screen.Profile to RootScreen.Profile,
                        Screen.Settings to RootScreen.Settings
                    )
                    items.forEach { (screen, rootScreen) ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentScreen == rootScreen,
                            onClick = { currentScreen = rootScreen }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                when (currentScreen) {
                    RootScreen.Home -> ScreenA(onNavigate = { currentScreen = it })
                    RootScreen.Trade -> ScreenB(onNavigate = { currentScreen = it })
                    RootScreen.Profile -> ScreenC(onNavigate = { currentScreen = it })
                    RootScreen.Settings -> ScreenA(onNavigate = { currentScreen = it }) // Reuse for now
                }
            }
        }
    }
}
