package com.project.toko.homeScreen.ui.homeScreen

import android.util.Log
import com.project.toko.homeScreen.ui.viewModel.HomePageViewModel
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.project.toko.R
import com.project.toko.core.ui.addToFavorite.AddFavorites
import com.project.toko.core.ui.theme.DarkSectionColor
import com.project.toko.core.ui.theme.SectionColor
import com.project.toko.core.ui.theme.evolventaBoldFamily
import com.project.toko.core.ui.theme.scoreBoardColor
import com.project.toko.dataBase.search.data.db.entity.AnimeEntity
import com.project.toko.dataBase.search.ui.viewmodel.AnimeDialogState
import com.project.toko.dataBase.search.ui.viewmodel.CatalogSearchViewModel
import com.project.toko.dataBase.search.ui.viewmodel.InitialState
import com.project.toko.dataBase.search.ui.viewmodel.PaginationState
import com.project.toko.dataBase.search.ui.viewmodel.ScrollBehavior
import com.project.toko.savedScreen.data.dao.AnimeItem
import com.project.toko.homeScreen.data.model.newAnimeSearchModel.AnimeSearchData
import com.project.toko.homeScreen.data.model.newAnimeSearchModel.NewAnimeSearchModel
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun GridAdder(
    onNavigateToDetailScreen: (Int) -> Unit,
    switch: () -> Boolean,
    isInDarkTheme: () -> Boolean,
    svgImageLoader: () -> ImageLoader,
    catalogSearchViewModel: CatalogSearchViewModel,
    hideBottomBar: () -> Unit,
    showBottomBar: () -> Unit,
    getTrendingAnime: @Composable () -> NewAnimeSearchModel,
    getTopAiring: @Composable () -> NewAnimeSearchModel,
    getTopUpcoming: @Composable () -> NewAnimeSearchModel,
) {
    val lazyListState = rememberLazyListState()
    val scrollState = rememberScrollState()
    if (switch()) {
        val state by catalogSearchViewModel.animeEntityList.collectAsStateWithLifecycle()
        SearchScreen(
            onNavigateToDetailScreen,
            svgImageLoader,
            catalogSearchViewModel,
            state,
            lazyListState,
            hideBottomBar,
            showBottomBar
        )
    } else {
        ShowMainScreen(
            isInDarkTheme = isInDarkTheme,
            onNavigateToDetailScreen = onNavigateToDetailScreen,
            svgImageLoader = svgImageLoader,
            getTopAiring = getTopAiring,
            getTopUpcoming = getTopUpcoming,
            getTrendingAnime = getTrendingAnime,
            scrollState =  scrollState
        )
    }

    val dialog by catalogSearchViewModel.dialogState.collectAsStateWithLifecycle()
    when (dialog) {
        is AnimeDialogState.Error -> {
            val error = (dialog as AnimeDialogState.Error).exception.message
            ErrorDialog(text = error, onDismissDialog = { catalogSearchViewModel.dismissDialog() })
        }

        AnimeDialogState.Hidden -> Unit
        AnimeDialogState.Loading -> {
            Dialog(
                onDismissRequest = {
                    catalogSearchViewModel.dismissDialog()
                }, properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false
                )
            ) {
                CircularProgressIndicator()
            }
        }

        is AnimeDialogState.Shown -> {
            val animeEntity = (dialog as AnimeDialogState.Shown).anime
            CustomDialog(
                data = animeEntity,
                onNavigateToDetailScreen = onNavigateToDetailScreen,
                onDismiss = {
                    catalogSearchViewModel.dismissDialog()
                },
                isInDarkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader()
            )
        }
    }
}

@Composable
private fun ErrorDialog(modifier: Modifier = Modifier, onDismissDialog: () -> Unit, text: String?) {
    Dialog(
        onDismissRequest = {
            onDismissDialog()
        }, properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(modifier = modifier.size(200.dp)) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: $text!")
            }
        }

    }
}

@Composable
fun <T> AnimeHorizontalSection(
    items: List<T>,
    modifier: Modifier = Modifier,
    isInDarkTheme: () -> Boolean,
    sectionTitle: String,
    itemContent: @Composable (T) -> Unit
) {
    if (items.isNotEmpty()) {
        ShowSectionName(sectionTitle, modifier, isInDarkTheme)
        LazyRow(modifier = modifier.background(MaterialTheme.colorScheme.primary)) {
            items(items) { item ->
                Spacer(modifier = Modifier.width(20.dp))
                itemContent(item)
            }
            item { Spacer(modifier = Modifier.width(20.dp)) }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Composable
fun SearchScreen(
    onNavigateToDetailScreen: (Int) -> Unit,
    svgImageLoader: () -> ImageLoader,
    catalogSearchViewModel: CatalogSearchViewModel,
    state: InitialState,
    lazyListState: LazyListState,
    hideBottomBar: () -> Unit,
    showBottomBar: () -> Unit
) {
    val scrollBehavior by catalogSearchViewModel.scrollBehavior.collectAsStateWithLifecycle()
    val showLoader by catalogSearchViewModel.showLoader.collectAsStateWithLifecycle()

    LaunchedEffect(scrollBehavior) {
        when (scrollBehavior) {
            ScrollBehavior.SCROLL_TO_TOP -> lazyListState.scrollToItem(0)
            ScrollBehavior.KEEP_POSITION -> Unit
        }
        catalogSearchViewModel.resetScrollBehavior()
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemScrollOffset }
            .distinctUntilChanged()
            .pairWithPrevious()
            .collect { (prevOffset, currOffset) ->
                when {
                    currOffset > prevOffset -> {
                        // Скролл вниз
                        hideBottomBar()
                    }

                    currOffset < prevOffset -> {
                        // Скролл вверх
                        showBottomBar()
                    }
                }
            }
    }

    when (state) {
        InitialState.Empty -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Nothing was found")
            }
        }

        is InitialState.Error -> {
            val errorState = (state as InitialState.Error).error.message
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier
                        .sizeIn(
                            minHeight = 100.dp,
                            minWidth = 160.dp,
                            maxWidth = 180.dp,
                            maxHeight = 120.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    if (errorState != null) {
                        Text(text = errorState, overflow = TextOverflow.Ellipsis, maxLines = 3)
                    }
                    Button(colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary,
                        disabledContentColor = MaterialTheme.colorScheme.primary
                    ), onClick = {
                        catalogSearchViewModel.viewModelScope.launch {
                            catalogSearchViewModel.refresh()
                        }
                    }) {
                        Text(text = "Reload?")
                    }
                }

            }
        }

        InitialState.Initial -> Spacer(modifier = Modifier.fillMaxSize())
        InitialState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .shadow(20.dp)
                        .height(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.onTertiaryContainer)
                        .sizeIn(
                            minHeight = 60.dp,
                            minWidth = 60.dp,
                            maxWidth = 80.dp,
                            maxHeight = 80.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

            }
        }

        is InitialState.Success -> {
            val data = (state as InitialState.Success).data

            val shouldStartPaginate by remember {
                derivedStateOf {
                    lazyListState.reachedBottom()
                }
            }

            LaunchedEffect(shouldStartPaginate) {
                if (shouldStartPaginate && catalogSearchViewModel.hasNextPage()) {
                    Log.d("compose", "Paginating...")
                    catalogSearchViewModel.paginate()
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState
            ) {
                items(data.chunked(2)) { rowData ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        rowData.forEach { anime ->
                            AnimeCardBox(
                                data = anime,
                                onNavigateToDetailScreen = onNavigateToDetailScreen,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 10.dp),
                                svgImageLoader = svgImageLoader,
                                catalogSearchViewModel = catalogSearchViewModel
                            )

                        }
                        if (rowData.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .heightIn(min = 20.dp)
                    ) {
                        when (showLoader) {
                            PaginationState.Empty -> Unit
                            is PaginationState.Error -> {
                                val errorLoaderState =
                                    (showLoader as PaginationState.Error).error.message
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Column(
                                        modifier = Modifier
                                            .sizeIn(
                                                minHeight = 100.dp,
                                                minWidth = 160.dp,
                                                maxWidth = 180.dp,
                                                maxHeight = 120.dp
                                            ),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        if (errorLoaderState != null) {
                                            Text(
                                                text = errorLoaderState,
                                                overflow = TextOverflow.Ellipsis,
                                                maxLines = 3
                                            )
                                        }
                                        Button(colors = ButtonColors(
                                            containerColor = MaterialTheme.colorScheme.secondary,
                                            contentColor = MaterialTheme.colorScheme.primary,
                                            disabledContainerColor = MaterialTheme.colorScheme.secondary,
                                            disabledContentColor = MaterialTheme.colorScheme.primary
                                        ), onClick = {
                                            catalogSearchViewModel.viewModelScope.launch {
                                                catalogSearchViewModel.paginate()
                                            }
                                        }) {
                                            Text(text = "Reload?")
                                        }
                                    }

                                }
                            }

                            PaginationState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .shadow(20.dp)
                                            .height(60.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.onTertiaryContainer)
                                            .sizeIn(
                                                minHeight = 60.dp,
                                                minWidth = 60.dp,
                                                maxWidth = 80.dp,
                                                maxHeight = 80.dp
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }

                                }
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .height(110.dp)
                        )
                    }
                }
            }
        }
    }
}

fun <T> Flow<T>.pairWithPrevious(): Flow<Pair<T, T>> = flow {
    var previous: T? = null
    collect { value ->
        val prev = previous
        if (prev != null) emit(prev to value)
        previous = value
    }
}
//    .retry(3) { e -> (e is java.lang.IllegalArgumentException).also { if (it) delay(1000) } }
//    .retryWhen { cause: Throwable, attempt: Long ->
//        if (cause is IllegalArgumentException) {
//            delay(1000)
//            true
//        } else false
//    }
    .catch { error ->
        Log.e("pairWithPrevious()", error.toString())
    }


fun LazyListState.reachedBottom(): Boolean {
    val visibleItemsInfo = layoutInfo.visibleItemsInfo // Get the visible items
    return if (layoutInfo.totalItemsCount == 0) {
        false // Return false if there are no items
    } else {
        val lastVisibleItem = visibleItemsInfo.last() // Get the last visible item
        val viewportHeight =
            layoutInfo.viewportEndOffset +
                    layoutInfo.viewportStartOffset // Calculate the viewport height

        // Check if the last visible item is the last item in the list and fully visible
        // This indicates that the user has scrolled to the bottom
        (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount &&
                lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
    }
}


@Composable
fun ShowMainScreen(
    modifier: Modifier = Modifier,
    isInDarkTheme: () -> Boolean,
    onNavigateToDetailScreen: (Int) -> Unit,
    svgImageLoader: () -> ImageLoader,
    getTrendingAnime: @Composable () -> NewAnimeSearchModel,
    getTopUpcoming: @Composable () -> NewAnimeSearchModel,
    getTopAiring: @Composable () -> NewAnimeSearchModel,
    scrollState :  ScrollState
) {
    val viewModel: HomePageViewModel = hiltViewModel()
    val loadingSectionTopAiring by remember { mutableStateOf(false) }
    val loadingSectionTopUpcoming by remember { mutableStateOf(false) }
    val loadingSectionTopTrending by remember { mutableStateOf(false) }
    val lastTenAnimeFromWatchingSection by viewModel.showListOfWatching()
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val getJustTenAddedAnime by viewModel.showLastAdded()
        .collectAsStateWithLifecycle(initialValue = emptyList())
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(bottom = 50.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        AnimeHorizontalSection(
            items = lastTenAnimeFromWatchingSection,
            modifier = modifier,
            isInDarkTheme = isInDarkTheme,
            sectionTitle = "Now Watching"
        ) { data ->
            ShowSection(data, onNavigateToDetailScreen, modifier, svgImageLoader)
        }

        if (!loadingSectionTopTrending && getTrendingAnime().data.isNotEmpty()) {
            AnimeHorizontalSection(
                items = getTrendingAnime().data,
                modifier = modifier,
                isInDarkTheme = isInDarkTheme,
                sectionTitle = "Trending"
            ) { data ->
                ShowTopAnime(data, onNavigateToDetailScreen, modifier, svgImageLoader)
            }
        } else {
            LoadingPlacer(modifier, "Trending", isInDarkTheme)
        }

        AnimeHorizontalSection(
            items = getJustTenAddedAnime,
            modifier = modifier,
            isInDarkTheme = isInDarkTheme,
            sectionTitle = "Just Added"
        ) { data ->
            ShowSection(data, onNavigateToDetailScreen, modifier, svgImageLoader)
        }

        if (!loadingSectionTopAiring && getTopAiring().data.isNotEmpty()) {
            AnimeHorizontalSection(
                items = getTopAiring().data,
                modifier = modifier,
                isInDarkTheme = isInDarkTheme,
                sectionTitle = "Top Airing"
            ) { data ->
                ShowTopAnime(data, onNavigateToDetailScreen, modifier, svgImageLoader)
            }
        } else {
            LoadingPlacer(modifier, "Top Airing", isInDarkTheme)
        }

        if (!loadingSectionTopUpcoming && getTopUpcoming().data.isNotEmpty()) {
            AnimeHorizontalSection(
                items = getTopUpcoming().data,
                modifier = modifier,
                isInDarkTheme = isInDarkTheme,
                sectionTitle = "Top Upcoming"
            ) { data ->
                ShowTopAnime(data, onNavigateToDetailScreen, modifier, svgImageLoader)
            }
        } else {
            LoadingPlacer(modifier, "Top Upcoming", isInDarkTheme)
        }
    }
}

@Composable
fun LoadingPlacer(
    modifier: Modifier = Modifier,
    sectionTitle: String,
    isInDarkTheme: () -> Boolean
) {
    ShowSectionName(sectionTitle, modifier, isInDarkTheme)
    LazyRow(
        modifier = modifier.background(MaterialTheme.colorScheme.primary),
        userScrollEnabled = false
    ) {
        items(count = 5) {
            Spacer(modifier = Modifier.width(20.dp))
            Card(
                modifier = modifier
                    .height(300.dp)
                    .width(170.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmer(),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                shape = RectangleShape,
            ) {

            }
        }
        item { Spacer(modifier = Modifier.width(20.dp)) }
    }
    Spacer(modifier = Modifier.height(20.dp))
}


@Stable
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AnimeCardBox(
    data: AnimeEntity,
    onNavigateToDetailScreen: (Int) -> Unit,
    modifier: Modifier = Modifier,
    svgImageLoader: () -> ImageLoader,
    catalogSearchViewModel: CatalogSearchViewModel
) {
    val isCardClicked by remember { mutableStateOf(false) }
    val value by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = if (isCardClicked) 0.99f else 1f, // Изменяем значение в зависимости от нажатия на Card
        targetValue = if (isCardClicked) 1f else 0.99f, // Изменяем значение в зависимости от нажатия на Card
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600, easing = LinearEasing
            ), repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val coroutine = rememberCoroutineScope()


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
                Log.d("LONG CLICK", "LONG CLICK TRIGGERED")
                coroutine.launch(Dispatchers.IO) {
                    catalogSearchViewModel.showDialogForAnime(data.mal_id)
                }

            }) { onNavigateToDetailScreen(data.mal_id) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onTertiaryContainer),
        shape = RectangleShape,
    ) {
        Box {
            SubcomposeAsyncImage(modifier = Modifier
                .aspectRatio(9f / 11f)
                .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillBounds,
                model = data.images?.webp?.image_url,
                contentDescription = "Anime poster",
                loading = {
                    Spacer(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray)
                            .shimmer()
                    )
                },
                error = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color.White
                            )
                    ) {
                        Text(text = "Error while loading image")
                    }
                }
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
                            model = R.drawable.usergroup, imageLoader = svgImageLoader()
                        ), contentDescription = null
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text =
//                        formatScoredBy(
                        data.scored_by.toString()
//                        )
                        ,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                }
            }

            AddFavorites(
                mal_id = { data.mal_id },
                title = { data.title ?: "N/A" },
                score = { formatScore(data.score) },
                scoredBy = {
//                    formatScoredBy(
                    data.scored_by.toString()
//                )
                },
                animeImage = {
                    data.images?.jpg?.image_url ?: "No image"
                },
                modifier = Modifier,
                status = { data.status ?: "N/A" },
                rating = { data.rating ?: "N/A" },
                secondName = { data.title_japanese },
                airedFrom = { data.aired?.from },
                type = { data.type ?: "N/A" },
                svgImageLoader = svgImageLoader
            )


        }
        Row(modifier = Modifier.height(50.dp)) {
            Text(
                text = data.title ?: "N/A",
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


@Stable
@Composable
private fun LoadingCardPlacer(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .shimmer()
            .padding(horizontal = 10.dp)
            .shadow(20.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onTertiaryContainer),
        shape = RectangleShape,
    ) {
        Box {

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
                        text = "",
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
                    Box(modifier = Modifier.size(25.dp))
//                    Image(
//                        modifier = Modifier.size(25.dp), painter = rememberAsyncImagePainter(
//                            model = R.drawable.usergroup, imageLoader = svgImageLoader()
//                        ), contentDescription = null
//                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text =
//                        formatScoredBy(
                        "    data.scored_by.toString()"
//                        )
                        ,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                }
            }

        }
        Row(modifier = Modifier.height(50.dp)) {
            Text(
                text = "N/A",
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
                text = "Status: ",
                fontSize = 10.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(start = 10.dp),
                color = MaterialTheme.colorScheme.inversePrimary
            )
            Text(
                text = "Type: ",
                fontSize = 10.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
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

@Stable
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShowSection(
    data: AnimeItem,
    onNavigateToDetailScreen: (Int) -> Unit,
    modifier: Modifier,
    svgImageLoader: () -> ImageLoader
) {
    val painter = rememberAsyncImagePainter(model = data.animeImage)
    var isCardClicked by remember { mutableStateOf(false) }

    val homePageViewModel: HomePageViewModel = hiltViewModel()
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
            .width(170.dp)
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
                homePageViewModel.viewModelScope.launch(Dispatchers.IO) {
                    isCardClicked = true
                    delay(3000L)
                    isCardClicked = false
                }

            }) {
                data.id?.let {
                    onNavigateToDetailScreen(it)
                }
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RectangleShape,
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = modifier.background(MaterialTheme.colorScheme.primary)
        ) {
            Box(
                modifier = modifier
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
                                model = R.drawable.usergroup, imageLoader = svgImageLoader()
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

        }
        Column(
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = data.animeName,
                    textAlign = TextAlign.Start,
                    modifier = modifier
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

            Row(
                modifier = modifier,
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

//@Stable
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//private fun ShowTopAnime(
//    data: com.project.toko.homeScreen.data.model.newAnimeSearchModel.AnimeSearchData,
//    onNavigateToDetailScreen: (Int) -> Unit,
//    modifier: Modifier,
//    svgImageLoader: ImageLoader
//) {
//    val painter = rememberAsyncImagePainter(model = data.images.webp.image_url)
//    var isCardClicked by remember { mutableStateOf(false) }
//
//    val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
//    val value by rememberInfiniteTransition(label = "").animateFloat(
//        initialValue = if (isCardClicked) 0.99f else 1f, // Изменяем значение в зависимости от нажатия на Card
//        targetValue = if (isCardClicked) 1f else 0.99f, // Изменяем значение в зависимости от нажатия на Card
//        animationSpec = infiniteRepeatable(
//            animation = tween(
//                durationMillis = 600, easing = LinearEasing
//            ), repeatMode = RepeatMode.Reverse
//        ), label = ""
//    )
//
//    Card(
//        modifier = modifier
//            .height(300.dp)
//            .width(180.dp)
//            .shadow(20.dp)
//            .then(if (isCardClicked) {
//                modifier.graphicsLayer {
//                    scaleX = value
//                    scaleY = value
//                }
//            } else {
//                modifier
//            })
//            .clip(RoundedCornerShape(16.dp))
//            .combinedClickable(onLongClick = {
//                homeScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
//                    isCardClicked = true
//                    homeScreenViewModel.onDialogLongClick(data.id ?: 0)
//                    delay(3000L)
//                    isCardClicked = false
//                }
//
//            }) {
//                onNavigateToDetailScreen(data.id)
//
//            },
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
//        shape = RectangleShape,
//    ) {
//        Box(
//            contentAlignment = Alignment.BottomCenter,
//            modifier = modifier.background(MaterialTheme.colorScheme.primary)
//        ) {
//            Box(
//                modifier = modifier
//                    .fillMaxSize()
//                    .background(MaterialTheme.colorScheme.primary)
//            ) {
//                // Coil image loader
//                Image(
//                    painter = painter,
//                    contentDescription = "Images for each Anime",
//                    modifier = modifier
//                        .aspectRatio(9f / 11f)
//                        .clip(RoundedCornerShape(10.dp)),
//                    contentScale = ContentScale.FillBounds
//                )
//
//                Column(
//                    modifier = modifier
//                        .width(50.dp)
//                        .clip(RoundedCornerShape(bottomEnd = 15.dp))
//                        .background(scoreBoardColor)
//                ) {
//                    Row(
//                        modifier = modifier
//                            .fillMaxWidth()
//                            .padding(top = 5.dp),
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//
//                        Text(
//                            text = formatScore(data.score),
//                            color = Color.White,
//                            fontSize = 15.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                    Column(
//                        modifier = modifier
//                            .fillMaxWidth()
//                            .height(50.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Top
//                    ) {
//                        Image(
//                            modifier = modifier.size(25.dp), painter = rememberAsyncImagePainter(
//                                model = R.drawable.usergroup, imageLoader = svgImageLoader
//                            ), contentDescription = null
//                        )
//                        Text(
//                            textAlign = TextAlign.Center,
//                            text = formatScoredBy(data.scored_by),
//                            color = Color.White,
//                            fontSize = 8.sp,
//                            fontWeight = FontWeight.Bold,
//                            modifier = modifier
//                        )
//                    }
//                }
//
//                AddFavorites(
//                    mal_id = data.id,
//                    title = data.title,
//                    score = formatScore(data.score),
//                    scoredBy = formatScoredBy(data.scored_by),
//                    animeImage = data.images.jpg.image_url,
//                    modifier = modifier,
//                    status = data.status,
//                    rating = data.rating ?: "N/A",
//                    secondName = data.title_japanese,
//                    airedFrom = data.aired.from,
//                    type = data.type ?: "N/A",
//                    svgImageLoader = svgImageLoader
//                )
//            }
//            Column(
//                modifier = modifier
//            ) {
//                Text(
//                    text = data.title,
//                    textAlign = TextAlign.Start,
//                    modifier = modifier
//                        .fillMaxWidth()
//                        .padding(end = 5.dp, top = 5.dp, bottom = 5.dp, start = 10.dp),
//                    lineHeight = 16.sp,
//                    fontSize = 16.sp,
//                    overflow = TextOverflow.Ellipsis,
//                    minLines = 2,
//                    maxLines = 2,
//                    color = MaterialTheme.colorScheme.onPrimary,
//                    fontFamily = evolventaBoldFamily,
//                    fontWeight = FontWeight.W900
//                )
//
//                Row(
//                    modifier = modifier.fillMaxWidth(1f),
//                    horizontalArrangement = Arrangement.Start
//                ) {
//                    Text(
//                        text = "Status: " + data.status,
//                        fontSize = 10.sp,
//                        textAlign = TextAlign.Left,
//                        modifier = modifier.padding(start = 10.dp),
//                        color = MaterialTheme.colorScheme.inversePrimary
//                    )
//                }
//                Row(
//                    modifier = modifier
//                        .fillMaxWidth(1f)
//                        .padding(bottom = 10.dp),
//                    horizontalArrangement = Arrangement.Start
//                ) {
//                    Text(
//                        text = "Type: " + data.type,
//                        fontSize = 10.sp,
//                        textAlign = TextAlign.Left,
//                        modifier = modifier.padding(start = 10.dp),
//                        color = MaterialTheme.colorScheme.inversePrimary
//                    )
//                }
//
//            }
//        }
//    }
//}


@Stable
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShowTopAnime(
    data: AnimeSearchData,
    onNavigateToDetailScreen: (Int) -> Unit,
    modifier: Modifier,
    svgImageLoader: () -> ImageLoader
) {
    val painter = rememberAsyncImagePainter(model = data.images.webp.image_url)
    var isCardClicked by remember { mutableStateOf(false) }

    val homePageViewModel: HomePageViewModel = hiltViewModel()
    val value by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = if (isCardClicked) 0.99f else 1f,
        targetValue = if (isCardClicked) 1f else 0.99f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )


    Card(
        modifier = modifier
            .height(300.dp)
            .width(170.dp)
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
                homePageViewModel.viewModelScope.launch(Dispatchers.IO) {
                    isCardClicked = true
                    delay(3000L)
                    isCardClicked = false
                }

            }) {
                data.id?.let {
                    onNavigateToDetailScreen(it)
                }
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RectangleShape,
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = modifier.background(MaterialTheme.colorScheme.primary)
        ) {
            Box(
                modifier = modifier
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
                                model = R.drawable.usergroup, imageLoader = svgImageLoader()
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
                    mal_id = { data.id },
                    title = { data.title },
                    score = { formatScore(data.score) },
                    scoredBy = { formatScoredBy(data.scored_by) },
                    animeImage = { data.images.jpg.image_url },
                    modifier = modifier,
                    status = { data.status },
                    rating = { data.rating ?: "N/A" },
                    secondName = { data.title_japanese },
                    airedFrom = { data.aired.from },
                    type = { data.type },
                    svgImageLoader = svgImageLoader
                )
            }

        }
        Column(
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = data.title,
                    textAlign = TextAlign.Start,
                    modifier = modifier
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

            Row(
                modifier = modifier,
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
