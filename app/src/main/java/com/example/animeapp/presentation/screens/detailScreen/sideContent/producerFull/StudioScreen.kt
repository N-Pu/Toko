package com.example.animeapp.presentation.screens.detailScreen.sideContent.producerFull

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.domain.viewModel.ProducerFullViewModel
import com.example.animeapp.presentation.animations.LoadingAnimation
import com.example.animeapp.presentation.screens.detailScreen.sideContent.bottomSheetActivatorButton.BottomSheetButton
import com.example.animeapp.presentation.theme.LightGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ShowScreen(
    id: Int,
    viewModelProvider: ViewModelProvider,
    studio_name: String,
    modifier: Modifier
) {

    val coroutine = rememberCoroutineScope()
    val rememberSheetState = rememberBottomSheetScaffoldState()
//    val scaffoldState = rememberBottomSheetScaffoldState()
    val viewModel = viewModelProvider[ProducerFullViewModel::class.java]
    LaunchedEffect(id) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.getProducerFromId(id)
        }


    }


    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val producerState by
    viewModel.producerFull.collectAsStateWithLifecycle()
    val painter =
        rememberAsyncImagePainter(model = producerState?.images?.jpg?.image_url)
    if (isSearching.not()) {
        BottomSheetScaffold(
            sheetContainerColor = LightGreen,
            sheetPeekHeight = 0.dp,
            scaffoldState = rememberSheetState,
            sheetContent = {
                Text(
                    text = "${producerState?.url} roles",
                    textAlign = TextAlign.Center,
                    modifier = modifier.fillMaxWidth(),
                    fontSize = 36.sp
                )
                LazyVerticalStaggeredGrid(
                    modifier = modifier.fillMaxSize(),
                    columns = StaggeredGridCells.Adaptive(120.dp),
                    contentPadding = PaddingValues(0.dp),

                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 8.dp
                ) {
                    producerState?.let {
                        itemsIndexed(it.titles) { _, anime ->
                            Text(text = anime.title + " " + anime.type)


                        }
                    }
                }
            }) {


            LazyColumn(modifier = modifier.fillMaxSize()) {
                item {
                    Card(
                        modifier = modifier
                            .fillMaxSize()
                    ) {


                        Image(
                            painter = painter,
                            contentDescription = "producer : ${producerState?.images?.jpg?.image_url}",
                            modifier = Modifier
                                .size(400.dp)
                                .aspectRatio(9 / 11f)
                        )
                    }


                    Spacer(modifier = modifier.size(28.dp))

                }

                item {
                    producerState?.let {
                        Text(
                            text = studio_name,
                            textAlign = TextAlign.Center,
                            fontSize = 40.sp,
                            modifier = modifier.fillMaxWidth(),
                            maxLines = 2
                        )
                    }
                }
                item {
                    BottomSheetButton(modifier, coroutine, rememberSheetState)
                }
                item{
                    // writes null in ui if there's no "about" data
                    producerState?.about?.let { about ->
                        Text(
                            text = about,
                            textAlign = TextAlign.Center,
                            modifier = modifier.fillMaxWidth()
                        )
                    }
                }


                item {
                    producerState?.titles?.forEach {

                        Text(text = it.title)
                    }
                }
            }
        }
    } else
        LoadingAnimation()

}

