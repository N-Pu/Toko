package com.project.toko.presentation.screens.randomAnimeScreen

import androidx.compose.ui.graphics.Color
import com.project.toko.presentation.theme.ScoreColors


fun scoreColorChanger(selectedRating: Float): Color {
    return when (selectedRating) {
        in 0.01..3.99 -> {
            ScoreColors.Red
        }

        in 4.00..6.99 -> {
            ScoreColors.Yellow
        }

        in 7.00..10.0 -> {
            ScoreColors.Green
        }

        else -> ScoreColors.Red
    }
}