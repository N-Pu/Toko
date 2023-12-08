package com.project.toko.core.presentation_layer.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.project.toko.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily (
            Font(R.font.montserrat_medium, FontWeight.ExtraLight),
            Font(R.font.montserrat_medium, FontWeight.Normal),
            Font(R.font.montserrat_medium, FontWeight.Normal, FontStyle.Italic),
            Font(R.font.montserrat_medium, FontWeight.Medium),
            Font(R.font.montserrat_medium, FontWeight.ExtraBold)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val evolventaBoldFamily = FontFamily (
//    Font(R.font.evolventa_bold, FontWeight.ExtraLight),
//    Font(R.font.evolventa_bold, FontWeight.Normal),
//    Font(R.font.evolventa_bold, FontWeight.Normal, FontStyle.Italic),
//    Font(R.font.evolventa_bold, FontWeight.Medium),
    Font(R.font.evolventa_bold, FontWeight.W900)
)
