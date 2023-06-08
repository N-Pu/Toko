package com.example.animeapp.presentation.screens.detailScreen.mainPage


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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


    val rememberVMState = remember {
        viewModelProvider
    }
    ActivateDetailScreen(
        viewModelProvider = rememberVMState,
        navController = navController,

        )


}


@Composable
fun DisplayPicture(painter: AsyncImagePainter) {

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painter,
            contentDescription = "Big anime picture",

            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .height(600.dp)

        )
    }
}

