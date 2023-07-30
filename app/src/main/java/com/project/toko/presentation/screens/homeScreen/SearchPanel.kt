package com.project.toko.presentation.screens.homeScreen

import com.project.toko.domain.viewModel.HomeScreenViewModel
import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.project.toko.dao.Dao
import com.project.toko.domain.models.linkChangeModel.Score
import com.project.toko.domain.models.linkChangeModel.getStateScore
import com.project.toko.presentation.animations.LoadingAnimation
import com.project.toko.presentation.theme.LightGreen
import com.project.toko.presentation.theme.SoftGreen
import com.project.toko.presentation.theme.ScoreColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    navController: NavHostController, viewModelProvider: ViewModelProvider,
    modifier: Modifier, dao: Dao
) {
    val viewModel = viewModelProvider[HomeScreenViewModel::class.java]
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val isSearching by viewModel.isPerformingSearch.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxWidth(1f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = modifier
                .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                .shadow(20.dp)
                .fillMaxWidth(1f)
                .background(
                    LightGreen
                )
                .padding(start = 20.dp, end = 20.dp, top = 35.dp, bottom = 15.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = viewModel::onSearchTextChange,
                modifier = modifier.fillMaxWidth(1f),
                prefix = {
                    Icon(Icons.Filled.Search, "Search Icon")
                },
                singleLine = true,
                trailingIcon = {
                    DropDownMenuWithIconButton(viewModel, modifier)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = LightGreen,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedTrailingIconColor = LightGreen,
                    focusedPlaceholderColor = LightGreen,
                    focusedPrefixColor = LightGreen,
                )
            )
        }

        if (!isSearching) {
            GridAdder(
                navController = navController,
                viewModel = viewModel,
                viewModelProvider = viewModelProvider,
                modifier = modifier,
                dao = dao
            )
        } else {
            LoadingAnimation()
        }

    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DropDownMenuWithIconButton(viewModel: HomeScreenViewModel, modifier: Modifier) {
    val isDropdownVisible = viewModel.isDropdownMenuVisible

    IconButton(onClick = {

        viewModel.viewModelScope.launch(Dispatchers.IO) {
            isDropdownVisible.value = true
        }

    }, modifier = modifier.size(65.dp)) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "GenreButton",
            modifier = modifier.size(30.dp)
        )
    }

    DropdownMenu(
        expanded = isDropdownVisible.value,
        onDismissRequest = { isDropdownVisible.value = false },
        offset = DpOffset(x = (-270).dp, y = 0.dp)
    ) {
        Box(modifier.size(360.dp)) {

            FlowRow(
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                content = {

                    ShowGenres(viewModel, modifier)
                    ShowRating(viewModel, modifier)
                    ShowTypes(viewModel, modifier)
                    ShowOrderBy(viewModel, modifier)
                    ScoreBar(viewModel = viewModel, modifier = modifier)
                    SafeFowWorkSwitch(viewModel)

                })
        }
        AddAllButton(viewModel, isDropdownVisible, modifier = modifier)
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
    modifier: Modifier,
    viewModel: HomeScreenViewModel
) {
    var ratingState by viewModel.scoreState
    var selected by viewModel.selectedMinMax
    val size by animateDpAsState(
        targetValue = if (selected) 30.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy), label = ""
    )



    Box(contentAlignment = Alignment.Center) {
        Divider(thickness = 25.dp)
        Text(text = "min - max score", textAlign = TextAlign.Center)
    }

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
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                if (i == ratingState) {
                                    // Повторное нажатие на звезду
                                    selected = false
                                    ratingState = 0

                                    viewModel.setSelectedMinScore(Score(getStateScore(ratingState).minScore))
                                    viewModel.setSelectedMaxScore(Score(getStateScore(ratingState).maxScore))
                                } else {
                                    selected = true
                                    ratingState = i
                                    viewModel.setSelectedMinScore(Score(getStateScore(ratingState).minScore))
                                    viewModel.setSelectedMaxScore(Score(getStateScore(ratingState).maxScore))
                                }
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
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

@Composable
private fun ShowGenres(
    viewModel: HomeScreenViewModel,
    modifier: Modifier
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
                    LightGreen
                } else {
                    SoftGreen
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
        content = {
            Text(
                text = text,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = modifier.padding(8.dp)
            )
        }
    )
}

@Composable
private fun ShowRating(viewModel: HomeScreenViewModel, modifier: Modifier) {
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
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModel.setSelectedRating(rating)
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

@Composable
private fun ShowTypes(viewModel: HomeScreenViewModel, modifier: Modifier) {
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
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModel.setSelectedType(type)
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

@Composable
private fun ShowOrderBy(viewModel: HomeScreenViewModel, modifier: Modifier) {
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
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModel.setSelectedOrderBy(orderBy)
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

@Composable
private fun AddAllButton(
    viewModel: HomeScreenViewModel,
    isDropdownVisible: MutableState<Boolean>,
    modifier: Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Button(
            onClick = {
                viewModel.viewModelScope.launch(Dispatchers.IO) {

                    viewModel.addAllParams()
                    isDropdownVisible.value = false

                }
            },
            modifier = modifier.align(Alignment.BottomCenter)

        ) {
            Text(text = "Ok", color = Color.Red)
        }
    }
}