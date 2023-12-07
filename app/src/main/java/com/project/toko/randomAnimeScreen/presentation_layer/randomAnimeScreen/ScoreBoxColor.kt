package com.project.toko.randomAnimeScreen.presentation_layer.randomAnimeScreen

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.project.toko.core.presentation_layer.theme.Blank
import com.project.toko.core.presentation_layer.theme.ScoreColors.Red
import com.project.toko.core.presentation_layer.theme.Yellow


@Composable
fun scoreColor(selectedRating: Float): Color {
    return when (selectedRating) {
        in 0.01..3.99 -> {
           Red
        }

        in 4.00..6.99 -> {
           Yellow
        }

        in 7.00..10.0 -> {
            MaterialTheme.colorScheme.secondary
        }

        else -> Blank
    }
}