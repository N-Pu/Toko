package com.example.animeapp.presentation.screens.homeScreen


import HomeScreenViewModel
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.R
import com.example.animeapp.dao.Dao
import com.example.animeapp.presentation.theme.LightYellow
import com.example.animeapp.domain.viewModel.IdViewModel
import com.example.animeapp.presentation.addToFavorite.AddFavorites
import kotlinx.coroutines.flow.Flow
import java.util.Locale

//@Composable
//fun GridAdder(
//
//    navController: NavHostController, viewModelProvider: ViewModelProvider
//) {
//
//    val viewModel = viewModelProvider[HomeScreenViewModel::class.java]
//    val listData =
//        viewModel.animeSearch.collectAsStateWithLifecycle()
//    val scrollGridState = rememberLazyGridState()
//
//
//// Добавить rememberScrollState
//    LazyVerticalGrid(
//        state = scrollGridState, // Использовать scrollState в качестве state
//        columns = GridCells.Adaptive(minSize = 140.dp),
//        modifier = Modifier.fillMaxSize(),
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//
//        itemsIndexed(listData.value.data) { index, anime ->
//
//
//            AnimeCardBox(
//                anime = anime, navController = navController, viewModelProvider = viewModelProvider
//            )
//        }
//
//    }
//}


@Composable
fun GridAdder(
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    val viewModel = viewModelProvider[HomeScreenViewModel::class.java]
    val listData = viewModel.animeSearch.collectAsStateWithLifecycle().value
    val scrollGridState = rememberLazyGridState()
    val isLoading = viewModel.isNextPageLoading.collectAsStateWithLifecycle().value



    LazyVerticalGrid(
        state = scrollGridState,
        columns = GridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(listData.data) { index, anime ->
            AnimeCardBox(
                anime = anime,
                navController = navController,
                viewModelProvider = viewModelProvider
            )

            // Загрузка следующей страницы при достижении конца списка и has_next_page = true
            if (index == listData.data.lastIndex && isLoading.not() && listData.pagination.has_next_page) {
//                viewModel.loadNextPage(listData)
                viewModel.loadNextPage()
            }
        }
    }
}





@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimeCardBox(
    anime: com.example.animeapp.domain.models.newAnimeSearchModel.Data,
    navController: NavController,
    viewModelProvider: ViewModelProvider
) {
    val viewModel = viewModelProvider[IdViewModel::class.java]
//    var isVisible by remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter(model = anime.images.webp.image_url)

//    AnimatedVisibility(
//        visible = isVisible, enter = fadeIn(
//            initialAlpha = 0.0f, animationSpec = TweenSpec(durationMillis = 500)
//        ), exit = fadeOut(
//            animationSpec = TweenSpec(durationMillis = 500)
//
//        )
//    ) {

    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable {
                viewModel.setId(anime.mal_id)
                navigateToDetailScreen(
                    navController, anime.mal_id
                )
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
                        contentDescription = "Score ${anime.score}",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(45.dp)
                    )
                    Text(
                        text = formatScore(anime.score),
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
                        contentDescription = "Scored by ${anime.scored_by}",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .width(45.dp)
                            .height(50.dp)
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = formatScoredBy(anime.scored_by),
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

            AddFavorites(
                mal_id = anime.mal_id,
                anime = anime.title,
                score = formatScore(anime.score),
                scoredBy = formatScoredBy(anime.scored_by),
                animeImage = anime.images.jpg.image_url,
                context = LocalContext.current,
                modifier = Modifier
                    .padding(end = 6.dp, top = 140.dp)
            )


        }

        Text(
            text = anime.title,
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


fun formatScoredBy(float: Float): String {
    return if (float == 0f) {
        "N/A"
    } else {
        val formattedString = String.format(Locale.US, "%.1f", float)
        if (formattedString.endsWith(".0")) {
            formattedString.substring(0, formattedString.length - 2)
        } else {
            formattedString.replace(",", ".")
        }
    }
}


fun formatScore(float: Float?): String {
    return if (float == null || float == 0f) {
        "N/A"
    } else {
        float.toString()
    }
}


@Composable
fun checkIdInDataBase(
    dao: Dao, id: Int
): Flow<Boolean> {
    return dao.containsInDataBase(id)
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true)
@Composable
fun PreviewGridView() {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(6) {
            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable {},
                colors = CardDefaults.cardColors(containerColor = LightYellow),
                shape = RectangleShape,
            ) {
                val painter = rememberAsyncImagePainter(model = R.drawable.kurisu)
                Box(modifier = Modifier.fillMaxSize()) {
                    // Coil image loader
                    Image(
                        painter = painter,
                        contentDescription = "Images for each Anime",
                        modifier = Modifier.aspectRatio(9f / 11f),
                        contentScale = ContentScale.FillBounds
                    )

                    Column(
                        modifier = Modifier.background(
                            MaterialTheme.colorScheme.secondary.copy(
                                alpha = 0.2f
                            )
                        )
                    ) {
                        Box(
                            modifier = Modifier.size(45.dp), contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "Favorite Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(45.dp)
                            )
                            Text(
                                text = "9.33",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(45.dp)
                                .height(50.dp),

                            ) {
//                            Column(modifier = Modifier.align(Alignment.Center).fillMaxSize()) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = "Favorite Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .width(45.dp)
                                    .height(50.dp)
                            )
                            Text(
                                textAlign = TextAlign.Center,
                                text = "1200000",
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

//                    AddFavorites()

                }

                Text(
                    text = "Stein;Gate",
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
    }

}