package com.example.shared.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    secondary = LightBlue,
    onSecondary = TextWhite,
    background = DarkBlue,
    onBackground = TextWhite,
    surface = DarkerBlue,
    onSurface = TextWhite,
    surfaceVariant = LightBlue,
    onSurfaceVariant = TextWhite,
    error = PrimaryRed
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    SystemBarColoring()

    MaterialTheme(
        colorScheme = DarkColorPalette,
        content = content
    )
}

@Composable
expect fun SystemBarColoring()
