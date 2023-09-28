package com.project.toko.core.presentation_layer.backArrowButton

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.project.toko.core.presentation_layer.theme.LightGreen

@Composable
fun BackButton(onClick: () -> Unit, modifier: Modifier) {
    Icon(
        imageVector = Icons.Outlined.ArrowBack,
        contentDescription = "Back Button",
        tint = LightGreen,
        modifier = modifier.clickable {
            onClick()
        }
    )
}