package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.project.toko.core.connectionCheck.isInternetAvailable
import com.project.toko.core.presentation_layer.animations.LoadingAnimation
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.AddToFavorites
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.DisplayJapAndEnglishTitles
import com.project.toko.detailScreen.presentation_layer.detailScreen.sideContent.castList.DisplayCast
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.customVisuals.DisplayCustomGenreBoxes
import com.project.toko.detailScreen.presentation_layer.detailScreen.sideContent.staffList.DisplayStaff
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.DisplayPicture
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.DisplayTitle
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.ExpandableRelated
import com.project.toko.core.presentation_layer.expandableText.ExpandableText
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.FavoritesLine
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.MembersLine
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.PopularityLine
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.RankedLine
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.Recommendations
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.ScoreByNumber
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.ScoreLabel
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.ScoreNumber
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.ShowBackground
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.ShowMoreInformation
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.ShowPictureAlbum
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.YearTypeEpisodesTimeStatusStudio
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActivateDetailScreen(
    viewModelProvider: ViewModelProvider,
    navController: NavController,
    id: Int,
    modifier: Modifier
) {

    val viewModel = viewModelProvider[DetailScreenViewModel::class.java]
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()

    val detailData by
    viewModel.animeDetails.collectAsStateWithLifecycle()

    val castData by viewModel.castList.collectAsStateWithLifecycle()
    val staffData by viewModel.staffList.collectAsStateWithLifecycle()
    val recommendationsData by viewModel.recommendationList.collectAsStateWithLifecycle()
    val scrollState = viewModel.scrollState
    val picturesData by viewModel.picturesData.collectAsStateWithLifecycle()
    val isDialogShown = remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = id) {

        val previousId = viewModel.previousId.value
        if (id != previousId) {
            scrollState.scrollTo(0)
            viewModel.previousId.value = id
        }
        if (isInternetAvailable(context)) {
            viewModel.onTapAnime(id)
            delay(300)
            viewModel.addStaffFromId(id)
            delay(300)
            viewModel.addCastFromId(id)
            delay(1000L)
            viewModel.addRecommendationsFromId(id)
            delay(1000L)
            viewModel.showPictures(id)
        } else {
            Toast.makeText(
                context,
                "No internet connection!",
                Toast.LENGTH_SHORT
            ).show()
        }

    }


    val model = ImageRequest.Builder(LocalContext.current)
        .data(detailData?.images?.jpg?.large_image_url)
        .size(Size.ORIGINAL)
        .crossfade(true)
        .build()

    val painter =
        rememberAsyncImagePainter(
            model
        )

    if (isSearching.not() && detailData != null) {

        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.errorContainer),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            DisplayPicture(
                painter = painter,
                modifier = modifier
                    .fillMaxSize()
                    .combinedClickable(onClick = {}, onLongClick = {
                        isDialogShown.value = true
                    })
            )

            ShowPictureAlbum(
                isDialogShown = isDialogShown,
                picturesData = picturesData,
                modifier = modifier
            )

            DisplayTitle(title = detailData?.title ?: "No title name", modifier)
            DisplayJapAndEnglishTitles(detailData = detailData, modifier = modifier)
            YearTypeEpisodesTimeStatusStudio(data = detailData, modifier = modifier)

            Row(
                modifier = modifier
                    .fillMaxWidth(1f)
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier.size(150.dp)
                ) {

                    ScoreLabel(modifier = modifier)
                    ScoreNumber(modifier = modifier, score = detailData?.score ?: 0.0f)
                    ScoreByNumber(scoreBy = detailData?.scored_by ?: 0.0f, modifier = modifier)

                }
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier.size(height = 150.dp, width = 150.dp)
                ) {

                    RankedLine(
                        rank = detailData?.rank ?: 0,
                        modifier = modifier
                    )
                    PopularityLine(
                        popularity = detailData?.popularity ?: 0,
                        modifier = modifier
                    )
                    MembersLine(
                        members = detailData?.members ?: 0,
                        modifier = modifier
                    )

                    FavoritesLine(
                        favorites = detailData?.favorites ?: 0,
                        modifier = modifier
                    )
                }
            }
            DisplayCustomGenreBoxes(
                genres = detailData?.genres ?: listOf(),
                modifier = modifier
            )
            AddToFavorites(viewModelProvider, modifier)
            ExpandableText(text = detailData!!.synopsis, title = "Synopsis", modifier = modifier)
//            FullScreenYoutubeActivity().YoutubePlayerSecond(
//                detailData?.trailer?.youtube_id ?: "",
//                LocalLifecycleOwner.current,
//                modifier
//            )

//            YoutubePlayer(
//                detailData?.trailer?.youtube_id ?: "",
//                LocalLifecycleOwner.current,
//                modifier
//            )

            ShowMoreInformation(modifier = modifier, detailData = detailData)
            ShowBackground(detailData = detailData, modifier = modifier)
            DisplayCast(
                castList = castData, navController = navController,
                modifier = modifier, detailMalId = viewModel.loadedId.intValue
            )
            DisplayStaff(
                staffList = staffData,
                navController = navController,
                modifier = modifier
            )
//            ShowStudios(detailData, navController)
            ExpandableRelated(
                relations = detailData?.relations,
                modifier = modifier,
                navController = navController,
                viewModel = viewModel
            )
            Recommendations(recommendationsData, navController, viewModelProvider, modifier)
            Spacer(modifier = modifier.height(20.dp))

        }
    } else {
        LoadingAnimation()
    }


}
