package com.example.foodorderav.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx. compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose. ui.graphics.toArgb
import androidx.compose. ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Orange500,
    onPrimary = White,
    primaryContainer = Orange100,
    onPrimaryContainer = Orange600,
    secondary = Green500,
    onSecondary = White,
    secondaryContainer = Green100,
    onSecondaryContainer = Green600,
    tertiary = Yellow500,
    onTertiary = Gray900,
    tertiaryContainer = Yellow100,
    onTertiaryContainer = Gray900,
    error = Red500,
    onError = White,
    errorContainer = Red100,
    onErrorContainer = Red500,
    background = Gray50,
    onBackground = Gray900,
    surface = White,
    onSurface = Gray900,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray700,
    outline = Gray300,
    outlineVariant = Gray300
)

private val DarkColorScheme = darkColorScheme(
    primary = OrangeDark,
    onPrimary = Gray900,
    primaryContainer = Orange600,
    onPrimaryContainer = Orange100,
    secondary = GreenDark,
    onSecondary = Gray900,
    secondaryContainer = Green600,
    onSecondaryContainer = Green100,
    background = BackgroundDark,
    onBackground = Gray100,
    surface = SurfaceDark,
    onSurface = Gray100
)

@Composable
fun FoodOrderAVTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // TẮT để dùng màu custom
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}