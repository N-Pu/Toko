package com.example.animeapp.presentation.screens.detailScreen.mainPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImagePainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController


@Composable
fun DetailScreen(
    navController: NavController,
    viewModelProvider: ViewModelProvider,

    ) {

    ActivateDetailScreen(
        viewModelProvider = viewModelProvider,
        navController = navController,
    )

}

@Composable
fun DisplayPicture(painter: AsyncImagePainter, height: Float
//                   , height: Dp, width: Dp
) {

    Image(
        painter = painter,
        contentDescription = "Big anime picture",

        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxWidth().height(height.dp)
//            .defaultMinSize(minWidth = 800.dp, minHeight = 250.dp)
//            .size(height = height, width = width)
       ,
        alignment = Alignment.TopCenter,
    )
}