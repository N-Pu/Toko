package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter

@Composable
 fun DisplayPicture(
    painter: AsyncImagePainter,
    modifier: Modifier,
) {


    Box {
        Image(
            painter = painter,
            contentDescription = "Big anime picture",
            contentScale = ContentScale.FillWidth,
            modifier = modifier,
        )
    }
}