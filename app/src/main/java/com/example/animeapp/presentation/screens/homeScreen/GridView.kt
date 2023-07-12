package com.example.animeapp.presentation.screens.homeScreen


import HomeScreenViewModel
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.animeapp.R
import com.example.animeapp.dao.Dao
import com.example.animeapp.domain.models.newAnimeSearchModel.Data
import com.example.animeapp.presentation.theme.LightGreen
import com.example.animeapp.presentation.addToFavorite.AddFavorites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridAdder(
    navController: NavHostController,
    viewModel: HomeScreenViewModel,
    viewModelProvider: ViewModelProvider
) {
    val listData by viewModel.animeSearch.collectAsStateWithLifecycle()
    val scrollGridState = rememberLazyStaggeredGridState()
    val isLoading by viewModel.isNextPageLoading.collectAsStateWithLifecycle()





    LazyVerticalStaggeredGrid(
        state = scrollGridState,
        columns = StaggeredGridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(22.dp),
        verticalItemSpacing = 20.dp,
        contentPadding = PaddingValues(10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(1.dp))

        }

        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
        itemsIndexed(listData.data) { index, data ->


            AnimeCardBox(
                data = data,
                navController = navController,
                viewModel = viewModelProvider[HomeScreenViewModel::class.java]
            )

            // Загрузка следующей страницы при достижении конца списка и has_next_page = true
            if (index == listData.data.lastIndex && isLoading.not() && listData.pagination.has_next_page) {
                viewModel.loadNextPage()
            }
        }
    }



    if (viewModel.isDialogShown) {
        val selectedAnime = listData.data.find { it.mal_id == viewModel.selectedAnimeId.value }
        selectedAnime?.let { anime ->

            val model = ImageRequest.Builder(LocalContext.current)
                .data(anime.images.jpg.large_image_url)
                .size(Size.ORIGINAL)
                .crossfade(true)
                .build()


            CustomDialog(
                data = anime,
                navController = navController,
                onDismiss = {
                    viewModel.onDialogDismiss()
                },
                painter = rememberAsyncImagePainter(model = model)
            )
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimeCardBox(
    data: Data,
    navController: NavController,
    viewModel: HomeScreenViewModel
) {
    val painter = rememberAsyncImagePainter(model = data.images.webp.image_url)

    Card(
        modifier = Modifier
            .shadow(20.dp)
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(onLongClick = {
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModel.onDialogLongClick(data.mal_id)
                }
            }) {
                viewModel.viewModelScope.launch(Dispatchers.Main) {
                    navigateToDetailScreen(
                        navController, data.mal_id
                    )
                }
            },
        colors = CardDefaults.cardColors(containerColor = LightGreen),
        shape = RectangleShape,
    ) {
        Box {
            // Coil image loader
            Image(
                painter = painter,
                contentDescription = "Images for each Anime",
                modifier = Modifier
                    .aspectRatio(9f / 11f)
                    .clip(RoundedCornerShape(10.dp)),
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
                        contentDescription = "Score ${data.score}",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(45.dp)
                    )
                    Text(
                        text = formatScore(data.score),
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
                        contentDescription = "Scored by ${data.scored_by}",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .width(45.dp)
                            .height(50.dp)
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = formatScoredBy(data.scored_by),
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
                mal_id = data.mal_id,
                anime = data.title,
                score = formatScore(data.score),
                scoredBy = formatScoredBy(data.scored_by),
                animeImage = data.images.jpg.image_url,
                context = LocalContext.current,
                modifier = Modifier
                    .padding(end = 6.dp, top = 140.dp),
                viewModel = viewModel
            )


        }

        Text(
            text = data.title,
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

        Row(modifier = Modifier.width(130.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Text(text = "Status: " + data.status, fontSize = 10.sp, textAlign = TextAlign.Left)
        }
        Row(modifier = Modifier.width(60.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Text(text = "Type: " + data.type, fontSize = 10.sp, textAlign = TextAlign.Left)
        }


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
    val painter = rememberAsyncImagePainter(model = R.drawable.kurisu)

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
                colors = CardDefaults.cardColors(containerColor = LightGreen),
                shape = RectangleShape,
            ) {
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


@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true)
@Composable
fun PreviewGridView2() {
    val painter = rememberAsyncImagePainter(model = R.drawable.kurisu)

//    val weight = if (it < 3) 2f else 1f
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(140.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalItemSpacing = 10.dp,
    ) {
        item {

        }
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
        items(6) {
            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(22.dp))
                    .clickable {},
                colors = CardDefaults.cardColors(containerColor = LightGreen),
                shape = RectangleShape,
            ) {
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


                }

                Text(
                    text = "Stein;Gate $it",
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
