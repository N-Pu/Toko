package com.example.animeapp.presentation.firstScreen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.domain.searchModel.Data
import com.example.animeapp.presentation.navigation.Screen
import com.example.animeapp.presentation.theme.LightYellow


@Composable
fun GridAdder(
    listData: List<Data>,
    navController: NavHostController
) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(listData) { _, anime ->
            AnimeCardBox(anime = anime, navController)

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimeCardBox(
    anime: Data,
    navController: NavHostController
) {

    var isVisible by remember {
        mutableStateOf(false)
    }


    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            initialAlpha = 0.0f,
            animationSpec = TweenSpec(durationMillis = 500)
        ),
        exit = fadeOut(
            animationSpec = TweenSpec(durationMillis = 500)

        )
    ) {

        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable {
                    navController.navigate(route = "detail_screen/" + anime.mal_id) {
                        popUpTo(Screen.Detail.route) {
                            inclusive = true
                        }
                    }
                },
            colors = CardDefaults.cardColors(containerColor = LightYellow),
            shape = RectangleShape,
        ) {
            val painter = rememberAsyncImagePainter(model = anime.images.webp.image_url)



            Box {
                // Coil image loader
                Image(
                    painter = painter,
                    contentDescription = "Images for each Anime",
                    modifier = Modifier
                        .aspectRatio(9f / 11f),
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier
//                    .align(Alignment.TopStart)
                        .background(Color.White.copy(alpha = 0.5f))
                ) {


                    Text(text = anime.score.toString())
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = anime.scored_by.toString())

                }
            }

            Text(
                text = anime.title,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        delayMillis = 2000,
                        initialDelayMillis = 2000,
                        velocity = 50.dp
                    )
                    .padding(end = 16.dp, top = 16.dp, bottom = 16.dp, start = 16.dp),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight(1000),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

    }

    LaunchedEffect(anime) {
        isVisible = true
    }
}


//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun AnimeCardBox(
//    anime: Data,
//    navController: NavHostController
//) {
//
//
//
//    Card(
//        modifier = Modifier
//            .clip(RoundedCornerShape(6.dp))
//            .clickable {
//                navController.navigate(route = "detail_screen/" + anime.mal_id) {
//                    popUpTo(Screen.Detail.route) {
//                        inclusive = true
//                    }
//                }
//            },
//
//        colors = CardDefaults.cardColors(containerColor = LightYellow),
//        shape = RectangleShape
//
//    ) {
//        val painter = rememberAsyncImagePainter(model = anime.images.webp.large_image_url)
//        // Coil image loader
//        Image(
//            painter = painter,
//            contentDescription = "Images for each Anime",
//            modifier = Modifier
//                .aspectRatio(9f / 11f),
//
//            contentScale = ContentScale.FillBounds
//
//        )
//
//
//        Text(text = anime.score.toString())
//        Text(text = anime.scored_by.toString())
//
//
//
//
//        Text(
//            text = anime.title,
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .fillMaxWidth()
//                .basicMarquee(
//                    iterations = Int.MAX_VALUE,
//                    delayMillis = 2000,
//                    initialDelayMillis = 2000,
//                    velocity = 50.dp
//                ),
//            fontFamily = FontFamily.Monospace,
//            fontWeight = FontWeight(1000),
//            overflow = TextOverflow.Ellipsis,
//            maxLines = 1
//
//        )
//    }
//
//
//}






