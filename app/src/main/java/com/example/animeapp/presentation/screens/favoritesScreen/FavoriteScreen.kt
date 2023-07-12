package com.example.animeapp.presentation.screens.favoritesScreen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.dao.AnimeItem
import com.example.animeapp.dao.Dao
import com.example.animeapp.dao.MainDb
import com.example.animeapp.domain.viewModel.DetailScreenViewModel
import com.example.animeapp.presentation.addToFavorite.AddFavorites
import com.example.animeapp.presentation.screens.homeScreen.navigateToDetailScreen
import com.example.animeapp.presentation.theme.LightGreen

@Preview(showSystemUi = true)
@Composable
fun PreviewFavoriteScreen() {
    // TODO: Implement preview
}

enum class AnimeListType {
    WATCHING, PLANNED, WATCHED, DROPPED
}

// Function that creates 4 grid sections
// - watching, planned, watched, dropped
@Composable
fun FavoriteScreen(navController: NavController, viewModelProvider: ViewModelProvider) {
    var selectedListType by rememberSaveable { mutableStateOf(AnimeListType.WATCHING) }
    val dao = MainDb.getDb(LocalContext.current).getDao()
    val scrollState = rememberLazyGridState()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(modifier = Modifier.weight(2f / 10)) {
            FavoriteAnimeListButton(
                listType = AnimeListType.WATCHING,
                selectedListType = selectedListType,
                onClick = { selectedListType = AnimeListType.WATCHING },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            FavoriteAnimeListButton(
                listType = AnimeListType.PLANNED,
                selectedListType = selectedListType,
                onClick = { selectedListType = AnimeListType.PLANNED },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            FavoriteAnimeListButton(
                listType = AnimeListType.WATCHED,
                selectedListType = selectedListType,
                onClick = { selectedListType = AnimeListType.WATCHED },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            FavoriteAnimeListButton(
                listType = AnimeListType.DROPPED,
                selectedListType = selectedListType,
                onClick = { selectedListType = AnimeListType.DROPPED },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }

        when (selectedListType) {
            AnimeListType.WATCHING -> FavoriteAnimeList(
                dao = dao,
                category = "Watching",
                navController = navController,
                viewModelProvider = viewModelProvider,
                scrollState = scrollState
            )

            AnimeListType.PLANNED -> FavoriteAnimeList(
                dao = dao,
                category = "Planned",
                navController = navController,
                viewModelProvider = viewModelProvider,
                scrollState = scrollState
            )

            AnimeListType.WATCHED -> FavoriteAnimeList(
                dao = dao,
                category = "Watched",
                navController = navController,
                viewModelProvider = viewModelProvider,
                scrollState = scrollState
            )

            AnimeListType.DROPPED -> FavoriteAnimeList(
                dao = dao,
                category = "Dropped",
                navController = navController,
                viewModelProvider = viewModelProvider,
                scrollState = scrollState
            )
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
    Button(
        onClick = onClick, modifier = modifier, colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedListType == listType) Color.Green else Color.LightGray
        )
    ) {
        Text(text = listType.name, maxLines = 1)
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
    scrollState: LazyGridState
) {
    val animeListState by dao.getAnimeInCategory(category)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(9f / 10f)
    ) {
        LazyVerticalGrid(columns = GridCells.Adaptive(140.dp), state = scrollState,

            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(5.dp)) {
            items(animeListState) { animeItem ->
                FavoriteScreenCardBox(
                    animeItem = animeItem,
                    navController = navController,
                    viewModelProvider = viewModelProvider
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
    animeItem: AnimeItem, navController: NavController,
    viewModelProvider: ViewModelProvider
) {
    val viewModel = viewModelProvider[DetailScreenViewModel::class.java]
    val painter = rememberAsyncImagePainter(model = animeItem.animeImage)

    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable {
                animeItem.id?.let {
//                    viewModel.setId(it)
                    navigateToDetailScreen(navController, it)
                }
//                animeItem.id?.let {

//                }
            },
        colors = CardDefaults.cardColors(containerColor = LightGreen),
        shape = RectangleShape
    ) {
        Column(modifier = Modifier) {
            Box {
                Image(
                    painter = painter,
                    contentDescription = "Images for anime: ${animeItem.anime}",
                    modifier = Modifier.aspectRatio(9f / 11f),
                    contentScale = ContentScale.FillBounds
                )

                Column(modifier = Modifier.background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))) {
                    ScoreIcon(score = animeItem.score)
                    ScoredByIcon(scoredBy = animeItem.scored_by)
                }
                AddFavorites(
                    mal_id = animeItem.id ?: 0,
                    anime = animeItem.anime,
                    score = animeItem.score,
                    scoredBy = animeItem.scored_by,
                    animeImage = animeItem.animeImage,
                    context = LocalContext.current,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 6.dp, top = 180.dp),
                    viewModel = viewModel
                )
            }
            Text(
                text = animeItem.anime,
                textAlign = TextAlign.Center,
                modifier = Modifier
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
fun ScoreIcon(score: String) {
    Box(modifier = Modifier.size(45.dp), contentAlignment = Alignment.Center) {
        Icon(
            Icons.Filled.Star,
            contentDescription = "Score $score",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(45.dp)
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
fun ScoredByIcon(scoredBy: String) {
    Box(modifier = Modifier.size(45.dp), contentAlignment = Alignment.Center) {
        Icon(
            Icons.Filled.Person,
            contentDescription = "Scored by $scoredBy",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(45.dp)
        )
        Text(
            textAlign = TextAlign.Center,
            text = scoredBy,
            color = Color.White,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
        )
    }
}

