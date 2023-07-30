package com.project.toko.presentation.screens.detailScreen.sideContent.bottomSheetActivatorButton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.project.toko.presentation.theme.LightGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetButton(modifier: Modifier, coroutine: CoroutineScope, rememberSheetState: BottomSheetScaffoldState) {
    Row(
        modifier = modifier.fillMaxWidth(1f),
        horizontalArrangement = Arrangement.Center
    ) {

        FilledIconButton(
            colors = IconButtonDefaults.iconButtonColors(containerColor = LightGreen),
            onClick = {
                coroutine.launch(Dispatchers.IO) {
                    rememberSheetState.bottomSheetState.expand()
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Show more"
            )
        }
    }
}