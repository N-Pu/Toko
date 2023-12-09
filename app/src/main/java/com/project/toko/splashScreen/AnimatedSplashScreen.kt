package com.project.toko.splashScreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.project.toko.R
import kotlinx.coroutines.delay

@Composable
fun AnimatedSplashScreen(
    navController: NavController,
    svgImageLoader: ImageLoader,
    splashShow: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    var firstPawStartAnimation by remember { mutableStateOf(false) }
    var secondPawStartAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 3000), label = ""
    )
    val firstPawAnim = animateFloatAsState(
        targetValue = if (firstPawStartAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000), label = ""
    )
    val secondPawAnim = animateFloatAsState(
        targetValue = if (secondPawStartAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000), label = ""
    )
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000)
        firstPawStartAnimation = true
        delay(1000)
        secondPawStartAnimation = true
        delay(1000)
        splashShow()
        navController.popBackStack()
    }
    Splash(alphaAnim.value, svgImageLoader, firstPawAnim.value, secondPawAnim.value)
}

@Composable
private fun Splash(
    alpha: Float,
    svgImageLoader: ImageLoader,
    pawAlpha: Float,
    secondPawAlpha: Float
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.raw.toko_text_logo, imageLoader = svgImageLoader
                ), contentDescription = null, modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.6f)
                    .alpha(alpha)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f).padding(end = 20.dp), horizontalArrangement = Arrangement.End) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.raw.paw, imageLoader = svgImageLoader
                    ), contentDescription = null, modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .fillMaxHeight()
                        .alpha(pawAlpha)
                )
            }


            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.raw.paw, imageLoader = svgImageLoader
                    ), contentDescription = null, modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight()
                        .scale(1.55f)
                        .alpha(secondPawAlpha)
                )
            }
        }
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashPreview() {
    Splash(alpha = 1f, ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build(), 1f, 1f)
}