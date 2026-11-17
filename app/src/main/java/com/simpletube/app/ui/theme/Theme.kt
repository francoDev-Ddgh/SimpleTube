package com.simpletube.app.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColors = darkColorScheme(
    primary = TubeRed,
    onPrimary = TubeWhite,
    primaryContainer = Color(0xFF8B0000),
    onPrimaryContainer = TubeWhite,
    secondary = Color(0xFF8AB4F8),
    background = TubeDark,
    onBackground = TubeWhite,
    surface = TubeSurface,
    onSurface = TubeWhite,
    surfaceVariant = TubeSurfaceVariant,
    onSurfaceVariant = TubeGrey,
    error = Color(0xFFFFB4AB)
)

private val LightColors = lightColorScheme(
    primary = TubeRed,
    onPrimary = TubeWhite,
    background = Color(0xFFFDFDFD),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun SimpleTubeTheme(
    darkTheme: Boolean = true,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
