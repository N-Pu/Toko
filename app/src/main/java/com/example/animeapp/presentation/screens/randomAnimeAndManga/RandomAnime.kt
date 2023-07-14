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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.domain.viewModel.RandomAnimeViewModel
import com.example.animeapp.presentation.screens.homeScreen.navigateToDetailScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun ShowRandomScreen(navController: NavController, viewModelProvider: ViewModelProvider, modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        ShowRandomAnime(navController, viewModelProvider, modifier)
    }
}

@Composable
fun ShowRandomAnime(navController: NavController, viewModelProvider: ViewModelProvider, modifier: Modifier) {
    val viewModel =
        viewModelProvider[RandomAnimeViewModel::class.java]
    val state by viewModel.animeDetails.collectAsStateWithLifecycle()
    var clicked by remember { mutableStateOf(false) }
    val painter =
        rememberAsyncImagePainter(model = state?.images?.jpg?.large_image_url)

    val clickableModifier = modifier.clickable {
        state?.let { animeData ->
            navigateToDetailScreen(navController, animeData.mal_id)
        }
    }


    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.84f)
    ) {
        Box {
            Card(modifier = modifier.size(450.dp)) {
                Image(
                    painter = painter,
                    contentDescription = "Anime ${state?.title}",
                    modifier = modifier
                        .fillMaxSize()
                        .then(clickableModifier)
                )

            }
            Box(contentAlignment = Alignment.BottomCenter, modifier = modifier.fillMaxHeight()) {
                state?.let {
                    Text(
                        modifier = modifier.shadow(115.dp, shape = AbsoluteCutCornerShape(40.dp)),
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
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f),
        verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center
    ) {


        RandomizerAnimeButton(
            onClick = {
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModelProvider[RandomAnimeViewModel::class.java].onTapRandomAnime()
                    clicked = true
                }
            }, modifier = modifier
        )
    }
}

@Composable
fun RandomizerAnimeButton(onClick: () -> Unit, modifier: Modifier) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Blue,
            containerColor = Color.Gray
        ), modifier = modifier
            .height(200.dp)
            .width(114.dp)
    ) {
        Text(text = "Hit me!", fontSize = 20.sp)
    }


}