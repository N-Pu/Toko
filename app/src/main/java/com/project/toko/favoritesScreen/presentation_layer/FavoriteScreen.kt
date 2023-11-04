package com.project.toko.favoritesScreen.presentation_layer


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
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
import com.project.toko.characterDetailedScreen.dao.CharacterItem
import com.project.toko.favoritesScreen.dao.AnimeItem
import com.project.toko.core.presentation_layer.addToFavorite.AddFavorites
import com.project.toko.homeScreen.presentation_layer.homeScreen.navigateToDetailScreen
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.core.presentation_layer.theme.SoftGreen
import com.project.toko.core.viewModel.daoViewModel.DaoViewModel
import com.project.toko.favoritesScreen.model.AnimeListType
import com.project.toko.personDetailedScreen.dao.PersonItem


// Function that creates 4 grid sections
// - watching, planned, watched, dropped
@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier
) {
    var selectedListType by rememberSaveable { mutableStateOf(AnimeListType.WATCHING) }
    val scrollState = rememberLazyGridState()
    val arrayOfEntries = AnimeListType.values()

    Column(
        modifier
            .fillMaxHeight(1f)
            .fillMaxWidth(1f)
            .background(Color.White)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(1f)
                .height(50.dp)
                .background(LightGreen)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {

            arrayOfEntries.forEach { entry ->
                Column(
                    modifier = modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    FavoriteAnimeListButton(
                        listType = entry,
                        selectedListType = selectedListType,
                        onClick = { selectedListType = entry},
                        modifier = modifier
                    )
                }
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
                    category = AnimeListType.WATCHING.route,
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.PLANNED -> FavoriteAnimeList(
                    category = AnimeListType.PLANNED.route,
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.COMPLETED -> FavoriteAnimeList(
                    category = AnimeListType.COMPLETED.route,
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.DROPPED -> FavoriteAnimeList(
                    category = AnimeListType.DROPPED.route,
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.PERSON -> ShowPerson(
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.FAVORITE -> FavoriteAnimeList(
                    category = AnimeListType.FAVORITE.route,
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.CHARACTER -> ShowCharacter(
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    scrollState = scrollState,
                    modifier = modifier
                )
            }
        }
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
    category: String,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    scrollState: LazyGridState,
    modifier: Modifier
) {


    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    val animeListState by daoViewModel.getAnimeInCategory(category).collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    Column(
        modifier = modifier
            .fillMaxSize(1f)
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
                    modifier = modifier
                )
            }
        }
    }
}


@Composable
fun ShowCharacter(
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    scrollState: LazyGridState,
    modifier: Modifier
) {


    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    val animeListState by daoViewModel.getCharacter().collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    Column(
        modifier = modifier
            .fillMaxSize(1f)
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
            items(animeListState) { characterItem ->
                CharacterCardBox(
                    characterItem = characterItem,
                    navController = navController,
                    modifier = modifier
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CharacterCardBox(
    characterItem: CharacterItem,
    navController: NavController,
    modifier: Modifier
) {
    val painter = rememberAsyncImagePainter(model = characterItem.image)

    Box(
        modifier = modifier
    ) {
        Card(
            modifier = modifier
                .clickable {
                    characterItem.id?.let {
                        navController.navigate(route = "detail_on_character/${it}")
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
                    contentDescription = "Images for anime: ${characterItem.anime}",
                    modifier = modifier.aspectRatio(9f / 11f),
                    contentScale = ContentScale.FillBounds
                )

//                AddFavorites(
//                    mal_id = animeItem.id ?: 0,
//                    anime = animeItem.anime,
//                    score = animeItem.score,
//                    scoredBy = animeItem.scored_by,
//                    animeImage = animeItem.animeImage,
//                    modifier = modifier,
//                    viewModelProvider = viewModelProvider
//                )

            }
            Text(
                text = characterItem.name,
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

            Text(
                text = "From:" + characterItem.anime,
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











@Composable
fun ShowPerson(
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    scrollState: LazyGridState,
    modifier: Modifier
) {


    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    val personList by daoViewModel.getPerson().collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    Column(
        modifier = modifier
            .fillMaxSize(1f)
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
            items(personList) { personItem ->
                PersonCardBox(
                    personItem = personItem,
                    navController = navController,
                    modifier = modifier
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PersonCardBox(
    personItem: PersonItem,
    navController: NavController,
    modifier: Modifier
) {
    val painter = rememberAsyncImagePainter(model = personItem.image)

    Box(
        modifier = modifier
    ) {
        Card(
            modifier = modifier
                .clickable {
                    personItem.id?.let {
                        navController.navigate("detail_on_staff/${it}")
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
                    contentDescription = "Images for anime: ${personItem.image}",
                    modifier = modifier.aspectRatio(9f / 11f),
                    contentScale = ContentScale.FillBounds
                )

//                AddFavorites(
//                    mal_id = animeItem.id ?: 0,
//                    anime = animeItem.anime,
//                    score = animeItem.score,
//                    scoredBy = animeItem.scored_by,
//                    animeImage = animeItem.animeImage,
//                    modifier = modifier,
//                    viewModelProvider = viewModelProvider
//                )

            }
            Text(
                text = personItem.name,
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

            if (!personItem.givenName.isNullOrEmpty()) {
                Text(
                    text = "Given name:" + personItem.givenName,
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
            if (!personItem.familyName.isNullOrEmpty()) {
                Text(
                    text = "Family name:" + personItem.familyName,
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
}























// Function for showing a single
// anime-card with "+" button
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteScreenCardBox(
    animeItem: AnimeItem,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier
) {
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
                    viewModelProvider = viewModelProvider
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

