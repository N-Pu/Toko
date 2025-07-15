package com.project.toko.homeScreen.ui.homeScreen

import android.util.Log
import com.project.toko.homeScreen.ui.viewModel.HomePageViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.project.toko.R
import com.project.toko.core.ui.pullToRefpresh.PullToRefreshLayout
import com.project.toko.core.ui.theme.DarkSearchBarColor
import com.project.toko.core.ui.theme.SearchBarColor
import com.project.toko.core.ui.theme.evolventaBoldFamily
import com.project.toko.core.ui.theme.iconColorInSearchPanel
import com.project.toko.dataBase.search.ui.viewmodel.CatalogSearchViewModel
import com.project.toko.homeScreen.data.model.linkChangerModel.Genre
import com.project.toko.homeScreen.data.model.linkChangerModel.OrderBy
import com.project.toko.homeScreen.data.model.linkChangerModel.Rating
import com.project.toko.homeScreen.data.model.linkChangerModel.Types
import com.project.toko.homeScreen.data.model.linkChangerModel.getGenres
import com.project.toko.homeScreen.data.model.linkChangerModel.getOrderBy
import com.project.toko.homeScreen.data.model.linkChangerModel.getRating
import com.project.toko.homeScreen.data.model.linkChangerModel.getTypes
import com.project.toko.homeScreen.data.model.newAnimeSearchModel.NewAnimeSearchModel
import com.project.toko.homeScreen.data.model.tabRow.returnListOfTabItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetailScreen: (Int) -> Unit,
    isInDarkTheme: () -> Boolean,
    svgImageLoader: () -> ImageLoader,
    drawerState: DrawerState,
    catalogSearchViewModel: CatalogSearchViewModel,
    onClickOrderBy: (MutableState<String?>, OrderBy) -> Unit,
    onClickFilterTypes: (MutableState<String?>, Types) -> Unit,
    onClickGenres: (MutableState<Set<Int>>, Genre) -> Unit,
    onClickRating: (MutableState<String?>, Rating) -> Unit,
    onCurrentScore: (ScoreRange) -> Unit,
    switchIndicator: MutableState<Boolean>,
    hideBottomBar: () -> Unit,
    showBottomBar: () -> Unit,
    getTrendingAnime : @Composable () -> NewAnimeSearchModel,
    getTopAiring : @Composable () -> NewAnimeSearchModel,
    getTopUpcoming : @Composable () -> NewAnimeSearchModel,
) {

    val drawerCoroutineScope = rememberCoroutineScope()
    val refreshCoroutineScope = rememberCoroutineScope()
    var query by rememberSaveable { mutableStateOf("") }

    var refreshState by rememberSaveable { mutableStateOf(false) }
    val swipeRefreshState =
        rememberSwipeRefreshState(
            isRefreshing = refreshState
        )



    PullToRefreshLayout(composable = {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.primary)

        ) {
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(MaterialTheme.colorScheme.error)
            )
            Column(
                modifier = modifier
                    .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                    .height(140.dp)
                    .shadow(20.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.error)
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 0.dp)

            ) {
                // Logotype on the top left
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.inversePrimary,
                        modifier = modifier
                            .size(30.dp)
                            .clickable {
                                drawerCoroutineScope.launch {
                                    try {
                                        drawerState.open()
                                    } catch (e: Throwable) {
                                        Log.e("TAG", coroutineContext.toString() + " " + e.message)
                                    }
                                }
                            }
                    )
                    Image(
                        painter = rememberAsyncImagePainter(model = R.drawable.tokominilogo),
                        contentDescription = null,
                        modifier = modifier
                            .height(50.dp)
                            .width(70.dp),
                        alpha = 0.8f,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                    )
                }
                Row(
                    modifier = modifier
                        .wrapContentSize()
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isInDarkTheme()) DarkSearchBarColor else SearchBarColor
                        ),
                    verticalAlignment = Alignment.Bottom
                ) {
                    OutlinedTextField(
                        placeholder = { Text(text = "Search...", color = iconColorInSearchPanel) },
                        value = query, onValueChange = {

                            query = it

                            if (query.isBlank()) {
                                catalogSearchViewModel.updateFilter {
                                    this.copy(query = null)
                                }
                            } else {
                                catalogSearchViewModel.updateFilter {
                                    this.copy(query = query)
                                }
                            }

                        },
                        modifier = modifier
                            .clip(RoundedCornerShape(30.dp))
                            .height(50.dp)
                            .fillMaxWidth(),
                        prefix = {
                            Icon(Icons.Filled.Search, "Search Icon", tint = iconColorInSearchPanel)
                        },
                        suffix = {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = if (switchIndicator.value) R.drawable.search_back else R.drawable.search_home,
                                    imageLoader = svgImageLoader()
                                ),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                                modifier = modifier
                                    .fillMaxHeight(0.5f)
                                    .clickable {
                                        switchIndicator.value = !switchIndicator.value
                                        if (!switchIndicator.value) {
                                            showBottomBar()
                                        }
                                    }
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = iconColorInSearchPanel,
                            focusedPlaceholderColor = iconColorInSearchPanel,
                            unfocusedPlaceholderColor = iconColorInSearchPanel,
                            cursorColor = iconColorInSearchPanel,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        )
                    )
                }
            }

            TabSelectionMenu(
                onClickFilterTypes = onClickFilterTypes,
                onClickOrderBy = onClickOrderBy,
                onClickRating = onClickRating,
                onClickGenres = onClickGenres,
                onCurrentScore = onCurrentScore
            ) { switchIndicator }

            GridAdder(
                onNavigateToDetailScreen = onNavigateToDetailScreen,
                switch = { switchIndicator.value },
                isInDarkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader,
                catalogSearchViewModel = catalogSearchViewModel,
                hideBottomBar = hideBottomBar,
                showBottomBar = showBottomBar,
                getTrendingAnime = getTrendingAnime,
                getTopAiring = getTopAiring,
                getTopUpcoming = getTopUpcoming,
            )


        }
    }, onLoad = {

        refreshCoroutineScope.launch(Dispatchers.IO) {
            try {
                refreshState = true
                catalogSearchViewModel.refresh()
            } catch (e: Throwable) {
                Log.e("TAG", coroutineContext.toString() + " " + e.message)
            } finally {
                refreshState = false
            }
        }


    }, swipeRefreshState = swipeRefreshState)

}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TabSelectionMenu(
    modifier: Modifier = Modifier,
    onClickFilterTypes: (MutableState<String?>, Types) -> Unit,
    onClickRating: (MutableState<String?>, Rating) -> Unit,
    onClickOrderBy: (MutableState<String?>, OrderBy) -> Unit,
    onClickGenres: (MutableState<Set<Int>>, Genre) -> Unit,
    onCurrentScore: (ScoreRange) -> Unit,
    switchIndicator: () -> MutableState<Boolean>,
) {
    var isTabMenuOpen by rememberSaveable { mutableStateOf(false) }
    val tabItems = remember { returnListOfTabItems() }
    val pagerState = rememberPagerState { tabItems.size }
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val selectedTypeName = rememberSaveable { mutableStateOf<String?>(null) }
    val selectedGenreIds = rememberSaveable { mutableStateOf<Set<Int>>(emptySet()) }
    val selectedOrderBy = rememberSaveable { mutableStateOf<String?>(null) }
    val selectedRating = rememberSaveable { mutableStateOf<String?>(null) }
    val selectedIndex = rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }


    Row(
        horizontalArrangement = Arrangement.Center, modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        ScrollableTabRow(
            modifier = modifier
                .fillMaxWidth(0.85f),
            selectedTabIndex = selectedTabIndex,
            contentColor = Color.Transparent,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    SecondaryIndicator(
                        modifier = modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            },
            edgePadding = 0.dp
        ) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        if (selectedTabIndex == index) {
                            isTabMenuOpen = !isTabMenuOpen
                        } else {
                            selectedTabIndex = index
                            isTabMenuOpen = true
                        }
                    },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.primary,
                ) {
                    Text(
                        text = item.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = evolventaBoldFamily
                    )
                    Spacer(modifier = modifier.height(5.dp))
                }
            }
        }
    }
    Spacer(modifier = modifier.height(20.dp))
    if (isTabMenuOpen) {
        Box(modifier = modifier.animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessVeryLow))) {
            HorizontalPager(
                state = pagerState, modifier = modifier
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessVeryLow))
            ) { index ->
                FlowRow(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    content = {
                        when (tabItems[index]) {
                            tabItems[0] -> {
                                ShowTabInsides(
                                    isTouched = { it.name == selectedTypeName.value },
                                    onClick = { onClickFilterTypes(selectedTypeName, it) },
                                    list = getTypes()
                                )
                            }

                            tabItems[1] -> {
                                ShowGenres(
                                    isTouched = { it.id in selectedGenreIds.value },
                                    onClick = { onClickGenres(selectedGenreIds, it) },
                                    list = getGenres()
                                )
                            }

                            tabItems[2] -> {
                                ShowTabInsides(
                                    isTouched = { it.name == selectedRating.value },
                                    onClick = { onClickRating(selectedRating, it) },
                                    list = getRating()
                                )
                            }

                            tabItems[3] -> {
                                ScoreBar(
                                    switchIndicator = switchIndicator,
                                    selectedIndex = selectedIndex,
                                    onCurrentScore = onCurrentScore,
                                )
                            }

                            tabItems[4] -> {
                                ShowTabInsides(
                                    isTouched = { it.name == selectedOrderBy.value },
                                    onClick = { onClickOrderBy(selectedOrderBy, it) },
                                    list = getOrderBy()
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

// Упрощенная модель данных
data class ScoreRange(
    val display: String,  // Отображаемое значение ("—", "1", "2" и т.д.)
    val minScore: String?,
    val maxScore: String?
)

@Composable
private fun ScoreBar(
    modifier: Modifier = Modifier,
    onCurrentScore: (ScoreRange) -> Unit,
    switchIndicator: () -> MutableState<Boolean>,
    selectedIndex: MutableState<Int>,
) {
    // Все возможные диапазоны оценок
    val scoreRanges = remember {
        listOf(
            ScoreRange("—", null, null),
            ScoreRange("1", "0.001", "1.999"),
            ScoreRange("2", "2.000", "2.999"),
            ScoreRange("3", "3.000", "3.999"),
            ScoreRange("4", "3.999", "4.999"),
            ScoreRange("5", "4.999", "5.999"),
            ScoreRange("6", "5.999", "6.999"),
            ScoreRange("7", "6.999", "7.999"),
            ScoreRange("8", "7.999", "8.999"),
            ScoreRange("9", "8.999", "9.999"),
            ScoreRange("10", "9.000", "10.000")
        )
    }


    val pagerState = rememberPagerState(initialPage = selectedIndex.value) { scoreRanges.size }
    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(10)
    )

    // Цвет круга в зависимости от выбранного значения
    val circleColor = when (selectedIndex.value) {
        0 -> Color(0f, 0f, 0f, 0.3f)  // Серый для "—"
        1, 2, 3 -> Color(255, 77, 87)  // Красный для 1-3
        4, 5, 6 -> Color(255, 160, 0)   // Оранжевый для 4-6
        else -> MaterialTheme.colorScheme.onPrimaryContainer // Зеленый для 7-10
    }

    // Обновляем ViewModel при изменении выбора
    LaunchedEffect(pagerState.currentPage) {
        selectedIndex.value = pagerState.currentPage
        val range = scoreRanges[selectedIndex.value]
        onCurrentScore(range)
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != 0) {
            switchIndicator().value = true
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .height(200.dp)
            .drawBehind {
                drawCircle(
                    color = circleColor,
                    radius = 100.dp.toPx(),
                )
            },
        contentPadding = PaddingValues(horizontal = 110.dp),
        flingBehavior = fling
    ) { page ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                modifier = modifier.animateContentSize(),
                text = scoreRanges[page].display,
                fontSize = if (page == pagerState.currentPage) 120.sp else 80.sp,
                color = if (page == pagerState.currentPage) MaterialTheme.colorScheme.primary else Color(
                    189,
                    189,
                    189
                ),
                fontWeight = if (page == pagerState.currentPage) FontWeight.ExtraBold else FontWeight.Bold,
                maxLines = 1,
                fontFamily = evolventaBoldFamily
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T : FilterItem> ShowGenres(
    modifier: Modifier = Modifier,
    isTouched: (T) -> Boolean,
    onClick: (T) -> Unit,
    list: List<T>
) {
    val genreList = remember { list }
    Box(
        modifier = modifier
            .fillMaxSize()
            .height(310.dp)
            .verticalScroll(rememberScrollState())
    ) {
        FlowRow(horizontalArrangement = Arrangement.Center) {
            genreList.forEach { genre ->
                ButtonCreator(
                    text = genre.name,
                    isTouched = { isTouched(genre) },
                    onClick = {
                        onClick(genre)
                    },
                    modifier = modifier,
                )
                Spacer(
                    modifier = modifier
                        .width(8.dp)
                        .height(50.dp)
                )
            }
        }
    }
}

@Composable
private fun ButtonCreator(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isTouched: () -> Boolean,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                if (isTouched()) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
        content = {
            Text(
                text = text,
                color = if (isTouched()) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = modifier.padding(8.dp),
                fontSize = 18.sp
            )
        }
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T : FilterItem> ShowTabInsides(
    modifier: Modifier = Modifier,
    isTouched: (T) -> Boolean,
    onClick: (T) -> Unit,
    list: List<T>
) {
    FlowRow(horizontalArrangement = Arrangement.Center, modifier = modifier.fillMaxWidth()) {
        list.forEach { item ->
            ButtonCreator(
                isTouched = { isTouched(item) },
                onClick = { onClick(item) },
                text = item.name,
                modifier = modifier
            )
            Spacer(
                modifier = modifier
                    .width(8.dp)
                    .height(50.dp)
            )
        }
    }
}


interface FilterItem {
    val name: String
}
