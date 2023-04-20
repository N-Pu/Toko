package com.example.animeapp.presentation.secondScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import com.example.animeapp.presentation.navigation.Screen
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.unit.dp





// this is the problem, it's cycling
@Composable
fun DetailScreen(navController: NavHostController, id: Int) {
    navController.navigate(route = Screen.Home.route)
    navController.popBackStack()
    ActivateDetailScreen(id = id)
//    CharactersViewModel().addCharacterAndSeyu(id)

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

