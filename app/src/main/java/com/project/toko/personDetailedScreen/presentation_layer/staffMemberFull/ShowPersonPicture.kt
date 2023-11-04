package com.project.toko.personDetailedScreen.presentation_layer.staffMemberFull

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowPersonPicture(
    painter: Painter,
    modifier: Modifier,
    isDialogShown: MutableState<Boolean>
) {

    Column(
        modifier = modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight()
            .padding(horizontal = 20.dp)

        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painter,
            contentDescription = "Character picture",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxSize()
                .clip(CardDefaults.shape)
                .combinedClickable(onLongClick = { isDialogShown.value = true }) { }
        )
    }
}