package com.example.animeapp.presentation.screens.homeScreen

import HomeScreenViewModel
import android.util.Log
import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.animeapp.domain.models.linkChangeModel.Score
import com.example.animeapp.domain.models.linkChangeModel.getStateScore
import com.example.animeapp.presentation.animations.LoadingAnimation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    navController: NavHostController, viewModelProvider: ViewModelProvider
) {
    val viewModel = viewModelProvider[HomeScreenViewModel::class.java]
    val searchText = viewModel.searchText.collectAsStateWithLifecycle()
    val isSearching = viewModel.isPerformingSearch.collectAsStateWithLifecycle()

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchText.value,
                onValueChange = viewModel::onSearchTextChange,
                modifier = Modifier.weight(1f), // Изменено на использование weight для занимания доступного пространства
                label = {
                    Icon(Icons.Filled.Search, "Search Icon")
                },
                shape = RoundedCornerShape(36.dp),
                singleLine = true,
                placeholder = {
                    Text(text = "Searching anime...")
                },
                trailingIcon = {
                    DropDownMenuWithIconButton(viewModel)
                }
            )
        }

        if (!isSearching.value) {
            GridAdder(
                navController = navController,
                viewModel = viewModel,
                viewModelProvider = viewModelProvider
            )
        } else {
            LoadingAnimation()
        }

    }
}


// Need to fix it later
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DropDownMenuWithIconButton(viewModel: HomeScreenViewModel) {
    val isDropdownVisible = remember { mutableStateOf(false) }
    IconButton(onClick = {

        viewModel.viewModelScope.launch(Dispatchers.IO) {
            isDropdownVisible.value = true
        }

    }, modifier = Modifier.size(65.dp)) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "GenreButton",
            modifier = Modifier.size(30.dp)
        )
    }

    DropdownMenu(
        expanded = isDropdownVisible.value,
        onDismissRequest = { isDropdownVisible.value = false },
    ) {
        Box(Modifier.size(400.dp)) {

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                content = {

                    ShowGenres(viewModel)
                    ShowRating(viewModel)
                    ShowTypes(viewModel)
                    ShowOrderBy(viewModel)
//                    ScrollMe()
                    ScoreBar(viewModel = viewModel)
                    SafeFowWorkSwitch(viewModel)

                })
        }
        AddAllButton(viewModel, isDropdownVisible)
    }

}

@Composable
fun SafeFowWorkSwitch(viewModel: HomeScreenViewModel) {

//    var sfw1  by remember {
//        mutableStateOf(false)
//    }
    var sfw by viewModel.safeForWork
    Box(contentAlignment = Alignment.Center) {
        Divider(thickness = 25.dp)
        Text(text = "SFW content", textAlign = TextAlign.Center)
    }
    Box {
        Switch(checked = sfw, onCheckedChange = {
            sfw = it
//            sfw.value = it
        })
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScoreBar(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel
) {
    var ratingState by viewModel.scoreState
    var selected by viewModel.selectedMinMax
    val size by animateDpAsState(
        targetValue = if (selected) 30.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Box(contentAlignment = Alignment.Center) {
        Divider(thickness = 25.dp)
        Text(text = "min - max score", textAlign = TextAlign.Center)
    }

    Row(
        modifier = Modifier.fillMaxSize(),
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
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                if (i == ratingState) {
                                    // Повторное нажатие на звезду
                                    selected = false
                                    ratingState = 0

                                    viewModel.setSelectedMinScore(Score(getStateScore(ratingState).minScore))
                                    viewModel.setSelectedMaxScore(Score(getStateScore(ratingState).maxScore))
//                                    viewModel.min_score = getStateScore(ratingState).minScore
//                                    viewModel.max_score = getStateScore(ratingState).maxScore

                                    Log.d(
                                        "SCORES",
                                        "MIN->" + getStateScore(ratingState).minScore + " MAX->" + getStateScore(
                                            ratingState
                                        ).maxScore
                                    )
                                } else {
                                    selected = true
                                    ratingState = i

//                                    viewModel.min_score = getStateScore(ratingState).minScore
//                                    viewModel.max_score = getStateScore(ratingState).maxScore
                                    viewModel.setSelectedMinScore(Score(getStateScore(ratingState).minScore))
                                    viewModel.setSelectedMaxScore(Score(getStateScore(ratingState).maxScore))
                                    Log.d(
                                        "SCORES",
                                        "MIN->" + getStateScore(ratingState).minScore + " MAX->" + getStateScore(
                                            ratingState
                                        ).maxScore
                                    )
                                }
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}


@Composable
fun ShowGenres(
    viewModel: HomeScreenViewModel
) {
    val selectedGenre by viewModel.selectedGenre.collectAsStateWithLifecycle()

    Box(contentAlignment = Alignment.TopCenter) {
        Divider(thickness = 25.dp)
        Text(text = "Genres", textAlign = TextAlign.Center)

    }

    selectedGenre.forEach { genreForUI ->
        ButtonCreator(
            text = genreForUI.name,
            isTouched = genreForUI.isSelected.value,
            onClick = {
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    genreForUI.isSelected.value = !genreForUI.isSelected.value
                    viewModel.tappingOnGenre(genreForUI.id)
                }
            }
        )
        Spacer(
            modifier = Modifier
                .width(8.dp)
                .height(50.dp)
        )
    }
}


@Composable
fun ButtonCreator(
    text: String,
    onClick: () -> Unit,
    isTouched: Boolean,
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(
                if (isTouched) {
                    Color.Red
                } else {
                    Color.Cyan
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
        content = {
            Text(
                text = text,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    )
}

@Composable
fun ShowRating(viewModel: HomeScreenViewModel) {
    val ratingList by viewModel.ratingList.collectAsStateWithLifecycle()
    val selectedRating by viewModel.selectedRating.collectAsStateWithLifecycle()

    Box(contentAlignment = Alignment.Center) {
        Divider(thickness = 25.dp)
        Text(text = "Rating", textAlign = TextAlign.Center)
    }

    ratingList.forEach { rating ->
        ButtonCreator(
            isTouched = rating == selectedRating,
            onClick = {
                viewModel.setSelectedRating(rating)
            },
            text = rating.ratingName
        )
        Spacer(
            modifier = Modifier
                .width(8.dp)
                .height(50.dp)
        )
    }
}

@Composable
fun ShowTypes(viewModel: HomeScreenViewModel) {
    val typeList by viewModel.typeList.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()

    Box(contentAlignment = Alignment.Center) {
        Divider(thickness = 25.dp)
        Text(text = "Type", textAlign = TextAlign.Center)
    }

    typeList.forEach { type ->
        ButtonCreator(
            isTouched = type == selectedType,
            onClick = {
                viewModel.setSelectedType(type)
            },
            text = type.typeName
        )
        Spacer(
            modifier = Modifier
                .width(8.dp)
                .height(50.dp)
        )
    }
}

@Composable
fun ShowOrderBy(viewModel: HomeScreenViewModel) {
    val orderByList by viewModel.orderByList.collectAsStateWithLifecycle()
    val selectedOrderBy by viewModel.selectedOrderBy.collectAsStateWithLifecycle()

    Box(contentAlignment = Alignment.Center) {
        Divider(thickness = 25.dp)
        Text(text = "Order by", textAlign = TextAlign.Center)
    }

    orderByList.forEach { orderBy ->
        ButtonCreator(
            isTouched = orderBy == selectedOrderBy,
            onClick = {
                viewModel.setSelectedOrderBy(orderBy)
            },
            text = orderBy.orderBy
        )
        Spacer(
            modifier = Modifier
                .width(8.dp)
                .height(50.dp)
        )
    }
}

@Composable
fun AddAllButton(
    viewModel: HomeScreenViewModel,
    isDropdownVisible: MutableState<Boolean>
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = {
                viewModel.viewModelScope.launch(Dispatchers.IO) {

                    viewModel.addAllParams()
                    isDropdownVisible.value = false

                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)

        ) {
            Text(text = "Ok", color = Color.Red)
        }
    }
}