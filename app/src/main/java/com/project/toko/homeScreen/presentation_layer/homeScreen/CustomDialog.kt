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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.ColorFilter
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.project.toko.R
import com.project.toko.core.presentation_layer.theme.evolventaBoldFamily
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.daoScreen.dao.FavoriteItem
import com.project.toko.daoScreen.daoViewModel.DaoViewModel
import com.project.toko.daoScreen.model.AnimeStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun CustomDialog(
    onDismiss: () -> Unit,
    data: com.project.toko.homeScreen.model.newAnimeSearchModel.AnimeSearchData,
    navController: NavController,
    modifier: Modifier,
    viewModelProvider: ViewModelProvider,
    isInDarkTheme: Boolean,
    svgImageLoader: ImageLoader
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
        }, properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .width(weight)
                .height(height())
        ) {
            Card(
                modifier = modifier,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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

                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = evolventaBoldFamily
                    )
                    if (!data.title_english.isNullOrEmpty()) {
                        Text(
                            text = data.title_english,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            lineHeight = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Row(
                    modifier = modifier.padding(15.dp, 0.dp),
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
                        modifier = modifier.fillMaxWidth(1f)
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
                        AddToDataBaseRow(
                            modifier = modifier,
                            data = data,
                            viewModelProvider = viewModelProvider,
                            isInDarkTheme = isInDarkTheme,
                            svgImageLoader = svgImageLoader
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
                    RatingLine(rating = data.rating ?: "N/A", modifier = modifier)
                }

                CheckGenresSize(
                    numbOfGenres = data.genres.size, genres = data.genres, modifier = modifier
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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
            shape = RoundedCornerShape(5.dp)

        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "SCORE",
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = evolventaBoldFamily

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
            .height(35.dp), horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = scoreNumb(),
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontFamily = evolventaBoldFamily
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
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = "#$rank",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = evolventaBoldFamily
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
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = "#$popularity",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = evolventaBoldFamily
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
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = members.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = evolventaBoldFamily
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
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun YearTypeStudio(
    data: com.project.toko.homeScreen.model.newAnimeSearchModel.AnimeSearchData?, modifier: Modifier
) {
    val isStudioEmpty = data?.studios.isNullOrEmpty()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier.padding(25.dp, 5.dp, 0.dp, 5.dp)
    ) {
        val yearSeasonText = buildAnnotatedString {
            if (data?.year != 0 && data?.season != null) {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    append("${data.season} ${data.year}")
                }
                append(" | ")
            }
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.secondary
                )
            ) {
                append(data?.type ?: "N/A")
            }
            if (!isStudioEmpty) {
                append(" | ")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.secondary
                    )
                ) {
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
            lineHeight = 12.sp,
            color = MaterialTheme.colorScheme.inversePrimary
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
                .background(MaterialTheme.colorScheme.onError)
                .padding(10.dp, 10.dp, 0.dp, 5.dp),

            ) {
            Text(
                text = ("Episodes: $episodes"),
                fontSize = 12.sp,
                fontWeight = FontWeight.Thin,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start
            )
        }
    }


}

@Composable
private fun AddToDataBaseRow(
    modifier: Modifier,
    data: com.project.toko.homeScreen.model.newAnimeSearchModel.AnimeSearchData,
    viewModelProvider: ViewModelProvider, isInDarkTheme: Boolean,
    svgImageLoader: ImageLoader
) {
    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    var isExpanded by remember { mutableStateOf(false) }

    val threeDots = if (isInDarkTheme) {
        R.drawable.three_dots_white
    } else {
        R.drawable.three_dots_gray
    }
    Spacer(modifier = modifier.height(10.dp))
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = {
            isExpanded = !isExpanded
        },
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .height(120.dp)
            .width(170.dp),
        offset = DpOffset((-40).dp, (-40).dp),
        properties = PopupProperties(clippingEnabled = true)
    ) {
        DropdownMenuItem(text = {
            Text(
                text = "Watching",
                fontWeight = FontWeight.Light,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = if (daoViewModel.containsItemIdInCategory(
                        id = data.mal_id,
                        AnimeStatus.WATCHING.route
                    ).collectAsStateWithLifecycle(initialValue = false).value
                ) Color.Yellow else MaterialTheme.colorScheme.primary,
            )
        }, modifier = modifier.weight(1f), onClick = {
            daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                if (daoViewModel.containsItemIdInCategory(
                        data.mal_id,
                        AnimeStatus.WATCHING.route
                    ).first()
                ) {
                    daoViewModel.removeFromDataBase(
                        AnimeItem(
                            data.mal_id,
                            data.title,
                            data.score.toString(),
                            data.scored_by.toInt().toString(),
                            data.images.jpg.large_image_url,
                            data.status,
                            data.rating ?: "N/A",
                            data.title_japanese,
                            airedFrom = data.aired.from,
                            category = AnimeStatus.WATCHING.route,
                            type = data.type
                        )
                    )
                } else {
                    daoViewModel.addToCategory(
                        AnimeItem(
                            data.mal_id,
                            data.title,
                            data.score.toString(),
                            data.scored_by.toInt().toString(),
                            data.images.jpg.large_image_url,
                            data.status,
                            data.rating ?: "N/A",
                            data.title_japanese,
                            airedFrom = data.aired.from,
                            category = AnimeStatus.WATCHING.route,
                            type = data.type
                        )
                    )
                }
            }
        }, colors = MenuDefaults.itemColors(
            textColor = MaterialTheme.colorScheme.onPrimary,
            trailingIconColor = MaterialTheme.colorScheme.onPrimary
        ), trailingIcon = {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.drawable.add, imageLoader = svgImageLoader
                ), contentDescription = null, modifier = modifier.size(25.dp),
                colorFilter =
                if (daoViewModel.containsItemIdInCategory(
                        id = data.mal_id,
                        AnimeStatus.WATCHING.route
                    ).collectAsStateWithLifecycle(initialValue = false).value
                ) ColorFilter.tint(Color.Yellow) else ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        })
        DropdownMenuItem(text = {
            Text(
                text = "Completed",
                fontWeight = FontWeight.Light,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = if (daoViewModel.containsItemIdInCategory(
                        id = data.mal_id,
                        AnimeStatus.COMPLETED.route
                    ).collectAsStateWithLifecycle(initialValue = false).value
                ) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
            )
        }, modifier = modifier.weight(1f), onClick = {
            daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                if (daoViewModel.containsItemIdInCategory(
                        data.mal_id,
                        AnimeStatus.COMPLETED.route
                    ).first()
                ) {
                    daoViewModel.removeFromDataBase(
                        AnimeItem(
                            data.mal_id,
                            data.title,
                            data.score.toString(),
                            data.scored_by.toInt().toString(),
                            data.images.jpg.large_image_url,
                            data.status,
                            data.rating ?: "N/A",
                            data.title_japanese,
                            airedFrom = data.aired.from,
                            category = AnimeStatus.COMPLETED.route,
                            type = data.type
                        )
                    )
                } else {
                    daoViewModel.addToCategory(
                        AnimeItem(
                            data.mal_id,
                            data.title,
                            data.score.toString(),
                            data.scored_by.toInt().toString(),
                            data.images.jpg.large_image_url,
                            data.status,
                            data.rating ?: "N/A",
                            data.title_japanese,
                            airedFrom = data.aired.from,
                            category = AnimeStatus.COMPLETED.route,
                            type = data.type
                        )
                    )
                }
            }
        }, colors = MenuDefaults.itemColors(
            textColor = MaterialTheme.colorScheme.onPrimary,
            trailingIconColor = MaterialTheme.colorScheme.onPrimary
        ), trailingIcon = {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.drawable.eyewhite, imageLoader = svgImageLoader
                ), contentDescription = null, modifier = modifier.size(22.dp),
                colorFilter =
                if (daoViewModel.containsItemIdInCategory(
                        id = data.mal_id,
                        AnimeStatus.COMPLETED.route
                    ).collectAsStateWithLifecycle(initialValue = false).value
                ) ColorFilter.tint(MaterialTheme.colorScheme.secondary) else ColorFilter.tint(
                    MaterialTheme.colorScheme.primary
                )
            )
        })
        DropdownMenuItem(text = {
            Text(
                text = "Dropped",
                fontWeight = FontWeight.Light,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = if (daoViewModel.containsItemIdInCategory(
                        id = data.mal_id,
                        AnimeStatus.DROPPED.route
                    ).collectAsStateWithLifecycle(initialValue = false).value
                ) Color.Red else MaterialTheme.colorScheme.primary,
            )
        }, modifier = modifier.weight(1f), onClick = {
            daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                if (daoViewModel.containsItemIdInCategory(
                        data.mal_id,
                        AnimeStatus.DROPPED.route
                    ).first()
                ) {
                    daoViewModel.removeFromDataBase(
                        AnimeItem(
                            data.mal_id,
                            data.title,
                            data.score.toString(),
                            data.scored_by.toInt().toString(),
                            data.images.jpg.large_image_url,
                            data.status,
                            data.rating ?: "N/A",
                            data.title_japanese,
                            airedFrom = data.aired.from,
                            category = AnimeStatus.DROPPED.route,
                            type = data.type
                        )
                    )
                } else {
                    daoViewModel.addToCategory(
                        AnimeItem(
                            data.mal_id,
                            data.title,
                            data.score.toString(),
                            data.scored_by.toInt().toString(),
                            data.images.jpg.large_image_url,
                            data.status,
                            data.rating ?: "N/A",
                            data.title_japanese,
                            airedFrom = data.aired.from,
                            category = AnimeStatus.DROPPED.route,
                            type = data.type
                        )
                    )
                }
            }
        }, colors = MenuDefaults.itemColors(
            textColor = MaterialTheme.colorScheme.onPrimary,
            trailingIconColor = MaterialTheme.colorScheme.onPrimary
        ), trailingIcon = {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.drawable.dropped, imageLoader = svgImageLoader
                ), contentDescription = null, modifier = modifier.size(25.dp),
                colorFilter =
                if (daoViewModel.containsItemIdInCategory(
                        id = data.mal_id,
                        AnimeStatus.DROPPED.route
                    ).collectAsStateWithLifecycle(initialValue = false).value
                ) ColorFilter.tint(Color.Red) else ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        })
        DropdownMenuItem(text = {
            Text(
                text = "Favorite",
                fontWeight = FontWeight.Light,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = if (daoViewModel.containsInFavorite(
                        id = data.mal_id
                    ).collectAsStateWithLifecycle(initialValue = false).value
                ) Color.Red else MaterialTheme.colorScheme.primary,
            )
        }, modifier = modifier.weight(1f), onClick = {
            daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                if (daoViewModel.containsInFavorite(
                        data.mal_id
                    ).first()
                ) {
                    daoViewModel.removeFromFavorite(
                        FavoriteItem(
                            data.mal_id,
                            data.title,
                            data.score.toString(),
                            data.scored_by.toInt().toString(),
                            data.images.jpg.large_image_url,
                            data.status,
                            data.rating ?: "N/A",
                            data.title_japanese,
                            airedFrom = data.aired.from,
                            category = AnimeStatus.DROPPED.route,
                            type = data.type
                        )
                    )
                } else {
                    daoViewModel.addToFavorite(
                        FavoriteItem(
                            data.mal_id,
                            data.title,
                            data.score.toString(),
                            data.scored_by.toInt().toString(),
                            data.images.jpg.large_image_url,
                            data.status,
                            data.rating ?: "N/A",
                            data.title_japanese,
                            airedFrom = data.aired.from,
                            category = AnimeStatus.DROPPED.route,
                            type = data.type
                        )
                    )
                }
            }
        }, colors = MenuDefaults.itemColors(
            textColor = MaterialTheme.colorScheme.onPrimary,
            trailingIconColor = MaterialTheme.colorScheme.onPrimary
        ), trailingIcon = {
            Image(
                modifier = modifier.size(25.dp),
                painter = rememberAsyncImagePainter(
                    model = if (daoViewModel.containsInFavorite(
                            id = data.mal_id
                        ).collectAsStateWithLifecycle(initialValue = false).value
                    ) R.drawable.favorite_touched else
                        R.drawable.favorite_untouched, imageLoader = svgImageLoader
                ),
                contentDescription = null,
                colorFilter = if (daoViewModel.containsInFavorite(
                        id = data.mal_id
                    ).collectAsStateWithLifecycle(initialValue = false).value
                ) null else ColorFilter.tint(
                    MaterialTheme.colorScheme.primary
                )
            )
        })

    }


    Row(
        modifier = modifier
            .fillMaxWidth(1f)
            .height(40.dp), horizontalArrangement = Arrangement.SpaceAround
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .weight(2.5f)
                .fillMaxWidth().fillMaxHeight().padding(horizontal = 5.dp)
                .clip(CardDefaults.shape)
                .background(
                    if (daoViewModel
                            .containsItemIdInCategory(
                                id = data.mal_id,
                                AnimeStatus.PLANNED.route
                            )
                            .collectAsStateWithLifecycle(initialValue = false).value
                    )
                        Color(
                            255,
                            152,
                            0,
                            255
                        )
                    else MaterialTheme.colorScheme.onError
                )
                .clickable {
                    daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                        if (daoViewModel
                                .containsItemIdInCategory(
                                    data.mal_id,
                                    AnimeStatus.PLANNED.route
                                )
                                .first()
                        ) {
                            daoViewModel.removeFromDataBase(
                                AnimeItem(
                                    data.mal_id,
                                    data.title,
                                    data.score.toString(),
                                    data.scored_by
                                        .toInt()
                                        .toString(),
                                    data.images.jpg.large_image_url,
                                    data.status,
                                    data.rating ?: "N/A",
                                    data.title_japanese,
                                    airedFrom = data.aired.from,
                                    category = AnimeStatus.PLANNED.route,
                                    type = data.type
                                )
                            )
                        } else {
                            daoViewModel.addToCategory(
                                AnimeItem(
                                    data.mal_id,
                                    data.title,
                                    data.score.toString(),
                                    data.scored_by
                                        .toInt()
                                        .toString(),
                                    data.images.jpg.large_image_url,
                                    data.status,
                                    data.rating ?: "N/A",
                                    data.title_japanese,
                                    airedFrom = data.aired.from,
                                    category = AnimeStatus.PLANNED.route,
                                    type = data.type
                                )
                            )
                        }
                    }
                }
        ) {

            Text(
                text = "In the plans",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp,
            )
        }

        Column(
            modifier = modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                daoViewModel.containsItemIdInCategory(
                    id = data.mal_id,
                    AnimeStatus.WATCHING.route
                ).collectAsStateWithLifecycle(initialValue = false).value -> {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.add, imageLoader = svgImageLoader
                        ),
                        contentDescription = null,
                        modifier = modifier
                            .clickable {
                                isExpanded = !isExpanded
                            },
                        colorFilter = ColorFilter.tint(Color.Yellow)
                    )

                }

                daoViewModel.containsItemIdInCategory(
                    id = data.mal_id,
                    AnimeStatus.COMPLETED.route
                ).collectAsStateWithLifecycle(initialValue = false).value -> {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.eyewhite, imageLoader = svgImageLoader
                        ),
                        contentDescription = null,
                        modifier = modifier
                            .clickable {
                                isExpanded = !isExpanded
                            }
                            .fillMaxSize(),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary))

                }

                daoViewModel.containsItemIdInCategory(
                    id = data.mal_id,
                    AnimeStatus.DROPPED.route
                ).collectAsStateWithLifecycle(initialValue = false).value -> {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.dropped, imageLoader = svgImageLoader
                        ),
                        contentDescription = null,
                        modifier = modifier
                            .clickable {
                                isExpanded = !isExpanded
                            },
                        colorFilter = ColorFilter.tint(Color.Red)
                    )

                }

                else -> {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = threeDots, imageLoader = svgImageLoader
                        ),
                        contentDescription = null,
                        modifier = modifier
                            .clickable {
                                isExpanded = !isExpanded
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
            text = ("Status: "),
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = status,
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.secondary
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
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = (rating),
            fontSize = 12.sp,
            fontWeight = FontWeight.W400,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun ColoredBox(
    text: String, modifier: Modifier
) {

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(2.dp),
            fontWeight = FontWeight.ExtraBold,
            fontFamily = evolventaBoldFamily
        )
    }
}

@Composable
private fun DisplayCustomGenres(
    genres: List<com.project.toko.homeScreen.model.newAnimeSearchModel.Genre>, modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .horizontalScroll(
                rememberScrollState()
            )
            .padding(0.dp, 10.dp, 0.dp, 0.dp), horizontalArrangement = Arrangement.Center
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
                Text(
                    text = "Synopsis",
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = evolventaBoldFamily
                )
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
                    text = synopsis ?: "",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Thin,
                    lineHeight = 12.sp,
                )
            }
        }
    }
}

@Composable
private fun DisplayDialogPicture(
    painter: Painter, mal_id: Int, navController: NavController, modifier: Modifier
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
                genres = genres.take(3), modifier = modifier
            )
        }
    }
}