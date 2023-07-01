package com.example.animeapp.presentation.screens.randomAnimeAndManga

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.domain.viewModel.IdViewModel
import com.example.animeapp.domain.viewModel.RandomAnimeViewModel
import com.example.animeapp.presentation.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ShowRandomScreen(navController: NavController, viewModelProvider: ViewModelProvider) {
//    if (navController.previousBackStackEntry?.destination?.route == DetailOnCast.value){
//        navController.popBackStack(DetailOnCast.value, inclusive = true)
//    }

    Column(
        modifier = Modifier
//            .fillMaxSize()
    ) {

        ShowRandomAnime(navController, viewModelProvider)

    }


}

@Composable
fun ShowRandomAnime(navController: NavController, viewModelProvider: ViewModelProvider) {
    val state =
        viewModelProvider[RandomAnimeViewModel::class.java].animeDetails.collectAsStateWithLifecycle()
    val idViewModel = viewModelProvider[IdViewModel::class.java]
    val coroutineScope = rememberCoroutineScope()
    var clicked by remember { mutableStateOf(false) }

    val painter =
        rememberAsyncImagePainter(model = state.value?.images?.jpg?.large_image_url)

    val clickableModifier = Modifier.clickable {
        state.value?.let { animeData ->
            idViewModel.setId(animeData.mal_id)
            navController.navigate(route = "detail_screen/${animeData.mal_id}") {
                launchSingleTop = true
                popUpTo(Screen.Home.route) {
                    saveState = true
                }
                restoreState = true
            }
        }
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.84f)
    ) {
        Box {
            Card(modifier = Modifier.size(450.dp)) {
                Image(
                    painter = painter,
                    contentDescription = "Anime ${state.value?.title}",
                    modifier = Modifier
                        .fillMaxSize()
                        .then(clickableModifier)
                )

            }
            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxHeight()) {
                state.value?.let {
                    Text(
                        modifier = Modifier.shadow(115.dp, shape = AbsoluteCutCornerShape(40.dp)),
                        text = it.title,
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Black,
                        letterSpacing = 5.sp, lineHeight = 35.sp
                    )
                }
            }
        }


    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f),
        verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center
    ) {


        RandomizerAnimeButton(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    viewModelProvider[RandomAnimeViewModel::class.java].onTapRandomAnime()
                    clicked = true
                }
            }
        )
    }
}

@Composable
fun RandomizerAnimeButton(onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Blue,
            containerColor = Color.Gray
        ), modifier = Modifier
            .height(200.dp)
            .width(114.dp)
    ) {
        Text(text = "Hit me!", fontSize = 20.sp)
    }


}