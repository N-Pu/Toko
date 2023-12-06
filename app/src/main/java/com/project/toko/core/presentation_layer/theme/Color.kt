package com.project.toko.core.presentation_layer.theme


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

sealed class ThemeColors(
    val background: Color,
    val text: Color,
    val unTouchedButton: Color,
    val touchedButton: Color,
    val secondaryText: Color,
    val primary: Color, // main color
//    val scoreCircleColors: Any,
    val scoreCircleSideNumber: Color,
    val searchBackground: Color,
    val castAndStaffCard: Color,
    val addDialog: Color,
    val systemBar: Color,
    val systemBarIcon: Color,
    val favoriteTopBarDaoItems: List<Color>,
    val customDialog: Color,
    val textDelimiter: Color,
    val sideBackground: Color,
    val sideBarCustomRow: Color,
    val topSearchColor: Color,
    val white : Color = Color.White,
    val backgroundDetailScreen: Color,
    val iconColor: Color,
    val dividerColor: Color,
    val circleMore: Color
) {

    object Night : ThemeColors(
        background = darkBackground,
        text = Color.White,
        unTouchedButton = darkUnTouchedButton,
        touchedButton = LightPink,
        secondaryText = darkSecondaryText,
        primary = LightPink,
        scoreCircleSideNumber = scoreCircleSideNumber,
        searchBackground = blackSearchBackground,
        castAndStaffCard = darkCastAndStaffColor,
        addDialog = darkAddDialog,
        systemBar = blackSearchBackground,
        systemBarIcon = darkSystemBarUIcon,
        favoriteTopBarDaoItems = darkFavoriteTopBarColors,
        customDialog = darkCustomDialog,
        textDelimiter = darkTextDelimiter,
        sideBackground= Color(26, 26, 26),
        sideBarCustomRow = Color(56, 56, 56, 255),
        topSearchColor = Color.Black,
        backgroundDetailScreen = darkBackground,
        iconColor = Color(211, 211, 211, 255),
        dividerColor = Color(255, 255, 255, 61),
        circleMore = Color(102,102,102)

    )

    object Day : ThemeColors(
        background = lightBackground,
        text = Color.Black,
        unTouchedButton = lightUnTouchedButton,
        touchedButton = LightGreen,
        secondaryText = darkBackground,
        primary = LightGreen,
//        scoreCircleColors = ScoreColors,
        scoreCircleSideNumber = scoreCircleSideNumber,
        searchBackground = lightSearchBackground,
        castAndStaffCard = Color.White,
        addDialog = darkBackground,
        systemBar = lightSearchBackground,
        systemBarIcon = lightSystemBarUIcon,
        favoriteTopBarDaoItems = lightFavoriteTopBarColors,
        customDialog = lightCustomDialog,
        textDelimiter = lightTextDelimiter,
        sideBackground= Color(251, 251, 251, 255),
        sideBarCustomRow = Color(104, 190, 174, 61),
        topSearchColor = Color.White,
        backgroundDetailScreen = Color(243,243,243),
        iconColor = Color(65, 65, 65, 255),
        dividerColor = Color(0, 0, 0, 87),
        circleMore = Color(204,204,204)
    )

}

// light scheme colors

val lightBackground = Color(0xFFF3F3F3)
val lightTextDelimiter = Color(157, 157, 157)
val lightUnTouchedButton = Color(210, 210, 210, 255)
val LightGreen = Color(0xFF68BDAD)
val lightSearchBackground = Color(255, 255, 255, 153)
val lightSystemBarUIcon = Color(0, 0, 0, 64)
val lightCustomDialog = Color(244, 244, 244)

private val watching = Color(189, 218, 213)
private val planned = Color(157, 214, 204)
private val completed = Color(126, 206, 192)
private val dropped = Color(104, 190, 174)
private val favorite = Color(69, 167, 149)
private val person = Color(46, 135, 118)
private val character = Color(34, 124, 107, 255)

val lightFavoriteTopBarColors = listOf(
    watching, planned, completed, dropped, favorite, person,
    character
)

// dark scheme colors
val darkBackground = Color(65, 65, 65)
val darkTextDelimiter = Color(1f, 1f, 1f, 0.35f)
val darkUnTouchedButton = Color(51, 51, 51, 255)
val darkSecondaryText = Color(209, 209, 209)
val LightPink = Color(0xFFD87DC3)
val blackSearchBackground = Color(0, 0, 0, 153)
val darkCastAndStaffColor = Color(73, 73, 73)
val darkAddDialog = Color(198, 198, 198, 194)
val darkSystemBarUIcon = Color(186, 186, 186)
val darkCustomDialog = Color(129, 129, 129, 166)

private val darkWatching = Color(219, 147, 203, 255)
private val darkPlanned = Color(216, 125, 195, 255)
private val darkCompleted = Color(234, 118, 207, 255)
private val darkDropped = Color(213, 111, 189, 255)
private val darkFavorite = Color(190, 80, 165, 255)
private val darkPerson = Color(177, 71, 153, 255)
private val darkCharacter = Color(150, 44, 126, 255)

val darkFavoriteTopBarColors = listOf(
    darkWatching, darkPlanned, darkCompleted, darkDropped, darkFavorite, darkPerson,
    darkCharacter
)

// unchangeable colors
val scoreCircleSideNumber = Color(188, 188, 188, 255)
val Yellow = Color(255, 160, 0)
val Green = Color(104, 190, 174)
val Blank = Color(0xFFA2ADB1)
object ScoreColors {
    val Red = Color(255, 77, 87)
    val Yellow = Color(255, 160, 0)
    val Blank = Color(0xFFA2ADB1)
}


// Light
val DialogColor = Color(0xE5494949)



val scoreBoardColor = Brush.linearGradient(
    0.0f to Color(0f, 0f, 0f, 0.59f),
    0.7f to Color(0, 0, 0, 1),
    1.0f to Color(0, 0, 0, 1),
    start = Offset(0.0f, 0.0f),
    end = Offset(0.0f, 500.0f)
)
val iconColorInSearchPanel = Color(0, 0, 0, 77)
private val FirstBarColor = Color(104, 190, 174, 161)
private val SecondBarColor = Color(255, 255, 255, 0)

private val DarkFirstBarColor = Color(193, 116, 175, 255)

val SearchBarColor = Brush.linearGradient(
    0.0f to FirstBarColor,
    1.0f to SecondBarColor,
    start = Offset(0.0f, 0.0f),
    end = Offset(600.0f, 0.0f)
)

val DarkSearchBarColor = Brush.linearGradient(
    0.0f to DarkFirstBarColor,
    1.0f to SecondBarColor,
    start = Offset(0.0f, 0.0f),
    end = Offset(600.0f, 0.0f)
)


val SectionColor = Brush.linearGradient(
    0.0f to FirstBarColor,
    1.0f to SecondBarColor,
    start = Offset(0.0f, 0.0f),
    end = Offset(750.0f, 0.0f)
)
val DarkSectionColor = Brush.linearGradient(
    0.0f to DarkFirstBarColor,
    1.0f to SecondBarColor,
    start = Offset(0.0f, 0.0f),
    end = Offset(750.0f, 0.0f)
)


val BackArrowCastColor = Brush.linearGradient(
    0.0f to FirstBarColor,
    1.0f to SecondBarColor,
    start = Offset(0.0f, 0.0f),
    end = Offset(450.0f, 0.0f)
)

val DarkBackArrowCastColor = Brush.linearGradient(
    0.0f to DarkFirstBarColor,
    1.0f to SecondBarColor,
    start = Offset(0.0f, 0.0f),
    end = Offset(450.0f, 0.0f)
)

val BackArrowSecondCastColor = Brush.linearGradient(
    0.0f to FirstBarColor,
    1.0f to SecondBarColor,
    start = Offset(0.0f, 0.0f),
    end = Offset(450.0f, 0.0f)
)


val DarkBackArrowSecondCastColor = Brush.linearGradient(
    0.0f to DarkFirstBarColor,
    1.0f to SecondBarColor,
    start = Offset(0.0f, 0.0f),
    end = Offset(450.0f, 0.0f)
)


