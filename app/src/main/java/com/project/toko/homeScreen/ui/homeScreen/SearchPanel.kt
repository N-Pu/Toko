package com.project.toko.homeScreen.ui.homeScreen

import com.project.toko.homeScreen.ui.viewModel.HomeScreenViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.project.toko.R
import com.project.toko.core.MainViewModel
import com.project.toko.core.ui.pullToRefpresh.PullToRefreshLayout
import com.project.toko.core.ui.theme.DarkSearchBarColor
import com.project.toko.core.ui.theme.SearchBarColor
import com.project.toko.core.ui.theme.evolventaBoldFamily
import com.project.toko.core.ui.theme.iconColorInSearchPanel
import com.project.toko.dataBase.search.ui.viewmodel.AnimeViewModel
import com.project.toko.homeScreen.data.model.linkChangerModel.getGenres
import com.project.toko.homeScreen.data.model.linkChangerModel.getOrderBy
import com.project.toko.homeScreen.data.model.linkChangerModel.getRating
import com.project.toko.homeScreen.data.model.linkChangerModel.getTypes
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
    animeViewModel: AnimeViewModel,
    viewModel: HomeScreenViewModel,
    mainViewModel: MainViewModel
) {
    val switchIndicator = remember { animeViewModel.switchIndicator }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var query by rememberSaveable { mutableStateOf("") }

    var refreshState by rememberSaveable { mutableStateOf(false) }
    val swipeRefreshState =
        rememberSwipeRefreshState(
            isRefreshing = refreshState
        )

    LaunchedEffect(key1 = switchIndicator.value) {
        if (switchIndicator.value.not()) {
            viewModel.loadAllSections(context)
            return@LaunchedEffect
        }
    }




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
                                scope.launch(Dispatchers.IO) {
                                    drawerState.open()
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
                                animeViewModel.updateFilter {
                                    this.copy(query = null)
                                }
                            } else {
                                animeViewModel.updateFilter {
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
                                        if (!switchIndicator.value){
                                            mainViewModel.showBottomBar()
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
                viewModel,
                modifier,
                animeViewModel
            ) { switchIndicator }

            GridAdder(
                onNavigateToDetailScreen = onNavigateToDetailScreen,
                switch = { switchIndicator.value },
                isInDarkTheme = isInDarkTheme,
                svgImageLoader = svgImageLoader,
                animeViewModel = animeViewModel,
                viewModel = viewModel,
                mainViewModel = mainViewModel
            )


        }
    }, onLoad = {
        animeViewModel.viewModelScope.launch {
            refreshState = true
            animeViewModel.refresh()
            refreshState = false
        }
    }, swipeRefreshState = swipeRefreshState)

}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TabSelectionMenu(
    viewModel: HomeScreenViewModel,
    modifier: Modifier,
    animeViewModel: AnimeViewModel,
    switchIndicator: () -> MutableState<Boolean>,
) {
    val isTabMenuOpen = remember { viewModel.isTabMenuOpen }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var sizeOfCurrentComposable by remember {
        mutableStateOf(IntSize.Zero)
    }

    val tabItems = remember {
        returnListOfTabItems()
    }
    val pagerState = rememberPagerState { tabItems.size }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
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
                    TabRowDefaults.Indicator(
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
                            isTabMenuOpen.value = !isTabMenuOpen.value
                        } else {
                            selectedTabIndex = index
                            isTabMenuOpen.value = true
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
    if (isTabMenuOpen.value) {
        Box(modifier = modifier.animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessVeryLow))) {
            HorizontalPager(
                state = pagerState, modifier = modifier
                    .fillMaxWidth()
                    .onSizeChanged {
                        sizeOfCurrentComposable = it
                    }
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
                                ShowTypes(
                                    switchIndicator = switchIndicator,
                                    animeViewModel = animeViewModel
                                )
                            }

                            tabItems[1] -> {
                                ShowGenres(
                                    switchIndicator = switchIndicator,
                                    viewModel = animeViewModel
                                )
                            }

                            tabItems[2] -> {
                                ShowRating(
                                    switchIndicator = switchIndicator,
                                    animeViewModel = animeViewModel
                                )
                            }

                            tabItems[3] -> {
                                ScoreBar(
                                    animeViewModel = animeViewModel,
                                    switchIndicator = switchIndicator
                                )
                            }

                            tabItems[4] -> {
                                ShowOrderBy(
                                    animeViewModel = animeViewModel,
                                    switchIndicator = switchIndicator
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
    animeViewModel: AnimeViewModel,
    modifier: Modifier = Modifier,
    switchIndicator: () -> MutableState<Boolean>
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

    // Сохраняем выбранный индекс
    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    val pagerState = rememberPagerState(initialPage = selectedIndex) { scoreRanges.size }
    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(10)
    )

    // Цвет круга в зависимости от выбранного значения
    val circleColor = when (selectedIndex) {
        0 -> Color(0f, 0f, 0f, 0.3f)  // Серый для "—"
        1, 2, 3 -> Color(255, 77, 87)  // Красный для 1-3
        4, 5, 6 -> Color(255, 160, 0)   // Оранжевый для 4-6
        else -> MaterialTheme.colorScheme.onPrimaryContainer // Зеленый для 7-10
    }

    // Обновляем ViewModel при изменении выбора
    LaunchedEffect(pagerState.currentPage) {
        selectedIndex = pagerState.currentPage
        val range = scoreRanges[selectedIndex]
//        viewModel.onScoreChange(range.minScore, range.maxScore)
        animeViewModel.updateFilter {
            this.copy(min_score = range.minScore, max_score = range.maxScore)
        }
        switchIndicator().value = true
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .height(200.dp)
            .drawBehind {
                drawCircle(
                    color = circleColor,
                    radius = 75.dp.toPx(),
                )
            },
        contentPadding = PaddingValues(horizontal = 110.dp),
        flingBehavior = fling
    ) { page ->
        val isSelected = page == pagerState.currentPage
        val textColor = if (isSelected) MaterialTheme.colorScheme.primary else Color(189, 189, 189)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = scoreRanges[page].display,
                fontSize = if (isSelected) 100.sp else 80.sp,
                color = textColor,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                maxLines = 1,
                fontFamily = evolventaBoldFamily
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ShowGenres(
    modifier: Modifier = Modifier,
    switchIndicator: () -> MutableState<Boolean>,
    viewModel: AnimeViewModel,
) {
    // Сохраняем только выбранные ID жанров
    var selectedGenreIds by rememberSaveable { mutableStateOf<Set<Int>>(emptySet()) }

    val genreList = remember { getGenres() }

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
                    isTouched = { genre.id in selectedGenreIds },
                    onClick = {
                        // Обновляем выбранные жанры
                        selectedGenreIds = if (genre.id in selectedGenreIds) {
                            selectedGenreIds - genre.id
                        } else {
                            selectedGenreIds + genre.id
                        }

                        // Отправляем ID жанра в ViewModel
                        viewModel.onGenreChange(genre.id)

                        // Обновляем индикатор изменений
                        switchIndicator().value = true
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
    text: String,
    onClick: () -> Unit,
    isTouched: () -> Boolean,
    modifier: Modifier
) {
    Box(
        modifier = Modifier
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

@Composable
private fun ShowRating(
    modifier: Modifier = Modifier,
    animeViewModel: AnimeViewModel,
    switchIndicator: () -> MutableState<Boolean>
) {
    val ratingList = remember {
        getRating()
    }
    var selectedRating by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        ratingList.forEach { rating ->
            ButtonCreator(
                isTouched = { rating.name == selectedRating },
                onClick = {
                    selectedRating = if (rating.name == selectedRating) null else rating.name
//                    viewModel.onRatingChange(selectedRating)
                    animeViewModel.updateFilter { this.copy(rating = selectedRating) }
                    switchIndicator().value = true
                },
                text = rating.name,
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


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ShowTypes(
    modifier: Modifier = Modifier,
    animeViewModel: AnimeViewModel,
    switchIndicator: () -> MutableState<Boolean>
) {
    // Сохраняем только выбранное имя типа (String), а не весь объект Types
    var selectedTypeName by rememberSaveable { mutableStateOf<String?>(null) }

    // Получаем список типов
    val typeList = remember { getTypes() }

    FlowRow(horizontalArrangement = Arrangement.Center, modifier = modifier.fillMaxWidth()) {
        typeList.forEach { type ->
            ButtonCreator(
                isTouched = { type.name == selectedTypeName },
                onClick = {
                    selectedTypeName =
                        if (type.name == selectedTypeName) null else type.name
//                    animeViewModel.onTypeChange(selectedTypeName)
                    animeViewModel.updateFilter { this.copy(type = selectedTypeName) }
                    switchIndicator().value = true
                },
                text = type.name,
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


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ShowOrderBy(
    animeViewModel: AnimeViewModel,
    modifier: Modifier = Modifier,
    switchIndicator: () -> MutableState<Boolean>
) {
    // Сохраняем только выбранное имя типа (String), а не весь объект Types
    var selectedOrderBy by rememberSaveable { mutableStateOf<String?>(null) }

    // Получаем список типов
    val orderByList = remember { getOrderBy() }

    FlowRow(horizontalArrangement = Arrangement.Center, modifier = modifier.fillMaxWidth()) {
        orderByList.forEach { type ->
            ButtonCreator(
                isTouched = { type.name == selectedOrderBy },
                onClick = {
                    selectedOrderBy =
                        if (type.name == selectedOrderBy) null else type.name
//                    animeViewModel.onOrderByChange(selectedOrderBy)
                    animeViewModel.updateFilter { this.copy(orderBy = selectedOrderBy) }
                    switchIndicator().value = true
                },
                text = type.name,
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
