package com.example.animeapp.presentation.Screens.favoritesScreen


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
import androidx.compose.material3.TextButton
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
import com.example.animeapp.domain.viewModel.IdViewModel
import com.example.animeapp.presentation.Screens.homeScreen.navigateToDetailScreen
import com.example.animeapp.presentation.theme.LightYellow

@Composable
fun Fav(navController: NavController, viewModelProvider: ViewModelProvider) {

    AnimeListScreen(navController = navController, viewModelProvider = viewModelProvider)

}


@Preview(showSystemUi = true)
@Composable
fun PreviewFavoriteScreen() {

}


enum class AnimeListType {
    WATCHING, PLANNED, WATCHED, DROPPED
}

@Composable
fun AnimeListScreen(navController: NavController, viewModelProvider: ViewModelProvider) {
    var selectedListType by rememberSaveable { mutableStateOf(AnimeListType.WATCHING) }
    val dao = MainDb.getDb(LocalContext.current).getDao()
    val scrollStateWatching = rememberLazyGridState()
    val scrollStatePlanned = rememberLazyGridState()
    val scrollStateWatched = rememberLazyGridState()
    val scrollStateDropped = rememberLazyGridState()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            Modifier
                .weight(2f / 10)
                .background(Color.Red)
        ) {
            TextButton(
                onClick = { selectedListType = AnimeListType.WATCHING },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedListType == AnimeListType.WATCHING) Color.Green else Color.LightGray
                )
            ) {
                Text("WATCHING")
            }

            Button(
                onClick = { selectedListType = AnimeListType.PLANNED },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedListType == AnimeListType.PLANNED) Color.Green else Color.LightGray
                )
            ) {
                Text("PLANNED")
            }

            Button(
                onClick = { selectedListType = AnimeListType.WATCHED },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedListType == AnimeListType.WATCHED) Color.Green else Color.LightGray
                )
            ) {
                Text("WATCHED")
            }

            Button(
                onClick = { selectedListType = AnimeListType.DROPPED },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedListType == AnimeListType.DROPPED) Color.Green else Color.LightGray
                )
            ) {
                Text("DROPPED")
            }
        }
        // Разный список аниме в зависимости от выбранного типа
        when (selectedListType) {
            AnimeListType.WATCHING -> AnimeListWatching(
                dao = dao,
                navController = navController,
                viewModelProvider = viewModelProvider,
                scrollState = scrollStateWatching
            )

            AnimeListType.PLANNED -> AnimeListPlanned(
                dao = dao,
                navController = navController,
                viewModelProvider = viewModelProvider,
                scrollState = scrollStatePlanned
            )

            AnimeListType.WATCHED -> AnimeListWatched(
                dao = dao,
                navController = navController,
                viewModelProvider = viewModelProvider,
                scrollState = scrollStateWatched
            )

            AnimeListType.DROPPED -> AnimeListDropped(
                dao = dao,
                navController = navController,
                viewModelProvider = viewModelProvider,
                scrollState = scrollStateDropped
            )
        }
        Spacer(modifier = Modifier.height(35.dp))
    }
}


@Composable
fun AnimeListWatching(
    dao: Dao,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    scrollState: LazyGridState
) {
    val animeListState by dao.getWatchingAnime()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    // Отображение списка аниме
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(9f / 10f)
    ) {
        LazyVerticalGrid(columns = GridCells.Adaptive(140.dp), state = scrollState) {
            items(animeListState) {
                AnimeCardBox2(
                    animeItem = it,
                    navController = navController,
                    viewModelProvider = viewModelProvider
                )
            }
        }

    }

}


@Composable
fun AnimeListPlanned(
    dao: Dao,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    scrollState: LazyGridState
) {
    val animeListState by dao.getPlannedAnime()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    // Отображение списка аниме
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(9f / 10f)
    ) {
        LazyVerticalGrid(columns = GridCells.Adaptive(140.dp), state = scrollState) {
            items(animeListState) {
                AnimeCardBox2(
                    animeItem = it,
                    navController = navController,
                    viewModelProvider = viewModelProvider
                )
            }
        }
    }
}


@Composable
fun AnimeListWatched(
    dao: Dao,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    scrollState: LazyGridState
) {
    val animeListState by dao.getWatchedAnime()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    // Отображение списка аниме
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(9f / 10f)
    ) {
        LazyVerticalGrid(columns = GridCells.Adaptive(140.dp), state = scrollState) {
            items(animeListState) {
                AnimeCardBox2(
                    animeItem = it,
                    navController = navController,
                    viewModelProvider = viewModelProvider
                )
            }
        }

    }
}


@Composable
fun AnimeListDropped(
    dao: Dao,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    scrollState: LazyGridState
) {
    val animeListState by dao.getDroppedAnime()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    // Отображение списка аниме
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(9f / 10f)
    ) {
        LazyVerticalGrid(columns = GridCells.Adaptive(140.dp), state = scrollState) {
            items(animeListState) {
                AnimeCardBox2(
                    animeItem = it,
                    navController = navController,
                    viewModelProvider = viewModelProvider
                )
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimeCardBox2(
    animeItem: AnimeItem, navController: NavController, viewModelProvider: ViewModelProvider
) {
    val viewModel = viewModelProvider[IdViewModel::class.java]
//    var isVisible by remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter(model = animeItem.animeImage)


    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable {
                animeItem.id?.let { viewModel.setId(it) }
                animeItem.id?.let {
                    navigateToDetailScreen(
                        navController, it
                    )
                }
            },
        colors = CardDefaults.cardColors(containerColor = LightYellow),
        shape = RectangleShape,
    ) {
        Box {
            // Coil image loader
            Image(
                painter = painter,
                contentDescription = "Images for each Anime",
                modifier = Modifier.aspectRatio(9f / 11f),
                contentScale = ContentScale.FillBounds
            )

            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier.size(45.dp), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Score ${animeItem.score}",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(45.dp)
                    )
                    Text(
                        text = animeItem.score,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,

                        )
                }
                Box(
                    modifier = Modifier
                        .width(45.dp)
                        .height(50.dp),

                    ) {

                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Scored by ${animeItem.scored_by}",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .width(45.dp)
                            .height(50.dp)
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = animeItem.scored_by,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(
                                Alignment.BottomEnd
                            )
                    )
                }
            }

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
                .padding(end = 16.dp, top = 16.dp, bottom = 16.dp, start = 16.dp),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight(1000),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )


    }

}
