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
val LightBottomBarColor = Color(255, 255, 255).copy(0.6f)
val LightCardColor = Color(255, 255, 255)


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
val DarkBottomBarColor = Color(0,0,0,).copy(0.6f)
object ScoreColors {
    val Red = Color(255, 77, 87)
    val Yellow =  Color(255, 160, 0)
    val Green = Color(104, 190, 174)
    val Blank = Color(0xFFA2ADB1)
}

