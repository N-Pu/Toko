package com.project.toko.randomAnimeScreen.presentation_layer.randomAnimeScreen

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.project.toko.core.presentation_layer.theme.ScoreColors


@Composable
fun ScoreColor(selectedRating: Float): Color {
    return when (selectedRating) {
        in 0.01..3.99 -> {
            ScoreColors.Red
        }

        in 4.00..6.99 -> {
            ScoreColors.Yellow
        }

        in 7.00..10.0 -> {
            MaterialTheme.colorScheme.secondary
        }

        else -> ScoreColors.Blank
    }
}