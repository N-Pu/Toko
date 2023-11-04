package com.project.toko.characterDetailedScreen.presentation_layer.characterFull

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
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
                        rememberAsyncImagePainter(model = picturesData[page].jpg.image_url)
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = modifier
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