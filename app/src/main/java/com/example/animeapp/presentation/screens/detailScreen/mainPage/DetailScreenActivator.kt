package com.example.animeapp.presentation.screens.detailScreen.mainPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import coil.compose.AsyncImagePainter
import androidx.compose.ui.layout.ContentScale
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
//    val height = painter.state.painter?.intrinsicSize?.height
//    val weight = painter.state.painter?.intrinsicSize?.width


//    Box(modifier = Modifier.size(600.dp)) {

        Image(
            painter = painter,
            contentDescription = "Big anime picture",

            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
//                    .fillMaxWidth()
//                                .align(Alignment.TopCenter)
//                .size(200.dp)
//                    .aspectRatio(height / weight.toFloat())

        )

//    }

//    Log.d("height", height.toString())
//    Log.d("height", height.toString())
//    Log.d("height", height.toString())
//    Log.d("height", height.toString())
//    Log.d("height", height.toString())
//    Log.d("height", height.toString())


}

//@Composable
//fun DisplayPicture(painter: AsyncImagePainter) {
//
//    val imageState = painter.state
//    var height: Float? by remember { mutableStateOf(null) }
//    var width: Float? by remember { mutableStateOf(null) }
//    val isLoaded = remember {
//        mutableStateOf(false)
//    }
//
//    LaunchedEffect(imageState) {
//        isLoaded.value = false
//        val image = imageState.painter
//        val jobFirst = launch {
//            do {
//                height = image?.intrinsicSize?.height
//            } while (height == null)
//        }
//        val jobSecond = launch {
//            do {
//                width = image?.intrinsicSize?.width
//            } while (width == null)
//        }
//
//        awaitAll(jobFirst, jobSecond)
//        isLoaded.value = true
//    }
//
//    if (isLoaded.value) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            Image(
//                painter = painter,
//                contentDescription = "Big anime picture",
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(height!! / width!!)
//            )
//        }
//
//    }
//}

//@Composable
//fun DisplayPicture(painter: AsyncImagePainter) {
//    val imageState = painter.state
//    var height: Dimension? by remember { mutableStateOf(null) }
//    var width: Float? by remember { mutableStateOf(null) }
//    val isLoaded = remember { mutableStateOf(false) }
//    val coroutineScope = rememberCoroutineScope()
//
//    LaunchedEffect(imageState) {
//        isLoaded.value = false
//      height =  painter.request.sizeResolver.size().height
//        isLoaded.value = true
////        coroutineScope.launch {
////            val image = imageState.painter
////
////            val jobFirst = async (Dispatchers.IO) {
////                delay(5000L)
////                    height = image?.intrinsicSize?.height
////                Log.d("height", height.toString())
////                }
////
////            val jobSecond = async(Dispatchers.IO) {
////                    delay(5000L)
////                    width = image?.intrinsicSize?.width
////
////            }
////
////            awaitAll(jobFirst, jobSecond)
////            isLoaded.value = true
////        }
//    }
//
//    if (isLoaded.value) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            Image(
//                painter = painter,
//                contentDescription = "Big anime picture",
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(height)
//            )
//        }
//    }}
