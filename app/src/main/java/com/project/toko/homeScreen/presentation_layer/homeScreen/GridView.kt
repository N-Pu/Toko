package com.project.toko.homeScreen.presentation_layer.homeScreen

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import com.project.toko.homeScreen.viewModel.HomeScreenViewModel
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalContext
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
import coil.decode.SvgDecoder
import com.project.toko.R
import com.project.toko.core.connectionCheck.isInternetAvailable
import com.project.toko.core.presentation_layer.addToFavorite.AddFavorites
import com.project.toko.core.presentation_layer.theme.DarkSectionColor
import com.project.toko.core.presentation_layer.theme.SectionColor
import com.project.toko.core.presentation_layer.theme.evolventaBoldFamily
import com.project.toko.core.presentation_layer.theme.scoreBoardColor
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.homeScreen.model.newAnimeSearchModel.AnimeSearchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun GridAdder(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    isTabMenuOpen: MutableState<Boolean>,
    switch: MutableState<Boolean>,
    isInDarkTheme: Boolean
) {

    val viewModel = viewModelProvider[HomeScreenViewModel::class.java]
    val listData by viewModel.animeSearch.collectAsStateWithLifecycle()
    val scrollGridState = rememberLazyStaggeredGridState()
    val isLoading by viewModel.isNextPageLoading.collectAsStateWithLifecycle()
    val lastTenAnimeFromWatchingSection =
        viewModel.showListOfWatching().collectAsStateWithLifecycle(
            initialValue = emptyList()
        )
    val getJustTenAddedAnime =
        viewModel.showLastAdded().collectAsStateWithLifecycle(
            initialValue = emptyList()
        )

    val context = LocalContext.current


    LaunchedEffect(key1 = Unit) {
        if (isInternetAvailable(context)) {
            viewModel.getTopTrendingAnime("bypopularity", 25)
            delay(2000L)
            viewModel.getTopAiring("airing", 25)
            delay(2000L)
            viewModel.getTopUpcoming("upcoming", 25)
        } else {
            Toast.makeText(
                context,
                "No internet connection!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val getTrendingAnime by viewModel.topTrendingAnime.collectAsStateWithLifecycle()
    val getTopUpcoming by viewModel.topUpcomingAnime.collectAsStateWithLifecycle()
    val getTopAiring by viewModel.topAiringAnime.collectAsStateWithLifecycle()

    var log by remember { mutableStateOf("") }
//    if (listData.data.isNotEmpty()) {
    if (switch.value) {
        LazyVerticalStaggeredGrid(
            state = if (scrollGridState.firstVisibleItemIndex >= 4) {
                log = scrollGridState.firstVisibleItemIndex.toString()
                isTabMenuOpen.value = false
                scrollGridState
            } else {
                log = scrollGridState.firstVisibleItemIndex.toString()
                isTabMenuOpen.value = true
                scrollGridState
            },
            columns = StaggeredGridCells.Adaptive(minSize = 140.dp),
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessVeryLow)),
            horizontalArrangement = Arrangement.spacedBy(22.dp),
            verticalItemSpacing = 20.dp,
            contentPadding = PaddingValues(10.dp)
        ) {
            item {
                Spacer(modifier = modifier.height(1.dp))

            }
            item {
                Spacer(modifier = modifier.height(20.dp))
            }
            itemsIndexed(listData.data) { index, data ->
                AnimeCardBox(
                    data = data,
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    modifier = modifier
                )

                // Загрузка следующей страницы при достижении конца списка и has_next_page = true
                if (index == listData.data.lastIndex - 2 && isLoading.not() && listData.pagination.has_next_page) {
                    viewModel.loadNextPage()
                }
            }
        }

    } else {

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            if (lastTenAnimeFromWatchingSection.value.isNotEmpty()) {
                item {
                    ShowSectionName(
                        sectionName = "Now Watching ",
                        modifier = modifier,
                        isInDarkTheme = isInDarkTheme
                    )
                }
                item {
                    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
                        lastTenAnimeFromWatchingSection.value.forEach { data ->
                            Spacer(modifier = modifier.width(20.dp))
                            ShowSection(
                                data = data, navController = navController,
                                viewModelProvider = viewModelProvider,
                                modifier = modifier
                            )
                        }
                        Spacer(modifier = modifier.width(20.dp))
                    }

                }
                item {
                    Spacer(modifier = modifier.height(20.dp))
                }
            }
            if (getTrendingAnime.data.isNotEmpty()) {
                item {
                    ShowSectionName(
                        sectionName = "Trending",
                        modifier = modifier,
                        isInDarkTheme = isInDarkTheme
                    )
                }
                item {
                    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {

                        getTrendingAnime.data.forEach { data ->
                            Spacer(modifier = modifier.width(20.dp))
                            ShowTopAnime(
                                data = data, navController = navController,
                                viewModelProvider = viewModelProvider,
                                modifier = modifier
                            )
                        }
                        Spacer(modifier = modifier.width(20.dp))
                    }

                }
                item {
                    Spacer(modifier = modifier.height(20.dp))
                }
            }
            if (getJustTenAddedAnime.value.isNotEmpty()) {
                item {
                    ShowSectionName(
                        sectionName = "Just Added",
                        modifier = modifier,
                        isInDarkTheme = isInDarkTheme
                    )
                }
                item {
                    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {

                        getJustTenAddedAnime.value.forEach { data ->
                            Spacer(modifier = modifier.width(20.dp))
                            ShowSection(
                                data = data, navController = navController,
                                viewModelProvider = viewModelProvider,
                                modifier = modifier
                            )
                        }
                        Spacer(modifier = modifier.width(20.dp))
                    }

                }
                item {
                    Spacer(modifier = modifier.height(20.dp))
                }
            }
            if (getTopAiring.data.isNotEmpty()) {
                item {
                    ShowSectionName(
                        sectionName = "Top Airing",
                        modifier = modifier,
                        isInDarkTheme = isInDarkTheme
                    )
                }
                item {
                    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {

                        getTopAiring.data.forEach { data ->
                            Spacer(modifier = modifier.width(20.dp))
                            ShowTopAnime(
                                data = data, navController = navController,
                                viewModelProvider = viewModelProvider,
                                modifier = modifier
                            )
                        }
                        Spacer(modifier = modifier.width(20.dp))
                    }

                }
                item {
                    Spacer(modifier = modifier.height(20.dp))
                }
            }
            if (getTopUpcoming.data.isNotEmpty()) {
                item {
                    ShowSectionName(
                        sectionName = "Top Upcoming",
                        modifier = modifier,
                        isInDarkTheme = isInDarkTheme
                    )
                }
                item {
                    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {

                        getTopUpcoming.data.forEach { data ->
                            Spacer(modifier = modifier.width(20.dp))
                            ShowTopAnime(
                                data = data, navController = navController,
                                viewModelProvider = viewModelProvider,
                                modifier = modifier
                            )
                        }
                        Spacer(modifier = modifier.width(20.dp))
                    }

                }
                item {
                    Spacer(modifier = modifier.height(20.dp))
                }
            }
        }

    }


    if (viewModel.isDialogShown) {

        val selectedAnime = listData.data.find { it.mal_id == viewModel.selectedAnimeId.value }

        if (selectedAnime != null) {
            CustomDialog(
                data = selectedAnime,
                navController = navController,
                onDismiss = {
                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                        viewModel.onDialogDismiss()
                    }

                },
                modifier = modifier,
                viewModelProvider = viewModelProvider,
                isInDarkTheme = isInDarkTheme
            )
        }

        val selectedTrending =
            getTrendingAnime.data.find { it.mal_id == viewModel.selectedAnimeId.value }

        if (selectedTrending != null) {
            CustomDialog(
                data = selectedTrending,
                navController = navController,
                onDismiss = {
                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                        viewModel.onDialogDismiss()
                    }

                },
                modifier = modifier,
                viewModelProvider = viewModelProvider,
                isInDarkTheme = isInDarkTheme
            )
        }
        val selectedAiring = getTopAiring.data.find { it.mal_id == viewModel.selectedAnimeId.value }

        if (selectedAiring != null) {
            CustomDialog(
                data = selectedAiring,
                navController = navController,
                onDismiss = {
                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                        viewModel.onDialogDismiss()
                    }

                },
                modifier = modifier,
                viewModelProvider = viewModelProvider,
                isInDarkTheme = isInDarkTheme
            )
        }
        val selectedUpcoming =
            getTopUpcoming.data.find { it.mal_id == viewModel.selectedAnimeId.value }

        if (selectedUpcoming != null) {
            CustomDialog(
                data = selectedUpcoming,
                navController = navController,
                onDismiss = {
                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                        viewModel.onDialogDismiss()
                    }

                },
                modifier = modifier,
                viewModelProvider = viewModelProvider,
                isInDarkTheme = isInDarkTheme
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AnimeCardBox(
    data: AnimeSearchData,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier
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
    val svgImageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()

    Card(
        modifier = modifier
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
                    homeScreenViewModel.onDialogLongClick(data.mal_id)
                    delay(3000L)
                    isCardClicked = false
                }

            }) {
                homeScreenViewModel.viewModelScope.launch(Dispatchers.Main) {
                    navigateToDetailScreen(
                        navController, data.mal_id
                    )
                }
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onTertiaryContainer),
        shape = RectangleShape,
    ) {
        Box(
            modifier = modifier.fillMaxSize()
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

        Row(modifier = modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Start) {
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShowSection(
    data: AnimeItem,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier
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
    val svgImageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()

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
                homeScreenViewModel.viewModelScope.launch(Dispatchers.Main) {
                    navigateToDetailScreen(
                        navController, data.id ?: 0
                    )
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

@Composable
private fun ShowSectionName(sectionName: String, modifier: Modifier, isInDarkTheme: Boolean) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.85f)
            .background(if (isInDarkTheme) DarkSectionColor else SectionColor)
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShowTopAnime(
    data: AnimeSearchData,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier
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
    val svgImageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()


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
                homeScreenViewModel.viewModelScope.launch(Dispatchers.Main) {
                    navigateToDetailScreen(
                        navController, data.mal_id ?: 0
                    )
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