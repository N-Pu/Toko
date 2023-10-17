package com.project.toko.core.presentation_layer.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable

import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = MainBackgroundColor,
    secondary = SoftGreen,
    tertiary = LightGreen,
    background = DarkBackgroundColor,
    scrim = DarktIconTint,
    onBackground = DarkBottomBarColor,

    )

private val LightColorScheme = lightColorScheme(
    primary = MainBackgroundColor,
    onBackground = LightBottomBarColor,
    secondary = SoftGreen,
    tertiary = LightGreen,
    background = LightBackgroundColor,
    scrim = LightIconTint

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TokoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    if (darkTheme) {
        systemUiController.setSystemBarsColor(color = DarkColorScheme.background, darkIcons = false)
        systemUiController.setNavigationBarColor(
            color = DarkColorScheme.background,
            darkIcons = false
        )
    } else {
        systemUiController.setSystemBarsColor(color = LightColorScheme.background, darkIcons = true)
        systemUiController.setNavigationBarColor(
            color = LightColorScheme.background,
            darkIcons = true
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )

}