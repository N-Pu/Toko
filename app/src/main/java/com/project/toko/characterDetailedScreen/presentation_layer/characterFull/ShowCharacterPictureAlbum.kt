package com.project.toko.characterDetailedScreen.presentation_layer.characterFull

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.project.toko.characterDetailedScreen.model.characterPictures.CharacterPicturesData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowCharacterPictureAlbum(
    isDialogShown: MutableState<Boolean>,
    picturesData: List<CharacterPicturesData>,
    modifier: Modifier
) {
    if (picturesData.isNotEmpty() && isDialogShown.value) {
        AlertDialog(
            onDismissRequest = { isDialogShown.value = false },
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true,
                usePlatformDefaultWidth = false
            ), modifier = modifier
                .fillMaxWidth()
        ) {
            DisplayHorizontalPagerWithIndicator(picturesData, modifier, isDialogShown)
        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DisplayHorizontalPagerWithIndicator(
    picturesData: List<CharacterPicturesData>,
    modifier: Modifier,
    isDialogShown: MutableState<Boolean>
) {
    val pagerState =
        rememberPagerState {
            picturesData.size
        }
    val allowTheGesture = remember { mutableStateOf(false) }
    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(5)
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Row(horizontalArrangement = Arrangement.SpaceAround) {
            if (picturesData.isNotEmpty()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = modifier
                        .size(500.dp),
                    reverseLayout = false, flingBehavior = fling,
                ) { page ->
                    val painter =
                        rememberAsyncImagePainter(model = picturesData[page].jpg.image_url)
                    var offset by remember { mutableStateOf(Offset.Zero) }
                    var zoom by remember { mutableFloatStateOf(1f) }

                    val customModifier = if (allowTheGesture.value) modifier
                        .pointerInput(Unit) {
                            detectTapGestures(onDoubleTap = { tapOffset ->
                                zoom = if (zoom > 1f) {
                                    allowTheGesture.value = false
                                    1f
                                } else {
                                    allowTheGesture.value = true
                                    2f

                                }
                                offset = calculateDoubleTapOffset(zoom, size, tapOffset)

                            })
                        }
                        .pointerInput(Unit) {
                            detectTransformGestures(
                                onGesture = { centroid, pan, gestureZoom, _ ->
                                    offset = offset.calculateNewOffset(
                                        centroid, pan, zoom, gestureZoom, size
                                    )
                                    zoom = maxOf(1f, zoom * gestureZoom)
                                }
                            )
                        }
                        .graphicsLayer {
                            translationX = -offset.x * zoom
                            translationY = -offset.y * zoom
                            scaleX = zoom; scaleY = zoom
                            transformOrigin = TransformOrigin(0f, 0f)
                        }
                        .fillMaxSize()
                        .padding(horizontal = 10.dp) else modifier
                        .pointerInput(Unit) {
                            detectTapGestures(onDoubleTap = { tapOffset ->
                                zoom = if (zoom > 1f) {
                                    allowTheGesture.value = false
                                    1f
                                } else {
                                    allowTheGesture.value = true
                                    2f
                                }
                                offset = calculateDoubleTapOffset(zoom, size, tapOffset)
                            })
                        }
                        .graphicsLayer {
                            translationX = -offset.x * zoom
                            translationY = -offset.y * zoom
                            scaleX = zoom; scaleY = zoom
                            transformOrigin = TransformOrigin(0f, 0f)
                        }
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = customModifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp),
                    )
                }
            }
        }


        // Dot indicators
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .clickable { isDialogShown.value = false }

        ) {
            repeat(picturesData.size) { index ->
                val color =
                    if (index == pagerState.currentPage) Color(202, 202, 202, 255) else Color(
                        217,
                        217,
                        217,
                        122
                    )
                Box(
                    modifier = modifier
                        .size(15.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

private fun calculateDoubleTapOffset(
    zoom: Float, size: IntSize, tapOffset: Offset
): Offset {
    val newOffset = Offset(tapOffset.x, tapOffset.y)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}


private fun Offset.calculateNewOffset(
    centroid: Offset,
    pan: Offset,
    zoom: Float,
    gestureZoom: Float,
    size: IntSize
): Offset {
    val newScale = maxOf(1f, zoom * gestureZoom)
    val newOffset = (this + centroid / zoom) -
            (centroid / newScale + pan / zoom)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}
