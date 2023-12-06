package com.project.toko.core.presentation_layer.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController


//private val DarkColorScheme = darkColorScheme(
//    primary =ThemeColors.Night.background,
//    onPrimary =ThemeColors.Night.text,
//    primaryContainer =ThemeColors.Night.unTouchedButton,
//    onPrimaryContainer =ThemeColors.Night.touchedButton,
//    inversePrimary =ThemeColors.Night.secondaryText,
//    secondary =ThemeColors.Night.primary,
//    onSecondary =ThemeColors.Night.scoreCircleColors,
//    secondaryContainer =ThemeColors.Night.,
//    onSecondaryContainer =ThemeColors.Night.,
//    tertiary =ThemeColors.Night.,
//    onTertiary =ThemeColors.Night.,
//    tertiaryContainer =ThemeColors.Night.,
//    onTertiaryContainer =ThemeColors.Night.,
//    background =ThemeColors.Night.,
//    onBackground =ThemeColors.Night.,
//    surface =ThemeColors.Night.,
//)


private val DarkColorScheme = darkColorScheme(
    primary = ThemeColors.Night.background,
    onPrimary = ThemeColors.Night.text,
    primaryContainer = ThemeColors.Night.unTouchedButton,
    onPrimaryContainer = ThemeColors.Night.touchedButton,
    inversePrimary = ThemeColors.Night.secondaryText,
    secondary = ThemeColors.Night.primary,
    onSecondary = ThemeColors.Night.circleMore,
    secondaryContainer = Yellow,
    onSecondaryContainer = Green,
    tertiary = Blank,
    onTertiary = ThemeColors.Night.scoreCircleSideNumber,
    tertiaryContainer = ThemeColors.Night.searchBackground,
    onTertiaryContainer = ThemeColors.Night.castAndStaffCard,
    background = ThemeColors.Night.addDialog,
    onBackground = ThemeColors.Night.systemBar,
    surface = ThemeColors.Night.systemBarIcon,
    onSurface =ThemeColors.Night.dividerColor,
    surfaceVariant = ThemeColors.Night.customDialog,
    onSurfaceVariant = ThemeColors.Night.textDelimiter,
    surfaceTint = ThemeColors.Night.sideBackground,
    inverseSurface =ThemeColors.Night.sideBarCustomRow,
//    inverseOnSurface =,
//    error =,
    onError =ThemeColors.Night.iconColor,
    errorContainer =ThemeColors.Night.backgroundDetailScreen,
//    onErrorContainer =,
    error =ThemeColors.Night.topSearchColor,
    outlineVariant =ThemeColors.Day.white,
//    scrim =
)
private val LightColorScheme = lightColorScheme(
    primary = ThemeColors.Day.background,
    onPrimary = ThemeColors.Day.text,
    primaryContainer = ThemeColors.Day.unTouchedButton,
    onPrimaryContainer = ThemeColors.Day.touchedButton,
    inversePrimary = ThemeColors.Day.secondaryText,
    secondary = ThemeColors.Day.primary,
    onSecondary = ThemeColors.Day.circleMore,
    secondaryContainer = Yellow,
    onSecondaryContainer = Green,
    tertiary = Blank,
    onTertiary = ThemeColors.Day.scoreCircleSideNumber,
    tertiaryContainer = ThemeColors.Day.searchBackground,
    onTertiaryContainer = ThemeColors.Day.castAndStaffCard,
    background = ThemeColors.Day.addDialog,
    onBackground = ThemeColors.Day.systemBar,
    surface = ThemeColors.Day.systemBarIcon,
    onSurface =ThemeColors.Day.dividerColor,
    surfaceVariant = ThemeColors.Day.customDialog,
    onSurfaceVariant = ThemeColors.Day.textDelimiter,
    surfaceTint = ThemeColors.Day.sideBackground,
    inverseSurface =ThemeColors.Day.sideBarCustomRow,
//    inverseOnSurface =,
//    error =,
    onError =ThemeColors.Day.iconColor,
//    errorContainer =,
    errorContainer =ThemeColors.Day.backgroundDetailScreen,
    error =ThemeColors.Day.topSearchColor,
    outlineVariant =ThemeColors.Day.white,
//    scrim =
)
//private val DarkColorScheme = darkColorScheme(
//    primary = MainBackgroundColor,
//    secondary = SoftGreen,
//    tertiary = LightGreen,
//    background = DarkBackgroundColor,
//    scrim = DarktIconTint,
//    onBackground = DarkBottomBarColor,
//    surface = DarkBottomBarColor
//    )
//
//private val LightColorScheme = lightColorScheme(
//    primary = MainBackgroundColor,
//    onBackground = LightBottomBarColor,
//    secondary = SoftGreen,
//    tertiary = LightGreen,
//    background = LightBackgroundColor,
//    scrim = LightIconTint,
//    surface = LightBottomBarColor
//)

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
//        systemUiController.setSystemBarsColor(
//            color = DarkColorScheme.surface.copy(0.4f),
//            darkIcons = false
//        )
//        systemUiController.setNavigationBarColor(
//            color = DarkColorScheme.surface,
//            darkIcons = false
//        )
        systemUiController.setSystemBarsColor(
            color = LightColorScheme.onPrimary,
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = LightColorScheme.onPrimary,
            darkIcons = false
        )


    } else {
        systemUiController.setSystemBarsColor(
            color = DarkColorScheme.onPrimary,
            darkIcons = true
        )
        systemUiController.setNavigationBarColor(
            color = DarkColorScheme.onPrimary,
            darkIcons = true
        )
    }

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )

}