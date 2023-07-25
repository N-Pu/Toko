package com.example.animeapp.presentation.screens.homeScreen

import HomeScreenViewModel
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
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.animeapp.dao.AnimeItem
import com.example.animeapp.dao.Dao
import com.example.animeapp.domain.models.newAnimeSearchModel.Data
import com.example.animeapp.domain.models.newAnimeSearchModel.Genre
import com.example.animeapp.presentation.theme.LightGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CustomDialog(
    onDismiss: () -> Unit, data: Data, navController: NavController,
    modifier: Modifier, dao: Dao, viewModel: HomeScreenViewModel
) {


    val model = ImageRequest.Builder(LocalContext.current).data(data.images.jpg.large_image_url)
        .size(Size.ORIGINAL).crossfade(true).build()

    val weight = 500.dp

    val height = {
        var currentHeight = 550
        if (data.synopsis.isNullOrEmpty()) {
            currentHeight -= 150
        }
        if (data.genres.isNullOrEmpty()) {
            currentHeight -= 60
        }
        currentHeight.dp
    }

    Dialog(onDismissRequest = {
        onDismiss.invoke()
    }) {
        Card(
            modifier = modifier.size(weight, height()),
            colors = CardDefaults.cardColors(containerColor = Color(129, 129, 129, 190))
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(15.dp, 10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = data.title,
                    fontSize = 20.sp,
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
//                    .wrapContentSize()
                    .padding(15.dp, 0.dp)
                ,
                horizontalArrangement = Arrangement.Start
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth(0.55f)
                        .fillMaxHeight(0.5f)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                        DisplayDialogPicture(
                            model,  data.mal_id, navController, modifier = modifier
                        )


                }
                Column(modifier = modifier
                    .fillMaxWidth(1f)
                ) {

                    ScoreLabel(modifier = modifier)
                    ScoreNumber(modifier = modifier, score = data.score)
                    ScoreByNumber(modifier = modifier, score_by = data.scored_by)

                    Spacer(modifier = modifier.height(10.dp))

                    RankedLine(rank = data.rank, modifier = modifier)
                    PopularityLine(popularity = data.popularity, modifier = modifier)
                    MembersLine(members = data.members, modifier = modifier)
                    YearTypeStudio(data = data, modifier = modifier)
                    EpisodesLabel(episodes = data.episodes, modifier = modifier)
                    AddToFavoriteRow(
                        dao = dao, modifier = modifier, data = data, viewModel = viewModel
                    )
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(15.dp, 0.dp, 0.dp, 0.dp),
                horizontalAlignment = Alignment.Start
            ) {
                StatusLine(data.status, modifier)
                RatingLine(rating = data.rating, modifier = modifier)
//                DisplayCustomGenres(genres = data.genres, modifier = modifier)
                CheckGenresSize(
                    numbOfGenres = data.genres.size,
                    genres = data.genres,
                    modifier = modifier
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(150.dp)
            ) {
                Synopsis(modifier = modifier, synopsis = data.synopsis)
            }

        }
    }
}

@Composable
fun ScoreLabel(modifier: Modifier) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth(1f)

    ) {
        Card(
            modifier = modifier.size(80.dp, 25.dp),
            colors = CardDefaults.cardColors(containerColor = LightGreen),
            shape = RoundedCornerShape(5.dp)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "SCORE",
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

            }

        }
    }

}


@Composable
fun RankedLine(rank: Int, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        Text(
            text = ("       Ranked #"),
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin,
            color = Color.White
        )
        Text(
            text = rank.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.White
        )
    }
}

@Composable
fun PopularityLine(popularity: Int, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        Text(
            text = ("       Popularity #"),
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin,
            color = Color.White
        )
        Text(
            text = popularity.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.White
        )
    }
}

@Composable
fun MembersLine(members: Int, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {

        Text(
            text = ("       Members "),
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin,
            color = Color.White
        )
        Text(
            text = members.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.White
        )
    }
}

@Composable
fun ScoreNumber(modifier: Modifier, score: Float) {

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
            .height(40.dp), horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = scoreNumb(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )
    }
}

@Composable
fun ScoreByNumber(modifier: Modifier, score_by: Float) {

    val scoreByNumb = {
        if (score_by == 0.0f) {
            "N/A"
        } else {
            score_by.toInt().toString()
        }

    }
    Row(
        modifier = modifier.fillMaxWidth()
//            .height(10.dp)
        , horizontalArrangement = Arrangement.Center
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
fun YearTypeStudio(data: Data?, modifier: Modifier) {
    val isStudioEmpty = data?.studios.isNullOrEmpty()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier.padding(20.dp, 5.dp, 0.dp, 5.dp)
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
            text = yearSeasonText,
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
fun EpisodesLabel(episodes: Int, modifier: Modifier) {

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
                .background(Color.DarkGray)
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
fun AddToFavoriteRow(dao: Dao, modifier: Modifier, data: Data, viewModel: HomeScreenViewModel) {
    Spacer(modifier = modifier.height(10.dp))
    var isExpanded by remember { mutableStateOf(false) }


    if (isExpanded) {
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            },
            modifier = modifier
                .background(Color.DarkGray)
                .height(78.dp)
                .clip(RoundedCornerShape(40.dp)),
            offset = DpOffset((-40).dp, (-40).dp),
        ) {


            DropdownMenuItem(text = {
                Text(
                    text = "Watching", fontSize = 12.sp,
                    fontWeight = FontWeight.Thin,
                )
            }, modifier = modifier.height(20.dp), onClick = {
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    dao.addToCategory(
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
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    dao.addToCategory(
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
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    dao.addToCategory(
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
            Card(colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                modifier = modifier
                    .height(30.dp)
                    .clickable {
                        viewModel.viewModelScope.launch(Dispatchers.IO) {
                            dao.addToCategory(
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
        Column(modifier = modifier.width(10.dp)) {}
        Column(
            modifier = modifier.fillMaxWidth(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray, contentColor = Color.White
                ), modifier = Modifier.size(30.dp)
            ) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Filled.Menu,
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
fun StatusLine(status: String, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(1f)
    ) {

        Text(
            text = ("Status: "), fontSize = 12.sp, fontWeight = FontWeight.Thin, color = Color.White
        )
        Text(
            text = status, fontWeight = FontWeight.Thin, fontSize = 12.sp, color = LightGreen
        )
    }
}

@Composable
fun RatingLine(rating: String, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(1f)
    ) {

        Text(
            text = ("Rating: $rating"),
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin,
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
            .background(LightGreen, shape = RoundedCornerShape(15.dp))
            .padding(horizontal = 6.dp, vertical = 6.dp)
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
private fun DisplayCustomGenres(genres: List<Genre>, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp, 15.dp, 0.dp),
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
fun Synopsis(modifier: Modifier, synopsis: String?) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(0.dp, 0.dp, 0.dp, 15.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(15.dp, 10.dp, 0.dp, 0.dp)
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

@Composable
fun DisplayDialogPicture(
    imageRequest: ImageRequest,
    mal_id: Int,
    navController: NavController,
    modifier: Modifier
) {


    Image(
        painter = rememberAsyncImagePainter(model = imageRequest),
        contentDescription = "Big anime picture",

//        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CardDefaults.shape)
            .clickable {
                navigateToDetailScreen(navController, mal_id)
            },
        alignment = Alignment.Center,
    )
}

@Composable
private fun CheckGenresSize(numbOfGenres: Int?, genres: List<Genre>, modifier: Modifier) {
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