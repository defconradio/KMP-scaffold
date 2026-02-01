package com.example.shared.presentation.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
actual fun SystemBarColoring() {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as? Activity)?.window
        // We use the same hardcoded color or theme color as the reference
        // In the reference they used DarkBlue.toArgb().
        // Here we can use MaterialTheme.colorScheme.background or a specific color if defined.
        // Assuming we want to match the theme background for a consistent dark look.
        
        val color = MaterialTheme.colorScheme.background.toArgb()

        window?.let {
             SideEffect {
                @Suppress("DEPRECATION")
                it.statusBarColor = color
                @Suppress("DEPRECATION")
                it.navigationBarColor = color // This sets the bottom system tray color
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(it, view).isAppearanceLightNavigationBars = false
            }
        }
    }
}
