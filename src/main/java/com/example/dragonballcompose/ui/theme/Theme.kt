package com.example.dragonballcompose.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Gold = Color(0xFFFFD700)
val GoldDark = Color(0xFFB8860B)
val BackgroundDark = Color(0xFF0D0D0D)
val CardBackground = Color(0xFF1A1A1A)
val TextPrimary = Color(0xFFEEEEEE)
val TextSecondary = Color(0xFF888888)
val ErrorColor = Color(0xFFFF4444)

private val DarkColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = Color.Black,
    secondary = GoldDark,
    onSecondary = Color.Black,
    background = BackgroundDark,
    surface = CardBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = ErrorColor
)

@Composable
fun DragonBallTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
