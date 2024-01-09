package com.project.toko.core.presentation_layer.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//@Composable
//fun LoadingAnimation(
//    modifier: Modifier = Modifier,
//    circleSize: Dp = 25.dp,
//    circleColor: Color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
//    spaceBetween: Dp = 10.dp,
//    travelDistance: Dp = 20.dp
//) {
//    val circles = remember {
//        List(3) {
//            Animatable(initialValue = 0f)
//        }
//    }
//
//    circles.forEachIndexed { index, animatable ->
//        LaunchedEffect(key1 = animatable) {
//            delay(index * 100L)
//            animatable.animateTo(
//                targetValue = 1f,
//                animationSpec = infiniteRepeatable(
//                    animation = keyframes {
//                        durationMillis = 1200
//                        0.0f at 0 with LinearOutSlowInEasing
//                        1.0f at 300 with LinearOutSlowInEasing
//                        0.0f at 600 with LinearOutSlowInEasing
//                        0.0f at 1200 with LinearOutSlowInEasing
//                    },
//                    repeatMode = RepeatMode.Restart
//                )
//            )
//        }
//    }
//
//    val circleValues = circles.map { it.value }
//    val distance = with(LocalDensity.current) { travelDistance.toPx() }
//
//    Box(
//        modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.primary)
//            .wrapContentSize(Alignment.Center)
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(spaceBetween)
//        ) {
//            circleValues.forEach { value ->
//                Box(
//                    modifier = Modifier
//                        .size(circleSize)
//                        .graphicsLayer {
//                            translationY = -value * distance
//                        }
//                        .background(
//                            color = circleColor,
//                            shape = CircleShape
//                        )
//                )
//            }
//        }
//    }
//}


@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 25.dp,
    circleColor: Color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
    spaceBetween: Dp = 10.dp,
    travelDistance: Dp = 20.dp
) {
    val scope = rememberCoroutineScope()

    val distancePx = with(LocalDensity.current) { travelDistance.toPx() }
    val keyframesSpec = remember {
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1200
                0.0f at 0 with LinearOutSlowInEasing
                1.0f at 300 with LinearOutSlowInEasing
                0.0f at 600 with LinearOutSlowInEasing
                0.0f at 1200 with LinearOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        )
    }

    val circles = remember {
        List(3) { Animatable(initialValue = 0f) }
    }

    LaunchedEffect(true) {
        circles.forEachIndexed { index, animatable ->
            scope.launch(Dispatchers.IO) {
                delay(index * 100L)
                animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = keyframesSpec
                )
            }
        }
    }

    val circleValues = circles.map { it.value }

    Box(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .wrapContentSize(Alignment.Center)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spaceBetween)
        ) {
            circleValues.forEach { value ->
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .graphicsLayer {
                            translationY = -value * distancePx
                        }
                        .background(
                            color = circleColor,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}


@Preview
@Composable
fun prev() {
    LoadingAnimation()
}