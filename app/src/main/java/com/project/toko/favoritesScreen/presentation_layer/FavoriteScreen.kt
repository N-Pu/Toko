package com.project.toko.favoritesScreen.presentation_layer


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.project.toko.core.dao.AnimeItem
import com.project.toko.core.dao.Dao
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.core.presentation_layer.addToFavorite.AddFavorites
import com.project.toko.homeScreen.presentation_layer.homeScreen.navigateToDetailScreen
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.core.presentation_layer.theme.SoftGreen


enum class AnimeListType {
    WATCHING, PLANNED, WATCHED, DROPPED
}

// Function that creates 4 grid sections
// - watching, planned, watched, dropped
@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    dao: Dao
) {
    var selectedListType by rememberSaveable { mutableStateOf(AnimeListType.WATCHING) }
    val scrollState = rememberLazyGridState()

    Column(
        modifier
            .fillMaxHeight(1f)
            .fillMaxWidth(1f)
            .background(Color.White)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(1f)
                .height(80.dp)
                .background(LightGreen),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                FavoriteAnimeListButton(
                    listType = AnimeListType.WATCHING,
                    selectedListType = selectedListType,
                    onClick = { selectedListType = AnimeListType.WATCHING },
                    modifier = modifier
                )
            }
            Column(
                modifier = modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FavoriteAnimeListButton(
                    listType = AnimeListType.PLANNED,
                    selectedListType = selectedListType,
                    onClick = { selectedListType = AnimeListType.PLANNED },
                    modifier = modifier
                )
            }
            Column(
                modifier = modifier
                    .weight(1f)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FavoriteAnimeListButton(
                    listType = AnimeListType.WATCHED,
                    selectedListType = selectedListType,
                    onClick = { selectedListType = AnimeListType.WATCHED },
                    modifier = modifier
                )
            }
            Column(
                modifier = modifier
                    .weight(1f)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FavoriteAnimeListButton(
                    listType = AnimeListType.DROPPED,
                    selectedListType = selectedListType,
                    onClick = { selectedListType = AnimeListType.DROPPED },
                    modifier = modifier
                )
            }

        }
        Row(
            modifier = modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f)
                .background(Color.White)
        ) {
            when (selectedListType) {
                AnimeListType.WATCHING -> FavoriteAnimeList(
                    dao = dao,
                    category = "Watching",
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.PLANNED -> FavoriteAnimeList(
                    dao = dao,
                    category = "Planned",
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.WATCHED -> FavoriteAnimeList(
                    dao = dao,
                    category = "Watched",
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.DROPPED -> FavoriteAnimeList(
                    dao = dao,
                    category = "Dropped",
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    scrollState = scrollState,
                    modifier = modifier
                )
            }
        }


        Spacer(modifier = Modifier.height(35.dp))
    }
}


// Buttons(watching, planned, watched, dropped)
// on the top of the Favorites screen
@Composable
fun FavoriteAnimeListButton(
    listType: AnimeListType,
    selectedListType: AnimeListType,
    onClick: () -> Unit,
    modifier: Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .padding(10.dp)
            .height(30.dp)
    ) {
        TextButton(
            onClick = onClick, modifier = modifier, colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedListType == listType) SoftGreen else Color.LightGray
            ),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                text = listType.name,
                maxLines = 1,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}


// List of anime in current category
// (watching, planned, watched, dropped)
@Composable
fun FavoriteAnimeList(
    dao: Dao,
    category: String,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    scrollState: LazyGridState,
    modifier: Modifier
) {
    val animeListState by dao.getAnimeInCategory(category)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.95f)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp), state = scrollState,

            modifier = modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(5.dp)
        ) {
            items(animeListState) { animeItem ->
                FavoriteScreenCardBox(
                    animeItem = animeItem,
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    modifier = modifier,
                    dao = dao
                )
            }
        }
    }
}


// Function for showing a single
// anime-card with "+" button
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteScreenCardBox(
    animeItem: AnimeItem,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    dao: Dao
) {
    val viewModel = viewModelProvider[DetailScreenViewModel::class.java]
    val painter = rememberAsyncImagePainter(model = animeItem.animeImage)

    Box(
        modifier = modifier
    ) {
        Card(
            modifier = modifier
                .clickable {
                    animeItem.id?.let {
                        navigateToDetailScreen(navController, it)
                    }
                },
            colors = CardDefaults.cardColors(containerColor = LightGreen),
            shape = RectangleShape
        ) {


            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(1f)
            ) {
                Image(
                    painter = painter,
                    contentDescription = "Images for anime: ${animeItem.anime}",
                    modifier = modifier.aspectRatio(9f / 11f),
                    contentScale = ContentScale.FillBounds
                )

                AddFavorites(
                    mal_id = animeItem.id ?: 0,
                    anime = animeItem.anime,
                    score = animeItem.score,
                    scoredBy = animeItem.scored_by,
                    animeImage = animeItem.animeImage,
                    modifier = modifier,
                    viewModel = viewModel,
                    dao = dao
                )


                Column(modifier = modifier.background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))) {
                    ScoreIcon(score = animeItem.score, modifier)
                    ScoredByIcon(scoredBy = animeItem.scored_by, modifier)
                }

            }

            Text(
                text = animeItem.anime,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        delayMillis = 2000,
                        initialDelayMillis = 2000,
                        velocity = 50.dp
                    )
                    .padding(16.dp),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight(1000),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

// Icon that placed in FavoriteScreenCardBox
// that shows score
@Composable
private fun ScoreIcon(score: String, modifier: Modifier) {
    Box(modifier = modifier.size(45.dp), contentAlignment = Alignment.Center) {
        Icon(
            Icons.Filled.Star,
            contentDescription = "Score $score",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = modifier.size(45.dp)
        )
        Text(
            text = score,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

// Icon that placed in FavoriteScreenCardBox
// that shows score by users of MyAnimeList.com
@Composable
private fun ScoredByIcon(scoredBy: String, modifier: Modifier) {
    Box(modifier = modifier.size(45.dp), contentAlignment = Alignment.Center) {
        Icon(
            Icons.Filled.Person,
            contentDescription = "Scored by $scoredBy",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = modifier.size(45.dp)
        )
        Text(
            textAlign = TextAlign.Center,
            text = scoredBy,
            color = Color.White,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
        )
    }
}

