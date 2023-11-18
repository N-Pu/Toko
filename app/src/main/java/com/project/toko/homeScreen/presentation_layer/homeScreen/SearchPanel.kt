package com.project.toko.homeScreen.presentation_layer.homeScreen

import com.project.toko.homeScreen.viewModel.HomeScreenViewModel
import android.view.MotionEvent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.project.toko.R
import com.project.toko.core.presentation_layer.animations.LoadingAnimation
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.core.presentation_layer.theme.ScoreColors
import com.project.toko.core.presentation_layer.theme.SearchBarColor
import com.project.toko.core.presentation_layer.theme.iconColorInSearchPanel
import com.project.toko.homeScreen.model.linkChangerModel.Score
import com.project.toko.homeScreen.model.linkChangerModel.getStateScore
import com.project.toko.homeScreen.model.tabRow.TabItem
import kotlinx.coroutines.Dispatchers
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

    Column(
        modifier = modifier
            .fillMaxWidth(1f)
            .background(Color(0xFFF4F4F4))
    ) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                .height(140.dp)
                .shadow(20.dp)
                .fillMaxWidth(1f)
                .background(Color.White)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 0.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = R.drawable.tokominilogo),
                    contentDescription = "None",
                    modifier = modifier
                        .height(50.dp)
                        .width(70.dp),
                    alpha = 0.8f
                )
            }
            Row(
                modifier = modifier
                    .wrapContentSize()
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SearchBarColor),
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedTextField(
                    placeholder = { Text(text = "Search...") },
                    value = searchText ?: "",
                    onValueChange = viewModel::onSearchTextChange,
                    modifier = modifier
                        .clip(RoundedCornerShape(30.dp))
                        .height(50.dp)
                        .fillMaxWidth(1f),
                    prefix = {
                        Icon(Icons.Filled.Search, "Search Icon", tint = iconColorInSearchPanel)
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

        TabSelectionMenu(viewModel, modifier, isTabMenuOpen)

        if (!isSearching) {
            GridAdder(
                navController = navController,
                viewModelProvider = viewModelProvider,
                modifier = modifier,
                isTabMenuOpen = isTabMenuOpen
            )
        } else {
            LoadingAnimation()
        }

    }
}


@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun TabSelectionMenu(
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
        TabItem("Order By"),
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
//    ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
    Row(
        horizontalArrangement = Arrangement.Center, modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF4F4F4))
    ) {

        ScrollableTabRow(
//        TabRow(
            modifier = modifier
                .fillMaxWidth(0.85f),
            selectedTabIndex = selectedTabIndex,
            contentColor = Color.Black,
            containerColor = Color(0xFFF4F4F4),
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    TabRowDefaults.Indicator(
                        modifier = modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = LightGreen
                    )
                }
            },
            edgePadding = 0.dp
        ) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selected = index == selectedTabIndex, onClick = {
                        selectedTabIndex = index
                    },
                    modifier = modifier.background(Color(0xFFF4F4F4))
                ) {
                    Text(text = item.title, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = modifier.height(5.dp))
                }
            }
        }
    }
    Spacer(
        modifier = modifier
            .height(20.dp)
            .fillMaxWidth()
            .background(Color(0xFFF4F4F4))
    )
    if (isTabMenuOpen.value) {
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
                    .background(Color(0xFFF4F4F4))
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
//                    SafeFowWorkSwitch(viewModel)

                }
            )
        }
    }
}

@Composable
private fun SafeFowWorkSwitch(viewModel: HomeScreenViewModel) {

    var sfw by viewModel.safeForWork
    Box(contentAlignment = Alignment.Center) {
        Divider(thickness = 25.dp)
        Text(text = "SFW content", textAlign = TextAlign.Center)
    }
    Box {
        Switch(checked = sfw, onCheckedChange = {
            sfw = it
        })
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ScoreBar(
    viewModel: HomeScreenViewModel,
    modifier: Modifier
) {
    var ratingState by viewModel.scoreState
    var selected by viewModel.selectedMinMax
    val size by animateDpAsState(
        targetValue = if (selected) 30.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy), label = ""
    )
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..10) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        viewModel.viewModelScope.launch {
                            when (it.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    if (i == ratingState) {
                                        // Повторное нажатие на звезду
                                        selected = false
                                        ratingState = 0

                                        viewModel.setSelectedMinScore(
                                            Score(
                                                getStateScore(
                                                    ratingState
                                                ).minScore
                                            )
                                        )
                                        viewModel.setSelectedMaxScore(
                                            Score(
                                                getStateScore(
                                                    ratingState
                                                ).maxScore
                                            )
                                        )
                                    } else {
                                        selected = true
                                        ratingState = i
                                        viewModel.setSelectedMinScore(
                                            Score(
                                                getStateScore(
                                                    ratingState
                                                ).minScore
                                            )
                                        )
                                        viewModel.setSelectedMaxScore(
                                            Score(
                                                getStateScore(
                                                    ratingState
                                                ).maxScore
                                            )
                                        )
                                    }
                                }

                                MotionEvent.ACTION_UP -> {
                                    selected = false
                                }
                            }
                            viewModel.addAllParams()
                        }
                        true
                    },
                tint = starColorChanger(ratingState, i)
            )
        }
    }
}


private fun starColorChanger(selectedScore: Int, starNumber: Int): Color {
    return when (selectedScore) {
        in 1..3 -> {
            if (starNumber <= selectedScore) {
                ScoreColors.Red
            } else {
                ScoreColors.Blank
            }
        }

        in 4..6 -> {
            if (starNumber <= selectedScore) {
                ScoreColors.Yellow
            } else {
                ScoreColors.Blank
            }
        }

        in 7..10 -> {
            if (starNumber <= selectedScore) {
                ScoreColors.Green
            } else {
                ScoreColors.Blank
            }
        }

        else -> ScoreColors.Blank
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
                            viewModel.tappingOnGenre(genreForUI.id)
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
                    Color(104, 190, 174)
                } else {
                    Color(222, 222, 222)
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
        content = {
            Text(
                text = text,
                color = if (isTouched) Color.White else Color.Black,
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
