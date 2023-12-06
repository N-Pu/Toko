package com.project.toko.homeScreen.presentation_layer.homeScreen

import com.project.toko.homeScreen.viewModel.HomeScreenViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.project.toko.R
import com.project.toko.core.presentation_layer.animations.LoadingAnimation
import com.project.toko.core.presentation_layer.theme.DarkSearchBarColor
import com.project.toko.core.presentation_layer.theme.SearchBarColor
import com.project.toko.core.presentation_layer.theme.iconColorInSearchPanel
import com.project.toko.homeScreen.model.linkChangerModel.Score
import com.project.toko.homeScreen.model.linkChangerModel.getMinMaxScore
import com.project.toko.homeScreen.model.tabRow.TabItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavHostController, viewModelProvider: ViewModelProvider,
    modifier: Modifier
) {
    val viewModel = viewModelProvider[HomeScreenViewModel::class.java]
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val isSearching by viewModel.isPerformingSearch.collectAsStateWithLifecycle()
    val isTabMenuOpen = remember { mutableStateOf(true) }
    val svgImageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()

    val switchIndicator = viewModel.switchIndicator
//    var active by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)

    ) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                .height(140.dp)
                .shadow(20.dp)
                .fillMaxWidth(1f)
                .background(MaterialTheme.colorScheme.error)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 0.dp)

        ) {
            // Logotype on the top left
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = R.drawable.tokominilogo),
                    contentDescription = "None",
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
                    .background(if (isSystemInDarkTheme()) DarkSearchBarColor else SearchBarColor)
                ,
                verticalAlignment = Alignment.Bottom
            ) {

//                DockedSearchBar(
//                    placeholder = { Text(text = "Search...") },
//                    query = searchText ?: "",
//                    onQueryChange = viewModel::onSearchTextChange,
//                    onSearch = {active = false},
//                    active = active,
//                    onActiveChange = {active = it}, trailingIcon = {
//                        Image(
//                            painter = rememberAsyncImagePainter(
//                                model = R.drawable.switchinsearch,
//                                imageLoader = svgImageLoader
//                            ),
//                            contentDescription = null,
//                            colorFilter = if (switchIndicator.value) ColorFilter.tint(LightGreen) else null,
//                            modifier = modifier
//                                .size(40.dp)
//                                .clickable {
//                                    switchIndicator.value = !switchIndicator.value
//                                }
//                        )
//                    }
//                    , leadingIcon = {Icon(Icons.Filled.Search, "Search Icon", tint = iconColorInSearchPanel)},
//                    modifier = modifier
//                        .clip(RoundedCornerShape(30.dp))
//                        .height(50.dp)
//                        .fillMaxWidth(1f)
//                ) {
//
//                }

                OutlinedTextField(
                    placeholder = { Text(text = "Search...", color = Color.Gray) },
                    value = searchText ?: "",
                    onValueChange = viewModel::onSearchTextChange,
                    modifier = modifier
                        .clip(RoundedCornerShape(30.dp))
                        .height(50.dp)
                        .fillMaxWidth(1f),
                    prefix = {
                        Icon(Icons.Filled.Search, "Search Icon", tint = iconColorInSearchPanel)
                    },
                    suffix = {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.switchinsearch,
                                imageLoader = svgImageLoader
                            ),
                            contentDescription = null,
                            colorFilter = if (switchIndicator.value) ColorFilter.tint(MaterialTheme.colorScheme.secondary) else null,
                            modifier = modifier
                                .size(40.dp)
                                .clickable {
                                    switchIndicator.value = !switchIndicator.value
                                }
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        cursorColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent

                    )
                )
            }
        }

        TabSelectionMenu(viewModel, modifier, isTabMenuOpen)

        if (isSearching.not()) {
            GridAdder(
                navController = navController,
                viewModelProvider = viewModelProvider,
                modifier = modifier,
                isTabMenuOpen = isTabMenuOpen,
                switch = switchIndicator
            )
        } else {
            LoadingAnimation()
        }

    }
}


@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
private fun TabSelectionMenu(
    viewModel: HomeScreenViewModel,
    modifier: Modifier,
    isTabMenuOpen: MutableState<Boolean>
) {

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var sizeOfCurrentComposable by remember {
        mutableStateOf(IntSize.Zero)
    }

    val tabItems = listOf(
        TabItem("Type"),
        TabItem("Genres"),
        TabItem("Rating"),
        TabItem("Score"),
        TabItem("Order By")
    )
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
//                    modifier = modifier.background(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = item.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = modifier.height(5.dp))
                }
            }
        }
    }

    if (isTabMenuOpen.value) {
        Spacer(
            modifier = modifier
                .height(20.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        )
        Box(modifier = modifier.animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessVeryLow))) {

            HorizontalPager(
                state = pagerState, modifier = modifier
                    .fillMaxWidth()
                    .onSizeChanged {
                        sizeOfCurrentComposable = it
                    }
//                    .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessVeryLow))
            ) { index ->
                FlowRow(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    content = {
                        when (tabItems[index]) {
                            tabItems[0] -> {
                                ShowTypes(viewModel, modifier)
                            }

                            tabItems[1] -> {
                                ShowGenres(viewModel, modifier)
                            }

                            tabItems[2] -> {
                                ShowRating(viewModel, modifier)
                            }

                            tabItems[3] -> {
                                ScoreBar(viewModel, modifier)
                            }

                            tabItems[4] -> {
                                ShowOrderBy(viewModel, modifier)
                            }
                        }
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScoreBar(
    viewModel: HomeScreenViewModel,
    modifier: Modifier
) {
    val selectedNumber = viewModel.scoreState
    val items = listOf("—", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
    val pagerState = rememberPagerState(initialPage = selectedNumber.intValue) { items.size }
    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(10)
    )
    val circleColor = when (items[pagerState.currentPage]) {
        items[1], items[2], items[3] -> Color(255, 77, 87)
        items[4], items[5], items[6] -> Color(255, 160, 0)
        items[7], items[8], items[9], items[10] -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> Color(0f, 0f, 0f, 0.3f)
    }
    val circleRadius = 75.dp
    val minMaxScore = getMinMaxScore(pagerState.currentPage)
    selectedNumber.intValue = pagerState.currentPage

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .height(200.dp)
            .drawBehind {
                drawCircle(
                    color = circleColor,
                    radius = circleRadius.toPx(),
                )
            },
        contentPadding = PaddingValues(horizontal = 110.dp),
        flingBehavior = fling
    ) { page ->
        val colorText =
            if (pagerState.currentPage == page) MaterialTheme.colorScheme.primary else Color(
                189,
                189,
                189
            )
        Box(
            contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()
        ) {
            Text(
                text = items[page],
                fontSize = if ((pagerState.currentPage == page)) 100.sp else 80.sp,
                color = colorText,
                fontWeight = if ((pagerState.currentPage == page)) FontWeight.ExtraBold else FontWeight.Bold,
                modifier = modifier,
                maxLines = 1,
                overflow = TextOverflow.Visible
            )
        }
    }

    LaunchedEffect(key1 = selectedNumber.intValue) {
        if (selectedNumber.intValue != 0) {
            delay(1000L)
            viewModel.pre_min_score.value = Score(minMaxScore.minScore)
            viewModel.pre_max_score.value = Score(minMaxScore.maxScore)
            viewModel.addAllParams()
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ShowGenres(
    viewModel: HomeScreenViewModel,
    modifier: Modifier
) {
    val selectedGenre by viewModel.selectedGenre.collectAsStateWithLifecycle()
    Box(
        modifier = modifier
            .fillMaxSize()
            .height(310.dp)
            .verticalScroll(rememberScrollState())
    ) {
        FlowRow(horizontalArrangement = Arrangement.Center) {
            selectedGenre.forEach { genreForUI ->
                ButtonCreator(
                    text = genreForUI.name,
                    isTouched = genreForUI.isSelected.value,
                    onClick = {
                        viewModel.viewModelScope.launch(Dispatchers.IO) {
                            genreForUI.isSelected.value = !genreForUI.isSelected.value
                        }
                        viewModel.viewModelScope.launch(Dispatchers.IO) {
                            viewModel.tappingOnGenre(genreForUI.id)
                        }
                        viewModel.viewModelScope.launch(Dispatchers.IO) {
                            viewModel.addAllParams()
                        }
                    },
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
}


@Composable
private fun ButtonCreator(
    text: String,
    onClick: () -> Unit,
    isTouched: Boolean,
    modifier: Modifier
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(
                if (isTouched) {
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
                color = if (isTouched) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = modifier.padding(8.dp),
                fontSize = 18.sp
            )
        }
    )
}

@Composable
private fun ShowRating(viewModel: HomeScreenViewModel, modifier: Modifier) {
    val ratingList by viewModel.ratingList.collectAsStateWithLifecycle()
    val selectedRating by viewModel.selectedRating.collectAsStateWithLifecycle()
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        ratingList.forEach { rating ->
            ButtonCreator(
                isTouched = rating == selectedRating,
                onClick = {
                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                        viewModel.setSelectedRating(rating)
                        viewModel.addAllParams()
                    }
                },
                text = rating.ratingName,
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
private fun ShowTypes(viewModel: HomeScreenViewModel, modifier: Modifier) {
    val typeList by viewModel.typeList.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()

    FlowRow(horizontalArrangement = Arrangement.Center, modifier = modifier.fillMaxWidth()) {
        typeList.forEach { type ->
            ButtonCreator(
                isTouched = type == selectedType,
                onClick = {
                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                        viewModel.setSelectedType(type)
                        viewModel.addAllParams()
                    }
                },
                text = type.typeName,
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
private fun ShowOrderBy(viewModel: HomeScreenViewModel, modifier: Modifier) {
    val orderByList by viewModel.orderByList.collectAsStateWithLifecycle()
    val selectedOrderBy by viewModel.selectedOrderBy.collectAsStateWithLifecycle()


    FlowRow(horizontalArrangement = Arrangement.Center, modifier = modifier.fillMaxWidth()) {
        orderByList.forEach { orderBy ->
            ButtonCreator(
                isTouched = orderBy == selectedOrderBy,
                onClick = {
                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                        viewModel.setSelectedOrderBy(orderBy)
                        viewModel.addAllParams()
                    }
                },
                text = orderBy.orderBy,
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
