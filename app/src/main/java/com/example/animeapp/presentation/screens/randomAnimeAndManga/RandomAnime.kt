package com.example.animeapp.presentation.screens.randomAnimeAndManga

//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.AbsoluteCutCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewModelScope
//import androidx.navigation.NavController
//import coil.compose.rememberAsyncImagePainter
//import com.example.animeapp.domain.viewModel.RandomAnimeViewModel
//import com.example.animeapp.presentation.screens.homeScreen.navigateToDetailScreen
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlin.math.roundToInt
//
//
//@Composable
//fun ShowRandomScreen(
//    navController: NavController,
//    viewModelProvider: ViewModelProvider,
//    modifier: Modifier
//) {
//    Column(
//        modifier = modifier
//    ) {
//        ShowRandomAnime(navController, viewModelProvider, modifier)
//    }
//}
//
//@Composable
//fun ShowRandomAnime(
//    navController: NavController,
//    viewModelProvider: ViewModelProvider,
//    modifier: Modifier
//) {
//    val viewModel =
//        viewModelProvider[RandomAnimeViewModel::class.java]
//    val state by viewModel.animeDetails.collectAsStateWithLifecycle()
//    var clicked by remember { mutableStateOf(false) }
//    val painter =
//        rememberAsyncImagePainter(model = state?.images?.jpg?.large_image_url)
//
//    val clickableModifier = modifier.clickable {
//        state?.let { animeData ->
//            navigateToDetailScreen(navController, animeData.mal_id)
//        }
//    }
//
//    var offsetX by remember { mutableStateOf(0f) }
//    var offsetY by remember { mutableStateOf(0f) }
//
//
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .fillMaxHeight(0.84f)
//    ) {
//        Box {
//            Card(
//                modifier = modifier
//                    .size(450.dp)
//                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
//                    .background(Color.Blue)
//                    .size(50.dp)
//                    .pointerInput(Unit) {
//                        detectDragGestures { change, dragAmount ->
//                            change.consume()
//                            offsetX += dragAmount.x
//                            offsetY += dragAmount.y
//                        }
//                    }) {
//                Image(
//                    painter = painter,
//                    contentDescription = "Anime ${state?.title}",
//                    modifier = modifier
//                        .fillMaxSize()
//                        .then(clickableModifier)
//                )
//
//            }
//            Box(contentAlignment = Alignment.BottomCenter, modifier = modifier.fillMaxHeight()) {
//                state?.let {
//                    Text(
//                        modifier = modifier.shadow(115.dp, shape = AbsoluteCutCornerShape(40.dp)),
//                        text = it.title,
//                        textAlign = TextAlign.Center,
//                        fontSize = 40.sp,
//                        fontWeight = FontWeight.Light,
//                        color = Color.Black,
//                        letterSpacing = 5.sp, lineHeight = 35.sp
//                    )
//                }
//            }
//        }
//
//
//    }
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .fillMaxHeight(0.4f),
//        verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center
//    ) {
//
//
//        RandomizerAnimeButton(
//            onClick = {
//                viewModel.viewModelScope.launch(Dispatchers.IO) {
//                    viewModelProvider[RandomAnimeViewModel::class.java].onTapRandomAnime()
//                    clicked = true
//                }
//            }, modifier = modifier
//        )
//    }
//}


//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectTransformGestures
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.AbsoluteCutCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewModelScope
//import androidx.navigation.NavController
//import coil.compose.rememberAsyncImagePainter
//import com.example.animeapp.domain.viewModel.RandomAnimeViewModel
//import com.example.animeapp.presentation.screens.homeScreen.navigateToDetailScreen
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlin.math.roundToInt
//
//@Composable
//fun ShowRandomScreen(
//    navController: NavController,
//    viewModelProvider: ViewModelProvider,
//    modifier: Modifier
//) {
//    Column(
//        modifier = modifier
//    ) {
//        ShowRandomAnime(navController, viewModelProvider, modifier)
//    }
//}
//
//@Composable
//fun ShowRandomAnime(
//    navController: NavController,
//    viewModelProvider: ViewModelProvider,
//    modifier: Modifier
//) {
//    val viewModel =
//        viewModelProvider[RandomAnimeViewModel::class.java]
//    val state by viewModel.animeDetails.collectAsStateWithLifecycle()
//    var clicked by remember { mutableStateOf(false) }
//    val painter =
//        rememberAsyncImagePainter(model = state?.images?.jpg?.large_image_url)
//
////
////    val clickableModifier = modifier.clickable {
////        state?.let { animeData ->
////            navigateToDetailScreen(navController, animeData.mal_id)
////        }
////    }
//
//    var offsetX by remember { mutableStateOf(0f) }
//    var offsetY by remember { mutableStateOf(0f) }
//
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .fillMaxHeight(0.84f)
//    ) {
//        Box {
//            Card(
//                modifier = modifier
//                    .size(450.dp)
//                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
//                    .background(Color.Blue)
//                    .size(50.dp)
//                    .pointerInput(Unit) {
//                        detectTransformGestures { _, panZoom, _, _ ->
//                            offsetX += panZoom.x
//                            offsetY += panZoom.y
//
//                            // Calculate the distance moved in the X and Y directions
////                            val dx = panZoom.x.absoluteValue
////                            val dy = panZoom.y.absoluteValue
//
//                            if (offsetY < -50) {
//                                // Swiped up more than to the left or right, and the offset is negative (upward)
//                                state?.let { animeData ->
//                                    navigateToDetailScreen(navController, animeData.mal_id)
//                                }
//                            } else {
//                                // Swiped mainly left/right or not enough upwards, reset the offsetY
//                                offsetY = 0f
//                            }
//
//                            if (offsetX < -50) {
//                                viewModel.viewModelScope.launch(Dispatchers.IO) {
//                                    viewModelProvider[RandomAnimeViewModel::class.java].onTapRandomAnime()
//                                    clicked = true
//                                }
//                            } else {
//                                offsetX = 0f
//                            }
//                        }
//                    }) {
//                Image(
//                    painter = painter,
//                    contentDescription = "Anime ${state?.title}",
//                    modifier = modifier
//                        .fillMaxSize()
////                        .then(clickableModifier)
//                )
//            }
//            Box(contentAlignment = Alignment.BottomCenter, modifier = modifier.fillMaxHeight()) {
//                state?.let {
//                    Text(
//                        modifier = modifier.shadow(115.dp, shape = AbsoluteCutCornerShape(40.dp)),
//                        text = it.title,
//                        textAlign = TextAlign.Center,
//                        fontSize = 40.sp,
//                        fontWeight = FontWeight.Light,
//                        color = Color.Black,
//                        letterSpacing = 5.sp, lineHeight = 35.sp
//                    )
//                }
//            }
//        }
//    }


//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .fillMaxHeight(0.4f),
//        verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center
//    ) {
//        RandomizerAnimeButton(
//            onClick = {
//                viewModel.viewModelScope.launch(Dispatchers.IO) {
//                    viewModelProvider[RandomAnimeViewModel::class.java].onTapRandomAnime()
//                    clicked = true
//                }
//            }, modifier = modifier
//        )
//    }
//}


import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun ShowRandomScreen(
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        ShowRandomAnime(
            navController,
            modifier,
            viewModel = viewModelProvider[RandomAnimeViewModel::class.java]
        )
    }
}

@Composable
fun ShowRandomAnime(
    navController: NavController,
    modifier: Modifier,
    viewModel: RandomAnimeViewModel
) {

    val state by viewModel.animeDetails.collectAsStateWithLifecycle()
    var clicked by remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter(model = state?.images?.jpg?.large_image_url)

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val clickableModifier = modifier.clickable {
        state?.let { animeData ->
            navigateToDetailScreen(navController, animeData.mal_id)
        }
    }
    var cardIsShown by remember {
        mutableStateOf(true)
    }
    if (cardIsShown) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.84f)
        ) {
            Card(
                modifier = modifier
                    .size(450.dp)
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .background(Color.Blue)
                    .size(50.dp)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, panZoom, _, _ ->
                            offsetX += panZoom.x
                            offsetY += panZoom.y

                            // Calculate the distance moved in the X and Y directions
                            val dx = offsetX.absoluteValue
                            val dy = offsetY.absoluteValue

                            if (dy > 50f) {
                                // Swiped up more than to the left or right, and the offset is negative (upward)
                                state?.let { animeData ->
                                    navigateToDetailScreen(navController, animeData.mal_id)
                                }
                            } else {
                                // Swiped mainly left/right or not enough upwards, reset the offsetY
                                offsetY = 0f
                            }

                            if (dx > 250f && dy == 0f) {
                                viewModel.viewModelScope.launch(Dispatchers.IO) {
                                    cardIsShown = false
                                    viewModel.onTapRandomAnime()
                                    clicked = true
                                    delay(1000L)
                                    cardIsShown = true
                                    offsetX = 0f
                                    offsetY = 0f
                                }
                            }
//                            else {
//                                offsetX = 0f
//                            }
                        }
                    }) {
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
                    viewModel.onTapRandomAnime()
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