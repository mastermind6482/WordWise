package com.mastermind.wordwise.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = PastelBlue,
    onPrimary = White,
    primaryContainer = LightPastelBlue,
    onPrimaryContainer = DarkBlue,
    
    secondary = LavenderPurple,
    onSecondary = White,
    secondaryContainer = LighterPastelBlue,
    onSecondaryContainer = DeepPurple,
    
    tertiary = MintGreen,
    onTertiary = White,
    tertiaryContainer = LightPastelGreen,
    onTertiaryContainer = DarkGreen,
    
    error = PastelRed,
    onError = White,
    errorContainer = LighterPastelRed,
    onErrorContainer = DarkRed,
    
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkGray,
    
    outline = Gray
)

private val DarkColors = darkColorScheme(
    primary = LightPastelBlue,
    onPrimary = DarkBlue,
    primaryContainer = PastelBlue.copy(alpha = 0.3f),
    onPrimaryContainer = LighterPastelBlue,
    
    secondary = LavenderPurple.copy(alpha = 0.7f),
    onSecondary = White,
    secondaryContainer = DeepPurple.copy(alpha = 0.3f),
    onSecondaryContainer = LighterPastelBlue,
    
    tertiary = LightPastelGreen,
    onTertiary = DarkGreen,
    tertiaryContainer = MintGreen.copy(alpha = 0.3f),
    onTertiaryContainer = LighterPastelGreen,
    
    error = LightPastelRed,
    onError = DarkRed,
    errorContainer = PastelRed.copy(alpha = 0.3f),
    onErrorContainer = LighterPastelRed,
    
    background = DarkBackground,
    onBackground = White,
    surface = DarkBackground,
    onSurface = White,
    
    surfaceVariant = DarkGray,
    onSurfaceVariant = LightGray,
    
    outline = Gray
)

@Composable
fun WordWiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
} 