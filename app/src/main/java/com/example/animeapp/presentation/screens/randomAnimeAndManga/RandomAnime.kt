package com.example.animeapp.presentation.screens.randomAnimeAndManga

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
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
import com.example.animeapp.presentation.animations.LoadingAnimation
import com.example.animeapp.presentation.screens.homeScreen.navigateToDetailScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    val clickableModifier = modifier.clickable {
        state?.let { animeData ->
            navigateToDetailScreen(navController, animeData.mal_id)
        }
    }
    var cardIsShown by remember {
        mutableStateOf(true)
    }
    Column {
        if (cardIsShown) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.84f)
            ) {
                Card(
                    modifier = modifier
                        .size(550.dp)
                        .background(Color.Blue)
                        .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                offsetX.value += dragAmount.x
                                offsetY.value += dragAmount.y

                                if (offsetX.value <= -750f && offsetY.value >= -550f) {
                                    println("LEFT " + " x " + offsetX.value + " y " + offsetY.value)
                                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                                        cardIsShown = false
                                        viewModel.onTapRandomAnime()
                                        clicked = true
                                        delay(1000L)
                                        cardIsShown = true
                                        offsetX.value = 0.0f
                                        offsetY.value = 0.0f
                                    }
                                }
                                if (offsetY.value <= -550f) {
                                    println("UP" + " y " + offsetY.value)
                                    state?.let { animeData ->
                                        navigateToDetailScreen(navController, animeData.mal_id)
                                    }

                                }
                            }
                        }
                ) {
                    Column {
                        state?.let {
                            Text(
                                modifier = modifier.shadow(
                                    115.dp,
                                    shape = AbsoluteCutCornerShape(40.dp)
                                ),
                                text = it.title,
                                textAlign = TextAlign.Center,
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.Black,
                                letterSpacing = 5.sp, lineHeight = 35.sp
                            )
                        }
                    }
                    Image(
                        painter = painter,
                        contentDescription = "Anime ${state?.title}",
                        modifier = modifier
                            .fillMaxSize()
                            .then(clickableModifier)
                    )
                }
            }
        } else {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.84f)
            ) {
                LoadingAnimation()
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


//@Preview(showSystemUi = true)
//@Composable
//fun DraggablePreview() {
//
//    val offsetX = remember { mutableStateOf(0f) }
//    val offsetY = remember { mutableStateOf(0f) }
//
//    Column {
//        Row(
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.fillMaxSize(1f)
//        ) {
//            Card(
//                modifier = Modifier
//                    .size(250.dp)
//                    .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
//                    .pointerInput(Unit) {
//                        detectDragGestures { change, dragAmount ->
//                            change.consume()
//                            offsetX.value += dragAmount.x
//                            offsetY.value += dragAmount.y
//
//                            if (offsetX.value <= -250f && offsetY.value >= -550f) {
//                                println("LEFT " + " x " + offsetX.value + " y " + offsetY.value)
//                                state?.let { animeData ->
//                                        navigateToDetailScreen(navController, animeData.mal_id)
//                                    }
////                                offsetX.value = 0.0f
////                                offsetY.value = 0.0f
//
//                            }
//                            if (offsetY.value <= -551f) {
//                                println("UP" + " y " + offsetY.value)
//                                viewModel.viewModelScope.launch(Dispatchers.IO) {
//                                        cardIsShown = false
//                                        viewModel.onTapRandomAnime()
//                                        clicked = true
//                                        delay(1000L)
//                                        cardIsShown = true
//                                        offsetX = 0f
//                                        offsetY = 0f
//                                    }
////                                offsetY.value = 0.0f
//                            }
//                        }
//                    }
//
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "x = " + offsetX.value.toString() + "/" + "y = " + offsetY.value.toString(),
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
//}