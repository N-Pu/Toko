package com.project.toko.homeScreen.presentation_layer.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.project.toko.core.dao.AnimeItem
import com.project.toko.core.presentation_layer.theme.DialogColor
import com.project.toko.core.presentation_layer.theme.DialogSideColor
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.core.viewModel.daoViewModel.DaoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CustomDialog(
    onDismiss: () -> Unit,
    data: com.project.toko.homeScreen.model.newAnimeSearchModel.AnimeSearchData,
    navController: NavController,
    modifier: Modifier,
    viewModelProvider: ViewModelProvider
) {

    val painter = rememberAsyncImagePainter(model = data.images.jpg.large_image_url)
    val localDensity = LocalConfiguration.current
    val isSynopsisEmpty = data.synopsis.isNullOrEmpty()
    val isGenresEmpty = data.genres.isEmpty()
    val weight = localDensity.screenWidthDp.dp - 50.dp
//    var genreHeight by remember {
//        mutableStateOf(0.dp)
//    }
//    val synopsisHeight = remember {
//        mutableStateOf(0.dp)
//    }

    var currentHeight = 550.dp

    val height = {
//        var currentHeight = localDensity.screenHeightDp.dp - 150.dp
        if (isSynopsisEmpty) {
//            currentHeight -= synopsisHeight.value
            currentHeight -= 160.dp
        }
        if (isGenresEmpty) {
            currentHeight -= 60.dp
        }
        currentHeight
    }

    Dialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(modifier = modifier
            .width(weight)
            .height(height())) {


            Card(
                modifier = modifier
//                . width(weight)


//                .size(weight, height())

//                .offset { IntOffset(0, offsetY.roundToInt()) }
//                .draggable(
//                    orientation = Orientation.Vertical,
//                    state = rememberDraggableState { delta ->
//                        offsetY += delta
//                    },
//                )
                ,
                colors = CardDefaults.cardColors(containerColor = DialogColor)
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(15.dp, 10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = data.title,
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!data.title_english.isNullOrEmpty()) {
                        Text(
                            text = data.title_english,
                            fontSize = 10.sp,
                            color = Color.White,
                            lineHeight = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Row(
                    modifier = modifier
                        .padding(15.dp, 0.dp),
                ) {
                    Column(
                        modifier = modifier

                            .fillMaxWidth(0.55f)
                            .fillMaxHeight(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        DisplayDialogPicture(
                            painter, data.mal_id, navController, modifier = modifier
                        )
                    }
                    Column(
                        modifier = modifier
                            .fillMaxWidth(1f)
                    ) {

                        ScoreLabel(modifier = modifier)
                        ScoreNumber(modifier = modifier, score = data.score)
                        ScoreByNumber(modifier = modifier, scoreBy = data.scored_by)

                        Spacer(modifier = modifier.height(10.dp))

                        RankedLine(rank = data.rank, modifier = modifier)
                        PopularityLine(popularity = data.popularity, modifier = modifier)
                        MembersLine(members = data.members, modifier = modifier)
                        YearTypeStudio(data = data, modifier = modifier)
                        EpisodesLabel(episodes = data.episodes, modifier = modifier)
                        AddToFavoriteRow(
                            modifier = modifier, data = data, viewModelProvider = viewModelProvider
                        )
                    }

                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 5.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    StatusLine(data.status, modifier)
                    RatingLine(rating = data.rating, modifier = modifier)
                }

                CheckGenresSize(
                    numbOfGenres = data.genres.size,
                    genres = data.genres,
                    modifier = modifier
                )
                Synopsis(
                    modifier = modifier,
                    synopsis = data.synopsis,
                    isSynopsisEmpty = !isSynopsisEmpty,
//                synopsisHeight = synopsisHeight,
//                localDensity = localDensity
                )
            }
        }
    }
}

@Composable
private fun ScoreLabel(modifier: Modifier) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth(1f)

    ) {
        Card(
            modifier = modifier.size(90.dp, 25.dp),
            colors = CardDefaults.cardColors(containerColor = LightGreen),
            shape = RoundedCornerShape(5.dp)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "SCORE",
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 16.sp

                )

            }

        }
    }

}

@Composable
private fun ScoreNumber(modifier: Modifier, score: Float) {

    val scoreNumb = {
        if (score == 0.0f) {
            "N/A"
        } else {
            score.toString()
        }

    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(35.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = scoreNumb(),
            color = Color.White,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )
    }
}

@Composable
private fun RankedLine(rank: Int, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        Text(
            text = ("       Ranked "),
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraLight,
            color = Color.White
        )
        Text(
            text = "#$rank",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            color = Color.White
        )
    }
}

@Composable
private fun PopularityLine(popularity: Int, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        Text(
            text = ("       Popularity "),
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin,
            color = Color.White,
        )
        Text(
            text = "#$popularity",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.White,
        )
    }
}

@Composable
private fun MembersLine(members: Int, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {

        Text(
            text = ("       Members "),
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin,
            color = Color.White,
        )
        Text(
            text = members.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.White,
        )
    }
}

@Composable
private fun ScoreByNumber(modifier: Modifier, scoreBy: Float) {

    val scoreByNumb = {
        if (scoreBy == 0.0f) {
            "N/A"
        } else {
            scoreBy.toInt().toString()
        }

    }
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = scoreByNumb() + " users",
            color = Color.White,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun YearTypeStudio(
    data: com.project.toko.homeScreen.model.newAnimeSearchModel.AnimeSearchData?,
    modifier: Modifier
) {
    val isStudioEmpty = data?.studios.isNullOrEmpty()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier.padding(25.dp, 5.dp, 0.dp, 5.dp)
    ) {
        val yearSeasonText = buildAnnotatedString {
            if (data?.year != 0 && data?.season != null) {
                withStyle(style = SpanStyle(color = LightGreen)) {
                    append("${data.season} ${data.year}")
                }
                append(" | ")
            }
            withStyle(style = SpanStyle(color = LightGreen)) {
                append(data?.type ?: "N/A")
            }
            if (!isStudioEmpty) {
                append(" | ")
                withStyle(style = SpanStyle(color = LightGreen)) {
                    append(data?.studios?.component1()?.name ?: "N/A")
                }
            }
        }

        Text(
            text = yearSeasonText.toUpperCase(),
            modifier = Modifier.padding(0.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            fontWeight = FontWeight.Thin,
            fontSize = 12.sp,
            lineHeight = 12.sp
        )
    }
}

@Composable
private fun EpisodesLabel(episodes: Int, modifier: Modifier) {

    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start,

        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp, 0.dp, 0.dp, 0.dp)
    ) {

        Box(
            modifier = modifier
                .fillMaxWidth(1f)
                .height(35.dp)
                .clip(CardDefaults.shape)
                .background(DialogSideColor)
                .padding(10.dp, 10.dp, 0.dp, 5.dp),

            ) {
            Text(
                text = ("Episodes: $episodes"),
                fontSize = 12.sp,
                fontWeight = FontWeight.Thin,
                color = Color.White,
                textAlign = TextAlign.Start
            )
        }
    }


}

@Composable
private fun AddToFavoriteRow(
    modifier: Modifier,
    data: com.project.toko.homeScreen.model.newAnimeSearchModel.AnimeSearchData,
    viewModelProvider: ViewModelProvider
) {
    val daoViewModel = viewModelProvider[DaoViewModel::class.java]

    Spacer(modifier = modifier.height(10.dp))
    var isExpanded by remember { mutableStateOf(false) }


    if (isExpanded) {

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            },
            modifier = modifier
                .background(DialogSideColor)
                .height(78.dp)
                .clip(RoundedCornerShape(80.dp)),
            offset = DpOffset((-40).dp, (-40).dp),
            properties = PopupProperties(clippingEnabled = true)
        ) {


            DropdownMenuItem(text = {
                Text(
                    text = "Watching", fontSize = 12.sp,
                    fontWeight = FontWeight.Thin,
                )
            }, modifier = modifier.height(20.dp), onClick = {
                daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                    daoViewModel.addToCategory(
                        AnimeItem(
                            data.mal_id,
                            data.title,
                            data.score.toString(),
                            data.scored_by.toInt().toString(),
                            data.images.jpg.large_image_url,
                            "Watching"
                        )
                    )
                }
            }, colors = MenuDefaults.itemColors(
                textColor = Color.White, trailingIconColor = Color.White
            ), trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Star, contentDescription = "Button 'Rating'"
                )
            })
            DropdownMenuItem(text = {
                Text(
                    text = "Completed", fontSize = 12.sp,
                    fontWeight = FontWeight.Thin,
                )
            }, modifier = modifier.height(20.dp), onClick = {
                daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                    daoViewModel.addToCategory(
                        AnimeItem(
                            data.mal_id,
                            data.title,
                            data.score.toString(),
                            data.scored_by.toInt().toString(),
                            data.images.jpg.large_image_url,
                            "Watched"
                        )
                    )
                }
            }, colors = MenuDefaults.itemColors(
                textColor = Color.White, trailingIconColor = Color.White
            ), trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = "Button 'Completed'"
                )
            })
            DropdownMenuItem(text = {
                Text(
                    text = "Not Interesting", fontSize = 12.sp,
                    fontWeight = FontWeight.Thin,
                )
            }, modifier = modifier.height(20.dp), onClick = {
                daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                    daoViewModel.addToCategory(
                        AnimeItem(
                            data.mal_id,
                            data.title,
                            data.score.toString(),
                            data.scored_by.toInt().toString(),
                            data.images.jpg.large_image_url,
                            "Dropped"
                        )
                    )
                }
            }, colors = MenuDefaults.itemColors(
                textColor = Color.White, trailingIconColor = Color.White
            ), trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Button 'Not Interesting'",
                    modifier = modifier
                )
            })

        }


    }

    Row(
        modifier = modifier
            .fillMaxWidth(1f)
            .height(40.dp)
    ) {
        Column(modifier = modifier.width(10.dp)) {}
        Column(modifier = modifier.fillMaxWidth(0.7f)) {
            Card(colors = CardDefaults.cardColors(containerColor = DialogSideColor),
                modifier = modifier
                    .height(30.dp)
                    .clickable {
                        daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                            daoViewModel.addToCategory(
                                AnimeItem(
                                    data.mal_id,
                                    data.title,
                                    data.score.toString(),
                                    data.scored_by
                                        .toInt()
                                        .toString(),
                                    data.images.jpg.large_image_url,
                                    "Planned"
                                )
                            )
                        }
                    }) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "In the plans",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Thin,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Column(modifier = modifier.width(5.dp)) {}
        Column(
            modifier = modifier.fillMaxWidth(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = DialogSideColor, contentColor = Color.White
                ), modifier = Modifier.size(30.dp)
            ) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Add to favorite button",
                        modifier = Modifier
                            .size(15.dp)
                            .clickable {
                                isExpanded = true
                            })
                }
            }
        }
    }
}

@Composable
private fun StatusLine(status: String, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(1f)
            .padding(15.dp, 0.dp, 0.dp, 0.dp)
    ) {

        Text(
            text = ("Status: "), fontSize = 12.sp, fontWeight = FontWeight.Thin, color = Color.White
        )
        Text(
            text = status, fontWeight = FontWeight.W400, fontSize = 12.sp, color = LightGreen
        )
    }
}

@Composable
private fun RatingLine(rating: String, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(1f)
            .padding(15.dp, 0.dp, 0.dp, 0.dp)
    ) {

        Text(
            text = ("Rating: "),
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin,
            color = Color.White
        )
        Text(
            text = (rating),
            fontSize = 12.sp,
            fontWeight = FontWeight.W400,
            color = Color.White
        )
    }
}

@Composable
private fun ColoredBox(
    text: String, modifier: Modifier
) {

    Box(
        modifier = modifier
            .background(LightGreen, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(2.dp),
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun DisplayCustomGenres(
    genres: List<com.project.toko.homeScreen.model.newAnimeSearchModel.Genre>,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .horizontalScroll(
                rememberScrollState()
            )
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
        ,
        horizontalArrangement = Arrangement.Center
    ) {
        genres.forEachIndexed { index, genre ->
            if (index != 0) {
                Spacer(modifier = modifier.width(8.dp))
            }
            ColoredBox(
                text = genre.name, modifier
            )
        }
    }
}


@Composable
private fun Synopsis(
    modifier: Modifier,
    synopsis: String?,
    isSynopsisEmpty: Boolean,
//    synopsisHeight: MutableState<Dp>,
//    localDensity: Configuration
) {
    if (isSynopsisEmpty) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 15.dp)
//                .onGloballyPositioned { coordinates ->
//                    synopsisHeight.value = with(localDensity) {coordinates.size.height.dp}
//                }
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp)

            ) {
                Text(text = "Synopsis", fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
            Column(
                modifier = modifier
                    .height(160.dp)
                    .fillMaxWidth(1f)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(15.dp, 5.dp, 15.dp, 15.dp)

            ) {
                Text(
                    text = synopsis ?: "", color = Color.White, fontSize = 12.sp,
                    fontWeight = FontWeight.Thin, lineHeight = 12.sp,
                )
            }
        }
    }
}

@Composable
private fun DisplayDialogPicture(
    painter: Painter,
    mal_id: Int,
    navController: NavController,
    modifier: Modifier
) {


    Image(
        painter = painter,
        contentDescription = "Big anime picture",

        contentScale = ContentScale.FillBounds,
        modifier = modifier

            .fillMaxSize()
            .clip(CardDefaults.shape)
            .clickable {
                navigateToDetailScreen(navController, mal_id)
            },
        alignment = Alignment.Center,
    )
}

@Composable
private fun CheckGenresSize(
    numbOfGenres: Int?,
    genres: List<com.project.toko.homeScreen.model.newAnimeSearchModel.Genre>,
    modifier: Modifier
) {
    if (numbOfGenres != null) {
        if (numbOfGenres <= 3) {
            DisplayCustomGenres(
                genres = genres, modifier = modifier
            )
        } else {
            DisplayCustomGenres(
                genres = genres.take(3),
                modifier = modifier
            )
        }
    }
}