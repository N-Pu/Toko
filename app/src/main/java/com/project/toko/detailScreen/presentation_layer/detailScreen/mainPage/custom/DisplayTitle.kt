package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
 fun DisplayTitle(title: String, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(1f),
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top
    ) {
        Text(
            text = title,
            fontSize = 40.sp,
            modifier = modifier.fillMaxWidth(),
            fontWeight = FontWeight.SemiBold,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                shadow = Shadow(
                    offset = Offset(x = 0f, y = 6f),
                    blurRadius = 5f,
                    color = Color.Black.copy(alpha = 0.5f)
                )
            ), textAlign = TextAlign.Center
        )
    }
}