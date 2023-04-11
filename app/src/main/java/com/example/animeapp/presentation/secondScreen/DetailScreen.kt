package com.example.animeapp.presentation.secondScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.presentation.navigation.Screen
import com.example.animeapp.viewModel.DetailScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine


@Composable
fun DetailScreen(navController: NavHostController) {

    val viewModel2 =
        androidx.lifecycle.viewmodel.compose.viewModel<DetailScreenViewModel>() // temp name
    val animeDetail by viewModel2.animeDetails.collectAsStateWithLifecycle()
    Text(text = "If it works - text will be a little bit lower")


    val painter = rememberAsyncImagePainter(model = animeDetail?.images?.webp?.large_image_url)

    Log.d("ANIMEDETAIL", animeDetail.toString()) // somehow it's empty


    Box(modifier = Modifier.fillMaxSize()) {


        animeDetail?.let { Text(text = it.title) }
        Image(
            painter = painter,
            contentDescription = "Image",
            modifier = Modifier.fillMaxSize()
        )

    }

    Text(text = "HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEY")


    navController.navigate(route = Screen.Home.route)
    navController.popBackStack()
}