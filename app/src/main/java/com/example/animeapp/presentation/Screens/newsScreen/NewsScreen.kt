package com.example.animeapp.presentation.Screens.newsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.animeapp.presentation.navigation.DetailOnCast

@Composable
fun News(navController: NavController) {
    if (navController.previousBackStackEntry?.destination?.route == DetailOnCast.value){
        navController.popBackStack(DetailOnCast.value, inclusive = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {

        Text(text = "NEWS")

    }


}
