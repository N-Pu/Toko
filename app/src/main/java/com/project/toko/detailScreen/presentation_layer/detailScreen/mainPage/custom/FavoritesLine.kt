package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun FavoritesLine(favorites: Int, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {

        Text(
            text = "Favorites ",
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(
                shadow = Shadow(
                    offset = Offset(x = 0f, y = 6f),
                    blurRadius = 5f,
                    color = Color.Black.copy(alpha = 0.5f)
                )
            )
        )
        Text(
            text = favorites.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(
                shadow = Shadow(
                    offset = Offset(x = 0f, y = 6f),
                    blurRadius = 5f,
                    color = Color.Black.copy(alpha = 0.5f)
                )
            )
        )
    }
}

