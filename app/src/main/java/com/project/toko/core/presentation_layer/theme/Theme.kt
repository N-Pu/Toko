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
    surface = DarkBottomBarColor

    )

private val LightColorScheme = lightColorScheme(
    primary = MainBackgroundColor,
    onBackground = LightBottomBarColor,
    secondary = SoftGreen,
    tertiary = LightGreen,
    background = LightBackgroundColor,
    scrim = LightIconTint,
    surface = LightBottomBarColor

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
//    val view = LocalView.current
//    val window = (view.context as Activity).window
//    window.statusBarColor = if (darkTheme) DarkColorScheme.background.copy(alpha = 0.7f)
//        .toArgb() else LightColorScheme.background.copy(alpha = 0.7f).toArgb()
//    window.navigationBarColor = if (darkTheme) DarkColorScheme.background.copy(alpha = 0.0f)
//        .toArgb() else LightColorScheme.background.copy(alpha = 0.0f).toArgb()
//    window.statusBarColor = DarkColorScheme.background
//        .toArgb()
//        Color.Transparent.toArgb()


    val systemUiController = rememberSystemUiController()
    if (darkTheme) {
        systemUiController.setSystemBarsColor(
            color = DarkColorScheme.surface.copy(0.4f),
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = DarkColorScheme.surface,
            darkIcons = false
        )
    } else {
        systemUiController.setSystemBarsColor(
            color = LightColorScheme.surface.copy(1f),
            darkIcons = true
        )
        systemUiController.setNavigationBarColor(
            color = LightColorScheme.surface,
            darkIcons = true
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )

}