package com.example.shared.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Trade : Screen("trade", "Trade", Icons.Default.ShoppingCart)
    data object Profile : Screen("profile", "Profile", Icons.Default.Person)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}
