package com.example.animeapp.presentation.newsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun News(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {

        Text(text = "NEWS")

    }


}
