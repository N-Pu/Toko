package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.project.toko.detailScreen.model.pictureModel.DetailPicturesData

@Composable
fun ShowPictureAlbum(
    isDialogShown: MutableState<Boolean>,
    picturesData: List<DetailPicturesData>,
    modifier: Modifier
) {
    if (picturesData.isNotEmpty() && isDialogShown.value) {
        Dialog(onDismissRequest = {
            isDialogShown.value = false
        }) {
            DisplayHorizontalPagerWithIndicator(picturesData, modifier)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DisplayHorizontalPagerWithIndicator(
    picturesData: List<DetailPicturesData>,
    modifier: Modifier
) {
    val pagerState =
        rememberPagerState {
            picturesData.size
        }
    Column {
        val fling = PagerDefaults.flingBehavior(
            state = pagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(5)
        )
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            if (picturesData.isNotEmpty()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = modifier
                        .size(500.dp),
                    reverseLayout = false, flingBehavior = fling,
                ) { page ->
                    val painter =
                        rememberAsyncImagePainter(model = picturesData[page].jpg.large_image_url)
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = modifier
                            .fillMaxSize().padding(horizontal = 10.dp),
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