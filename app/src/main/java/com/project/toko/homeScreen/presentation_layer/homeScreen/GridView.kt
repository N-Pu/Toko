package com.project.toko.homeScreen.presentation_layer.homeScreen

import android.media.effect.Effect
import com.project.toko.homeScreen.viewModel.HomeScreenViewModel
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.project.toko.R
import com.project.toko.core.presentation_layer.addToFavorite.AddFavorites
import com.project.toko.core.presentation_layer.animations.LoadingAnimation
import com.project.toko.core.presentation_layer.theme.DarkSectionColor
import com.project.toko.core.presentation_layer.theme.SectionColor
import com.project.toko.core.presentation_layer.theme.evolventaBoldFamily
import com.project.toko.core.presentation_layer.theme.scoreBoardColor
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.homeScreen.model.newAnimeSearchModel.AnimeSearchData
import com.project.toko.homeScreen.model.newAnimeSearchModel.NewAnimeSearchModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@Stable
@Composable
fun GridAdder(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    switch: () -> Boolean,
    isInDarkTheme: () -> Boolean,
    svgImageLoader: ImageLoader,
) {
    val viewModel = viewModelProvider[HomeScreenViewModel::class.java]
    val newAnimeSearchModel by viewModel.animeSearch.collectAsStateWithLifecycle()
    val getTrendingAnime by viewModel.topTrendingAnime.collectAsStateWithLifecycle()
    val getTopUpcoming by viewModel.topUpcomingAnime.collectAsStateWithLifecycle()
    val getTopAiring by viewModel.topAiringAnime.collectAsStateWithLifecycle()

    if (switch()) {
        if (viewModel.isLoadingSearch.value.not()) {
            SearchScreen(
                viewModel, newAnimeSearchModel, navController, viewModelProvider, svgImageLoader
            )
        } else {
            LoadingAnimation()
        }
    } else {
        ShowMainScreen(
            viewModelProvider = viewModelProvider,
            isInDarkTheme = isInDarkTheme,
            navController = navController,
            svgImageLoader = svgImageLoader,
            getTopAiring = getTopAiring,
            getTopUpcoming = getTopUpcoming,
            getTrendingAnime = getTrendingAnime
        )
    }


    if (viewModel.isDialogShown) {
        val selectedAnime =
            newAnimeSearchModel.data.find { it.mal_id == viewModel.selectedAnimeId.value }
        val selectedTrending =
            getTrendingAnime.data.find { it.mal_id == viewModel.selectedAnimeId.value }
        val selectedAiring = getTopAiring.data.find { it.mal_id == viewModel.selectedAnimeId.value }
        val selectedUpcoming =
            getTopUpcoming.data.find { it.mal_id == viewModel.selectedAnimeId.value }

        val selectedData = selectedAnime ?: selectedTrending ?: selectedAiring ?: selectedUpcoming

        selectedData?.let { data ->
            CustomDialog(
                data = data,
                navController = navController,
                onDismiss = {
                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                        viewModel.onDialogDismiss()
                    }
                },
                modifier = modifier,
                viewModelProvider = viewModelProvider,
                isInDarkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader
            )
        }
    }
}

@Stable
@Composable
fun SearchScreen(
    viewModel: HomeScreenViewModel,
    newAnimeSearchModel: NewAnimeSearchModel,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    svgImageLoader: ImageLoader
) {
    var additionalDataRequested by remember { mutableStateOf(false) }
    val columnState = rememberLazyListState()

    LaunchedEffect(key1 = !columnState.canScrollForward && newAnimeSearchModel.pagination.has_next_page) {
        withContext(Dispatchers.IO) {
            additionalDataRequested = true
            delay(300) // Измените задержку по вашему усмотрению
            viewModel.loadNextPage()
            additionalDataRequested = false
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = columnState
    ) {
        items(newAnimeSearchModel.data.chunked(2)) { rowData ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowData.forEach { data ->
                    val cardModifier = Modifier
                        .weight(1f)
                        .padding(vertical = 10.dp) // Add your desired padding here
                    AnimeCardBox(
                        data = data,
                        navController = navController,
                        viewModelProvider = viewModelProvider,
                        modifier = cardModifier,
                        svgImageLoader = svgImageLoader,
                        homeScreenViewModel = viewModel
                    )
                }
            }
        }
    }

}


@Composable
fun ShowMainScreen(
    modifier: Modifier = Modifier,
    isInDarkTheme: () -> Boolean,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    svgImageLoader: ImageLoader,
    getTrendingAnime: NewAnimeSearchModel,
    getTopUpcoming: NewAnimeSearchModel,
    getTopAiring: NewAnimeSearchModel
) {

    val scroll = rememberScrollState()
    val viewModel = viewModelProvider[HomeScreenViewModel::class.java]
    val loadingSectionTopAiring by viewModel.loadingSectionTopAiring
    val loadingSectionTopUpcoming by viewModel.loadingSectionTopUpcoming
    val loadingSectionTopTrending by viewModel.loadingSectionTopTrending
    val lastTenAnimeFromWatchingSection by
    viewModel.showListOfWatching().collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val getJustTenAddedAnime by
    viewModel.showLastAdded().collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    Column(
        modifier = modifier
            .verticalScroll(scroll)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        if (lastTenAnimeFromWatchingSection.isNotEmpty()) {

            ShowSectionName(
                sectionName = "Now Watching ",
                modifier = modifier,
                isInDarkTheme = isInDarkTheme
            )
            LazyRow(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                items(lastTenAnimeFromWatchingSection) { data ->
                    Spacer(modifier = modifier.width(20.dp))
                    ShowSection(
                        data = data, navController = navController,
                        viewModelProvider = viewModelProvider,
                        modifier = modifier,
                        svgImageLoader = svgImageLoader
                    )
                }
                item {
                    Spacer(modifier = modifier.width(20.dp))
                }
            }
            Spacer(modifier = modifier.height(20.dp))

        }
        if (loadingSectionTopTrending.not()) {

            ShowSectionName(
                sectionName = "Trending",
                modifier = modifier,
                isInDarkTheme = isInDarkTheme
            )


            LazyRow(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                items(getTrendingAnime.data) { data ->
                    Spacer(modifier = modifier.width(20.dp))
                    ShowTopAnime(
                        data = data, navController = navController,
                        viewModelProvider = viewModelProvider,
                        modifier = modifier,
                        svgImageLoader = svgImageLoader
                    )
                }
                item {
                    Spacer(modifier = modifier.width(20.dp))
                }
            }
            Spacer(modifier = modifier.height(20.dp))

        } else {
            ShowSectionName(
                sectionName = "Trending",
                modifier = modifier,
                isInDarkTheme = isInDarkTheme
            )
            Row(
                modifier = modifier
                    .height(300.dp)
                    .background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                LoadingAnimation()
            }
        }
        if (getJustTenAddedAnime.isNotEmpty()) {

            ShowSectionName(
                sectionName = "Just Added",
                modifier = modifier,
                isInDarkTheme = isInDarkTheme
            )


            LazyRow(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                items(getJustTenAddedAnime) { data ->
                    Spacer(modifier = modifier.width(20.dp))
                    ShowSection(
                        data = data, navController = navController,
                        viewModelProvider = viewModelProvider,
                        modifier = modifier,
                        svgImageLoader = svgImageLoader
                    )
                }
                item {
                    Spacer(modifier = modifier.width(20.dp))
                }
            }
            Spacer(modifier = modifier.height(20.dp))

        }
        if (loadingSectionTopAiring.not()) {

            ShowSectionName(
                sectionName = "Top Airing",
                modifier = modifier,
                isInDarkTheme = isInDarkTheme
            )


            LazyRow(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                items(getTopAiring.data) { data ->
                    Spacer(modifier = modifier.width(20.dp))
                    ShowTopAnime(
                        data = data, navController = navController,
                        viewModelProvider = viewModelProvider,
                        modifier = modifier,
                        svgImageLoader = svgImageLoader
                    )
                }
                item {
                    Spacer(modifier = modifier.width(20.dp))
                }

            }



            Spacer(modifier = modifier.height(20.dp))

        } else {
            ShowSectionName(
                sectionName = "Top Airing",
                modifier = modifier,
                isInDarkTheme = isInDarkTheme
            )
            Row(
                modifier = modifier
                    .height(300.dp)
                    .background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                LoadingAnimation()
            }

        }
        if (loadingSectionTopUpcoming.not()) {

            ShowSectionName(
                sectionName = "Top Upcoming",
                modifier = modifier,
                isInDarkTheme = isInDarkTheme
            )


            LazyRow(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                items(getTopUpcoming.data) { data ->
                    Spacer(modifier = modifier.width(20.dp))
                    ShowTopAnime(
                        data = data, navController = navController,
                        viewModelProvider = viewModelProvider,
                        modifier = modifier,
                        svgImageLoader = svgImageLoader
                    )
                }
                item {
                    Spacer(modifier = modifier.width(20.dp))
                }
            }



            Spacer(modifier = modifier.height(20.dp))

        } else {
            ShowSectionName(
                sectionName = "Top Upcoming",
                modifier = modifier,
                isInDarkTheme = isInDarkTheme
            )
            Row(
                modifier = modifier
                    .height(300.dp)
                    .background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                LoadingAnimation()
            }

        }
    }
}

@Stable
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AnimeCardBox(
    data: AnimeSearchData,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    svgImageLoader: ImageLoader,
    homeScreenViewModel: HomeScreenViewModel
) {
    val painter = rememberAsyncImagePainter(model = data.images.webp.image_url)
    var isCardClicked by remember { mutableStateOf(false) }
    val value by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = if (isCardClicked) 0.99f else 1f, // Изменяем значение в зависимости от нажатия на Card
        targetValue = if (isCardClicked) 1f else 0.99f, // Изменяем значение в зависимости от нажатия на Card
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600, easing = LinearEasing
            ), repeatMode = RepeatMode.Reverse
        ), label = ""
    )


    Card(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .shadow(20.dp)
            .then(if (isCardClicked) {
                Modifier.graphicsLayer {
                    scaleX = value
                    scaleY = value
                }
            } else {
                Modifier
            })
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(onLongClick = {
                homeScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
                    isCardClicked = true
                    homeScreenViewModel.onDialogLongClick(data.mal_id)
                    delay(3000L)
                    isCardClicked = false
                }

            }) {
                navigateToDetailScreen {
                        navController.navigate(route = "detail_screen/${data.mal_id}")
                        {
                            launchSingleTop = true
                        }
                    }

            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onTertiaryContainer),
        shape = RectangleShape,
    ) {
        Box {
            // Coil image loader
            Image(
                painter = painter,
                contentDescription = "Images for each Anime",
                modifier = Modifier
                    .aspectRatio(9f / 11f)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillBounds
            )

            Column(
                modifier = Modifier
                    .width(50.dp)
                    .clip(RoundedCornerShape(bottomEnd = 15.dp))
                    .background(scoreBoardColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = formatScore(data.score),
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Image(
                        modifier = Modifier.size(25.dp), painter = rememberAsyncImagePainter(
                            model = R.drawable.usergroup, imageLoader = svgImageLoader
                        ), contentDescription = null
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = formatScoredBy(data.scored_by),
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                }
            }

            AddFavorites(
                mal_id = data.mal_id,
                title = data.title,
                score = formatScore(data.score),
                scoredBy = formatScoredBy(data.scored_by),
                animeImage = data.images.jpg.image_url,
                modifier = Modifier,
                viewModelProvider = viewModelProvider,
                status = data.status,
                rating = data.rating ?: "N/A",
                secondName = data.title_japanese,
                airedFrom = data.aired.from,
                type = data.type ?: "N/A",
                imageLoader = svgImageLoader
            )


        }
        Row(modifier = Modifier.height(50.dp)) {
            Text(
                text = data.title,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp, top = 5.dp, bottom = 5.dp, start = 10.dp),
                lineHeight = 16.sp,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                minLines = 2,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = evolventaBoldFamily,
                fontWeight = FontWeight.W900
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Status: " + data.status,
                fontSize = 10.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(start = 10.dp),
                color = MaterialTheme.colorScheme.inversePrimary
            )
            Text(
                text = "Type: " + data.type,
                fontSize = 10.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }
//        Spacer(
//            modifier = Modifier
//                .height(20.dp)
//                .fillMaxWidth()
//                .background(Color.Yellow)
//        )

//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 10.dp),
//            horizontalArrangement = Arrangement.Start
//        ) {
//            Text(
//                text = "Type: " + data.type,
//                fontSize = 10.sp,
//                textAlign = TextAlign.Left,
//                modifier = Modifier.padding(start = 10.dp),
//                color = MaterialTheme.colorScheme.inversePrimary
//            )
//        }
    }
}

private fun formatScoredBy(float: Float): String {
    return if (float == 0.0f) {
        "N/A"
    } else {
        val formattedString = String.format(Locale.US, "%.1f", float)
        if (formattedString.endsWith(".0")) {
            formattedString.substring(0, formattedString.length - 2)
        } else {
            formattedString.replace(",", ".")
        }
    }
}


private fun formatScore(float: Float?): String {
    return if (float == null || float == 0.0f) {
        "N/A"
    } else {
        float.toString()
    }
}

@Stable
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShowSection(
    data: AnimeItem,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    svgImageLoader: ImageLoader
) {
    val painter = rememberAsyncImagePainter(model = data.animeImage)
    var isCardClicked by remember { mutableStateOf(false) }

    val homeScreenViewModel = viewModelProvider[HomeScreenViewModel::class.java]
    val value by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = if (isCardClicked) 0.99f else 1f, // Изменяем значение в зависимости от нажатия на Card
        targetValue = if (isCardClicked) 1f else 0.99f, // Изменяем значение в зависимости от нажатия на Card
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600, easing = LinearEasing
            ), repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Card(
        modifier = modifier
            .height(300.dp)
            .width(180.dp)
            .shadow(20.dp)
            .then(if (isCardClicked) {
                modifier.graphicsLayer {
                    scaleX = value
                    scaleY = value
                }
            } else {
                modifier
            })
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(onLongClick = {
                homeScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
                    isCardClicked = true
                    delay(3000L)
                    isCardClicked = false
                }

            }) {
                    navigateToDetailScreen {
                        navController.navigate(route = "detail_screen/${data.id}")
                        {
                            launchSingleTop = true
                        }
                    }


            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RectangleShape,
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier.background(MaterialTheme.colorScheme.primary)
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                // Coil image loader
                Image(
                    painter = painter,
                    contentDescription = "Images for each Anime",
                    modifier = modifier
                        .aspectRatio(9f / 11f)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.FillBounds
                )

                Column(
                    modifier = modifier
                        .width(50.dp)
                        .clip(RoundedCornerShape(bottomEnd = 15.dp))
                        .background(scoreBoardColor)
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = data.score,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Image(
                            modifier = modifier.size(25.dp), painter = rememberAsyncImagePainter(
                                model = R.drawable.usergroup, imageLoader = svgImageLoader
                            ), contentDescription = null
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = data.scored_by,
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = modifier
                        )
                    }
                }

            }
            Column(
                modifier = modifier
            ) {
                Text(
                    text = data.animeName,
                    textAlign = TextAlign.Start,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(end = 5.dp, top = 5.dp, bottom = 5.dp, start = 10.dp),
                    lineHeight = 16.sp,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = evolventaBoldFamily,
                    fontWeight = FontWeight.W900
                )

                Row(
                    modifier = modifier.fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Status: " + data.status,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Left,
                        modifier = modifier.padding(start = 10.dp),
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                }
                Row(
                    modifier = modifier
                        .fillMaxWidth(1f)
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Type: " + data.type,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Left,
                        modifier = modifier.padding(start = 10.dp),
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                }

            }
        }
    }

}

@Stable
@Composable
private fun ShowSectionName(sectionName: String, modifier: Modifier, isInDarkTheme: () -> Boolean) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.85f)
            .background(if (isInDarkTheme()) DarkSectionColor else SectionColor)
            .padding(bottom = 5.dp)
    ) {
        Box(
            modifier = modifier
        ) {
            Text(
                text = "    $sectionName   ",
                fontSize = 24.sp,
                textAlign = TextAlign.Start,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = evolventaBoldFamily,
                fontWeight = FontWeight.W900
            )
        }
    }
    Spacer(modifier = modifier.height(20.dp))
}

@Stable
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShowTopAnime(
    data: AnimeSearchData,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    svgImageLoader: ImageLoader
) {
    val painter = rememberAsyncImagePainter(model = data.images.webp.image_url)
    var isCardClicked by remember { mutableStateOf(false) }

    val homeScreenViewModel = viewModelProvider[HomeScreenViewModel::class.java]
    val value by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = if (isCardClicked) 0.99f else 1f, // Изменяем значение в зависимости от нажатия на Card
        targetValue = if (isCardClicked) 1f else 0.99f, // Изменяем значение в зависимости от нажатия на Card
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600, easing = LinearEasing
            ), repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Card(
        modifier = modifier
            .height(300.dp)
            .width(180.dp)
            .shadow(20.dp)
            .then(if (isCardClicked) {
                modifier.graphicsLayer {
                    scaleX = value
                    scaleY = value
                }
            } else {
                modifier
            })
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(onLongClick = {
                homeScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
                    isCardClicked = true
                    homeScreenViewModel.onDialogLongClick(data.mal_id ?: 0)
                    delay(3000L)
                    isCardClicked = false
                }

            }) {
                navigateToDetailScreen {
                    navController.navigate(route = "detail_screen/${data.mal_id}")
                    {
                        launchSingleTop = true
                    }
                }
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RectangleShape,
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier.background(MaterialTheme.colorScheme.primary)
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                // Coil image loader
                Image(
                    painter = painter,
                    contentDescription = "Images for each Anime",
                    modifier = modifier
                        .aspectRatio(9f / 11f)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.FillBounds
                )

                Column(
                    modifier = modifier
                        .width(50.dp)
                        .clip(RoundedCornerShape(bottomEnd = 15.dp))
                        .background(scoreBoardColor)
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = formatScore(data.score),
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Image(
                            modifier = modifier.size(25.dp), painter = rememberAsyncImagePainter(
                                model = R.drawable.usergroup, imageLoader = svgImageLoader
                            ), contentDescription = null
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = formatScoredBy(data.scored_by),
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = modifier
                        )
                    }
                }

                AddFavorites(
                    mal_id = data.mal_id,
                    title = data.title,
                    score = formatScore(data.score),
                    scoredBy = formatScoredBy(data.scored_by),
                    animeImage = data.images.jpg.image_url,
                    modifier = modifier,
                    viewModelProvider = viewModelProvider,
                    status = data.status,
                    rating = data.rating ?: "N/A",
                    secondName = data.title_japanese,
                    airedFrom = data.aired.from,
                    type = data.type ?: "N/A",
                    imageLoader = svgImageLoader
                )
            }
            Column(
                modifier = modifier
            ) {
                Text(
                    text = data.title,
                    textAlign = TextAlign.Start,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(end = 5.dp, top = 5.dp, bottom = 5.dp, start = 10.dp),
                    lineHeight = 16.sp,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = evolventaBoldFamily,
                    fontWeight = FontWeight.W900
                )

                Row(
                    modifier = modifier.fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Status: " + data.status,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Left,
                        modifier = modifier.padding(start = 10.dp),
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                }
                Row(
                    modifier = modifier
                        .fillMaxWidth(1f)
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Type: " + data.type,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Left,
                        modifier = modifier.padding(start = 10.dp),
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                }

            }
        }
    }
}