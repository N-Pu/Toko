package com.project.toko.core.presentation_layer.theme


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Light
val MainBackgroundColor = Color(0xFFFEFBF0)
val DialogColor = Color(0xE5494949)
val DialogSideColor = Color(0xFF343434)

val LightGreen = Color(0xFF68BEAE)
val SoftGreen = Color(0xFF076D63)

val LightBackgroundColor = Color(0xFFF4F4F4)
val LightIconTint = Color(154, 154, 154)
val LightBottomBarColor = Color(255, 255, 255)
val LightCardColor = Color(255, 255, 255)

val threeLines = Color(65, 65, 65)

val iconColorInSearchPanel = Color(0, 0, 0, 77)
private val FirstBarColor = Color(104, 190, 174, 161)
private val SecondBarColor = Color(255, 255, 255, 0)
val SearchBarColor = Brush.linearGradient(
    0.0f to FirstBarColor,
//    0.3f to SecondBarColor,
    1.0f to SecondBarColor,
    start = Offset(0.0f, 0.0f),
    end = Offset(600.0f, 0.0f))

val SectionColor = Brush.linearGradient(
    0.0f to FirstBarColor,
//    0.3f to SecondBarColor,
    1.0f to SecondBarColor,
    start = Offset(0.0f, 0.0f),
    end = Offset(750.0f, 0.0f))

val BackArrowCastColor = Brush.linearGradient(
    0.0f to FirstBarColor,
//    0.3f to SecondBarColor,
    1.0f to SecondBarColor,
    start = Offset(0.0f, 0.0f),
    end = Offset(950.0f, 0.0f))

val BackArrowSecondCastColor = Brush.linearGradient(
    0.0f to FirstBarColor,
//    0.3f to SecondBarColor,
    1.0f to SecondBarColor,
    start = Offset(0.0f, 0.0f),
    end = Offset(450.0f, 0.0f))

//    .linearGradient(
// 0.0f to FirstBarColor,
//    50.0f to SecondBarColor,
//    start = Offset.Zero,
//    end = Offset.Infinite
//)

//    Color(104, 190, 174, 1).copy(1F, 255f, 255f, 255f)

// Dark
val CardBackgroundColor = Color(0xFF494949)
val DarkBackgroundColor = Color(0xFF414141)
val DarktIconTint = Color(255, 255, 255, 178)
val DarkBottomBarColor = Color(49, 49, 49, 255)
object ScoreColors {
    val Red = Color(255, 77, 87)
    val Yellow =  Color(255, 160, 0)
    val Green = Color(104, 190, 174)
    val Blank = Color(0xFFA2ADB1)
}

    private val watching = Color(189, 218, 213)
    private val planned = Color(157, 214, 204)
    private val completed = Color(126, 206, 192)
    private val dropped = Color(104, 190, 174)
    private val favorite = Color(69, 167, 149)
    private val person = Color(46, 135, 118)
    private val character = Color(34, 124, 107, 255)

val favoriteTopBarColors = listOf(watching, planned, completed, dropped, favorite,person,
    character)