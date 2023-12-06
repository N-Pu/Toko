package com.project.toko.daoScreen.presentation_layer

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.project.toko.R
import com.project.toko.characterDetailedScreen.dao.CharacterItem
import com.project.toko.core.presentation_layer.theme.BackArrowCastColor
import com.project.toko.core.presentation_layer.theme.BackArrowSecondCastColor
import com.project.toko.core.presentation_layer.theme.DarkBackArrowCastColor
import com.project.toko.core.presentation_layer.theme.DarkBackArrowSecondCastColor
import com.project.toko.core.presentation_layer.theme.DarkSearchBarColor
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.core.presentation_layer.theme.SearchBarColor
import com.project.toko.core.presentation_layer.theme.darkFavoriteTopBarColors
import com.project.toko.core.presentation_layer.theme.iconColorInSearchPanel
import com.project.toko.core.presentation_layer.theme.lightFavoriteTopBarColors
import com.project.toko.core.presentation_layer.theme.threeLines
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.daoScreen.dao.FavoriteItem
import com.project.toko.daoScreen.daoViewModel.DaoViewModel
import com.project.toko.daoScreen.model.AnimeListType
import com.project.toko.homeScreen.presentation_layer.homeScreen.navigateToDetailScreen
import com.project.toko.personDetailedScreen.dao.PersonItem

//class DaoScreen

//@Inject lateinit var svgImageLoader : ImageLoader

@Composable
fun DaoScreen(
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier
) {

    val svgImageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()

    var selectedListType by rememberSaveable { mutableStateOf(AnimeListType.WATCHING) }
    val arrayOfEntries = AnimeListType.values()
    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    val searchText by daoViewModel.searchText.collectAsStateWithLifecycle()
    val rightSortingMenu = remember { mutableStateOf(false) }
    val leftSortingMenu = remember { mutableStateOf(false) }

    val isSortedAlphabetically = daoViewModel.isSortedAlphabetically
    val isSortedByScore = daoViewModel.isSortedByScore
    val isSortedByUsers = daoViewModel.isSortedByUsers
    val isAiredFrom = daoViewModel.isAiredFrom


    val selectedType = daoViewModel.selectedType
    val isTvSelected = daoViewModel.isTvSelected
    val isMovieSelected = daoViewModel.isMovieSelected
    val isOvaSelected = daoViewModel.isOvaSelected
    val isSpecialSelected = daoViewModel.isSpecialSelected
    val isOnaSelected = daoViewModel.isOnaSelected
    val isMusicSelected = daoViewModel.isMusicSelected


    Column(
        modifier = modifier
            .fillMaxWidth(1f)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                .height(140.dp)
                .shadow(20.dp)
                .fillMaxWidth(1f)
                .background(MaterialTheme.colorScheme.error)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 0.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = R.drawable.tokominilogo),
                    contentDescription = "None",
                    modifier = modifier
                        .height(50.dp)
                        .width(70.dp),
                    alpha = 0.8f,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                )
            }
            Row(
                modifier = modifier
                    .wrapContentSize()
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSystemInDarkTheme()) DarkSearchBarColor else SearchBarColor),
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedTextField(
                    placeholder = { Text(text = "Search...", color = Color.Gray) },
                    value = searchText ?: "",
                    onValueChange = daoViewModel::onSearchTextChange,
                    modifier = modifier
                        .clip(RoundedCornerShape(30.dp))
                        .height(50.dp)
                        .fillMaxWidth(1f),
                    prefix = {
                        Icon(Icons.Filled.Search, "Search Icon", tint = iconColorInSearchPanel)
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = iconColorInSearchPanel,
                        focusedPlaceholderColor = iconColorInSearchPanel,
                        unfocusedPlaceholderColor = iconColorInSearchPanel,
                        cursorColor = iconColorInSearchPanel,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent

                    )
                )
            }
        }
        Row(
            modifier = modifier
                .fillMaxWidth(1f)
                .height(61.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            arrayOfEntries.forEachIndexed { i, entry ->
                FavoriteAnimeListButton(
                    listType = entry,
                    selectedListType = selectedListType,
                    onClick = { selectedListType = entry },
                    modifier = modifier,
                    colorIndex = i
                )
            }
        }

        if ((selectedListType != AnimeListType.CHARACTER) and (selectedListType != AnimeListType.PERSON)) {
            TwoSortingButtons(
                modifier = modifier,
                svgImageLoader = svgImageLoader,
                rightSortingMenu = rightSortingMenu,
                isSortedAlphabetically = isSortedAlphabetically,
                isSortedByScore = isSortedByScore,
                isSortedByUsers = isSortedByUsers,
                isAiredFrom = isAiredFrom,
                leftSortingMenu = leftSortingMenu,
                isTvSelected = isTvSelected,
                isMovieSelected = isMovieSelected,
                isOvaSelected = isOvaSelected,
                isSpecialSelected = isSpecialSelected,
                isOnaSelected = isOnaSelected,
                isMusicSelected = isMusicSelected,
                selectedType = selectedType
            )
        }
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            when (selectedListType) {
                AnimeListType.WATCHING -> DataAnimeList(
                    navController = navController,
                    daoViewModel = daoViewModel,
                    modifier = modifier,
                    category = AnimeListType.WATCHING.route,
                    isSortedAlphabetically = isSortedAlphabetically,
                    isSortedByScore = isSortedByScore,
                    isSortedByUsers = isSortedByUsers,
                    isAiredFrom = isAiredFrom,
                    searchText = searchText ?: "",
                    type = selectedType
                )

                AnimeListType.PLANNED -> DataAnimeList(
                    navController = navController,
                    daoViewModel = daoViewModel,
                    modifier = modifier,
                    category = AnimeListType.PLANNED.route,
                    isSortedAlphabetically = isSortedAlphabetically,
                    isSortedByScore = isSortedByScore,
                    isSortedByUsers = isSortedByUsers,
                    isAiredFrom = isAiredFrom,
                    searchText = searchText ?: "",
                    type = selectedType
                )

                AnimeListType.COMPLETED -> DataAnimeList(
                    navController = navController,
                    daoViewModel = daoViewModel,
                    modifier = modifier,
                    category = AnimeListType.COMPLETED.route,
                    isSortedAlphabetically = isSortedAlphabetically,
                    isSortedByScore = isSortedByScore,
                    isSortedByUsers = isSortedByUsers,
                    isAiredFrom = isAiredFrom,
                    searchText = searchText ?: "",
                    type = selectedType
                )

                AnimeListType.DROPPED -> DataAnimeList(
                    navController = navController,
                    daoViewModel = daoViewModel,
                    modifier = modifier,
                    category = AnimeListType.DROPPED.route,
                    isSortedAlphabetically = isSortedAlphabetically,
                    isSortedByScore = isSortedByScore,
                    isSortedByUsers = isSortedByUsers,
                    isAiredFrom = isAiredFrom,
                    searchText = searchText ?: "",
                    type = selectedType
                )

                AnimeListType.FAVORITE -> FavoriteList(
                    navController = navController,
                    daoViewModel = daoViewModel,
                    modifier = modifier,
                    isSortedAlphabetically = isSortedAlphabetically,
                    isSortedByScore = isSortedByScore,
                    isSortedByUsers = isSortedByUsers,
                    isAiredFrom = isAiredFrom,
                    searchText = searchText ?: "",
                    type = selectedType
                )

                AnimeListType.PERSON -> ShowPerson(
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    modifier = modifier
                )

                AnimeListType.CHARACTER -> ShowCharacter(
                    navController = navController,
                    modifier = modifier,
                    daoViewModel = daoViewModel,
                )
            }
        }
    }
}

@Composable
private fun FavoriteAnimeListButton(
    listType: AnimeListType,
    selectedListType: AnimeListType,
    onClick: () -> Unit,
    modifier: Modifier,
    colorIndex: Int
) {
    val colors = if (isSystemInDarkTheme()) darkFavoriteTopBarColors else lightFavoriteTopBarColors
    val customModifier = if (selectedListType == listType) modifier
        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
        .clickable { onClick() }
        .fillMaxHeight(1f)
        .background(colors[colorIndex])
        .animateContentSize()
    else modifier
        .clickable { onClick() }
        .background(colors[colorIndex])
        .animateContentSize()

    Box(modifier = customModifier) {
        TextButton(
            modifier = modifier.padding(horizontal = 20.dp),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = Color.Transparent,
            )
        ) {
            Text(
                text = listType.name,
                maxLines = 1,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

// List of anime in current category
// (watching, planned, watched, dropped)
@Composable
private fun DataAnimeList(
    navController: NavController,
    daoViewModel: DaoViewModel,
    modifier: Modifier,
    isSortedAlphabetically: MutableState<Boolean>,
    isSortedByScore: MutableState<Boolean>,
    isSortedByUsers: MutableState<Boolean>,
    isAiredFrom: MutableState<Boolean>,
    category: String,
    searchText: String,
    type: MutableState<String?>

) {
    val currentAnimeInSection by daoViewModel.getAnimeInCategory(
        category = category,
        searchText = searchText,
        isSortedByScore = isSortedByScore.value,
        isAiredFrom = isAiredFrom.value,
        isSortedAlphabetically = isSortedAlphabetically.value,
        isSortedByUsers = isSortedByUsers.value,
        type = type.value ?: ""
    )
        .collectAsStateWithLifecycle(initialValue = emptyList())
//    val coroutineScope = rememberCoroutineScope()
//    val offsetY  =  remember { Animatable(0f) }
//    val draggableState = rememberDraggableState{ delta ->
//        coroutineScope.launch {
//            offsetY.snapTo(offsetY.value + delta)
//        }
//    }

    Column(
        modifier = modifier
            .fillMaxSize(1f)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 265.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(currentAnimeInSection) { animeItem ->
                DataScreenCardBox(
                    animeItem = animeItem,
                    navController = navController,
                    modifier = modifier
//                        .draggable(state = draggableState, orientation = Orientation.Horizontal)
                )
            }
        }
    }
}


@Composable
private fun FavoriteList(
    navController: NavController,
    daoViewModel: DaoViewModel,
    modifier: Modifier,
    isSortedAlphabetically: MutableState<Boolean>,
    isSortedByScore: MutableState<Boolean>,
    isSortedByUsers: MutableState<Boolean>,
    isAiredFrom: MutableState<Boolean>,
    searchText: String,
    type: MutableState<String?>

) {
    val currentAnimeInSection by daoViewModel.getAnimeInFavorite(
        searchText = searchText,
        isSortedByScore = isSortedByScore.value,
        isAiredFrom = isAiredFrom.value,
        isSortedAlphabetically = isSortedAlphabetically.value,
        isSortedByUsers = isSortedByUsers.value,
        type = type.value ?: ""
    )
        .collectAsStateWithLifecycle(initialValue = emptyList())

//    }

    Column(
        modifier = modifier
            .fillMaxSize(1f)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 265.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(currentAnimeInSection) { favoriteItem ->
                FavoriteScreenCardBox(
                    favoriteItem = favoriteItem,
                    navController = navController,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun ShowCharacter(
    navController: NavController,
    modifier: Modifier,
    daoViewModel: DaoViewModel
) {

    val animeListState by daoViewModel.getAllCharacters().collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    Column(
        modifier = modifier
            .fillMaxSize(1f)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 265.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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

@Composable
private fun CharacterCardBox(
    characterItem: CharacterItem,
    navController: NavController,
    modifier: Modifier
) {
    val painter = rememberAsyncImagePainter(model = characterItem.image)
    Box(modifier = modifier.clickable {
        characterItem.id?.let {
            navController.navigate("detail_on_character/${it}")
        }
    }) {
        Row {
            Column(modifier = modifier.padding(vertical = 2.dp, horizontal = 10.dp)) {
                Image(
                    painter = painter,
                    contentDescription = characterItem.name,
                    modifier = modifier
                        .size(100.dp, 150.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.FillBounds
                )

            }
            Column {
                Text(
                    text = characterItem.name,
                    modifier = modifier,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                if (!characterItem.anime.isNullOrEmpty()) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth(0.7f)
                            .height(60.dp)
                    ) {
                        Text(
                            text = "From: ${characterItem.anime}",
                            modifier = Modifier,
                            minLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun ShowPerson(
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier
) {

    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    val personList by daoViewModel.getAllPeople()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize(1f)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 265.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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


@Composable
private fun PersonCardBox(
    personItem: PersonItem,
    navController: NavController,
    modifier: Modifier
) {
    val painter = rememberAsyncImagePainter(model = personItem.image)
    val familyName =
        if (personItem.familyName.isNullOrEmpty()) "Family name: N/A" else "Family name: ${personItem.familyName}"
    val givenName =
        if (personItem.givenName.isNullOrEmpty()) "Given name: N/A" else "Given name: ${personItem.givenName}"
    Box(modifier = modifier.clickable {
        personItem.id?.let {
            navController.navigate("detail_on_staff/${it}")
        }
    }) {
        Row {
            Column(modifier = modifier.padding(vertical = 2.dp, horizontal = 10.dp)) {
                Image(
                    painter = painter,
                    contentDescription = personItem.name,
                    modifier = modifier
                        .size(100.dp, 150.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.FillBounds
                )

            }
            Column {
                Text(
                    text = personItem.name,
                    modifier = modifier,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = familyName,
                    modifier = Modifier,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = givenName,
                    modifier = Modifier,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}


// Function for showing a single
// anime-card with "+" button

@Composable
private fun DataScreenCardBox(
    animeItem: AnimeItem,
    navController: NavController,
    modifier: Modifier
) {
    val painter = rememberAsyncImagePainter(model = animeItem.animeImage)

    val score = if (animeItem.score.isNullOrEmpty()) "N/A" else animeItem.score
    val scoredBy =
        if (animeItem.scored_by.isNullOrEmpty()) "0 users" else "${animeItem.scored_by} users"
    val status =
        if (animeItem.status.isNullOrEmpty()) "Status: N/A" else "Status: " + animeItem.status
    val rating =
        if (animeItem.rating.isNullOrEmpty()) "Rating: N/A" else "Rating: " + animeItem.rating

    Column(modifier = modifier.clickable {
        animeItem.id?.let {
            navigateToDetailScreen(navController, it)
        }
    }, verticalArrangement = Arrangement.SpaceBetween) {
        Row {
            Column(modifier = modifier.padding(vertical = 2.dp, horizontal = 10.dp)) {
                Image(
                    painter = painter,
                    contentDescription = animeItem.animeImage,
                    modifier = modifier
                        .size(100.dp, 150.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.FillBounds
                )

            }
            Column {
                Text(
                    text = animeItem.animeName,
                    modifier = modifier,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp, color = MaterialTheme.colorScheme.onPrimary
                )
//                Text(
//                    text = animeItem.airedFrom,
//                    modifier = modifier,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 25.sp
//                )
                if (!animeItem.secondName.isNullOrEmpty()) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth(0.7f)
                            .height(60.dp)
                    ) {
                        Text(
                            text = animeItem.secondName,
                            modifier = Modifier,
                            minLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(15.dp)
        ) {

        }
        Row {
            Column(
                modifier = modifier
                    .fillMaxWidth(0.3f)
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = modifier
                            .clip(RoundedCornerShape(6.dp))
                            .height(25.dp)
                            .width(80.dp)
                            .background(MaterialTheme.colorScheme.secondary),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = "score",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Column(
                    modifier = modifier
                        .fillMaxWidth(0.7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = score,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp, color = MaterialTheme.colorScheme.onPrimary

                    )
                    Text(
                        text = scoredBy,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimary

                    )
                }
            }
            Column(modifier = modifier.fillMaxWidth()) {
                Text(
                    text = status,
                    modifier = Modifier,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = rating,
                    modifier = Modifier,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary
                )

            }

        }
    }
}


@Composable
private fun FavoriteScreenCardBox(
    favoriteItem: FavoriteItem,
    navController: NavController,
    modifier: Modifier
) {
    val painter = rememberAsyncImagePainter(model = favoriteItem.animeImage)

    val score = if (favoriteItem.score.isNullOrEmpty()) "N/A" else favoriteItem.score
    val scoredBy =
        if (favoriteItem.scored_by.isNullOrEmpty()) "0 users" else "${favoriteItem.scored_by} users"
    val status =
        if (favoriteItem.status.isNullOrEmpty()) "Status: N/A" else "Status: " + favoriteItem.status
    val rating =
        if (favoriteItem.rating.isNullOrEmpty()) "Rating: N/A" else "Rating: " + favoriteItem.rating

    Column(modifier = modifier.clickable {
        favoriteItem.id?.let {
            navigateToDetailScreen(navController, it)
        }
    }, verticalArrangement = Arrangement.SpaceBetween) {
        Row {
            Column(modifier = modifier.padding(vertical = 2.dp, horizontal = 10.dp)) {
                Image(
                    painter = painter,
                    contentDescription = favoriteItem.animeImage,
                    modifier = modifier
                        .size(100.dp, 150.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.FillBounds
                )

            }
            Column {
                Text(
                    text = favoriteItem.animeName,
                    modifier = modifier,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp, color = MaterialTheme.colorScheme.onPrimary
                )
//                Text(
//                    text = animeItem.airedFrom,
//                    modifier = modifier,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 25.sp
//                )
                if (!favoriteItem.secondName.isNullOrEmpty()) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth(0.7f)
                            .height(60.dp)
                    ) {
                        Text(
                            text = favoriteItem.secondName,
                            modifier = Modifier,
                            minLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(15.dp)
        ) {

        }
        Row {
            Column(
                modifier = modifier
                    .fillMaxWidth(0.3f)
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = modifier
                            .clip(RoundedCornerShape(6.dp))
                            .height(25.dp)
                            .width(80.dp)
                            .background(MaterialTheme.colorScheme.secondary),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = "score",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Column(
                    modifier = modifier
                        .fillMaxWidth(0.7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = score,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp, color = MaterialTheme.colorScheme.onPrimary

                    )
                    Text(
                        text = scoredBy,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimary

                    )
                }
            }
            Column(modifier = modifier.fillMaxWidth()) {
                Text(
                    text = status,
                    modifier = Modifier,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = rating,
                    modifier = Modifier,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary
                )

            }

        }
    }
}

@Composable
private fun TwoSortingButtons(
    modifier: Modifier,
    svgImageLoader: ImageLoader,
    leftSortingMenu: MutableState<Boolean>,
    rightSortingMenu: MutableState<Boolean>,
    isSortedAlphabetically: MutableState<Boolean>,
    isSortedByScore: MutableState<Boolean>,
    isSortedByUsers: MutableState<Boolean>,
    isAiredFrom: MutableState<Boolean>,
    isTvSelected: MutableState<Boolean>,
    isMovieSelected: MutableState<Boolean>,
    isOvaSelected: MutableState<Boolean>,
    isSpecialSelected: MutableState<Boolean>,
    isOnaSelected: MutableState<Boolean>,
    isMusicSelected: MutableState<Boolean>,
    selectedType: MutableState<String?>
) {
    val backArrowFirstColor =
        if (isSystemInDarkTheme()) DarkBackArrowCastColor else BackArrowCastColor
    val backArrowSecondColor =
        if (isSystemInDarkTheme()) DarkBackArrowSecondCastColor else BackArrowSecondCastColor

    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.07f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = modifier
//                .fillMaxWidth()
                .background(backArrowFirstColor)
        ) {
            if (leftSortingMenu.value) {
                Text(
                    text = "All v",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    modifier = modifier
                        .padding(start = 20.dp)
                        .clickable {
                            leftSortingMenu.value = !leftSortingMenu.value
                        }, color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = "All >",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    modifier = modifier
                        .padding(start = 20.dp)
                        .clickable {
                            leftSortingMenu.value = !leftSortingMenu.value
                        }, color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Box(
                modifier = modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.02f)
                    .background(backArrowSecondColor)
            )
        }

        DropdownMenu(
            modifier = modifier
                .background(MaterialTheme.colorScheme.error),
            expanded = leftSortingMenu.value,
            onDismissRequest = { leftSortingMenu.value = false }) {
            DropdownMenuItem(
                text = {
                    Text(text = "TV", fontSize = 22.sp, color = MaterialTheme.colorScheme.onPrimary)
                },
                trailingIcon = {
                    if (isTvSelected.value) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.filledcircle, imageLoader = svgImageLoader
                            ),
                            contentDescription = null,
                            modifier = modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.circle, imageLoader = svgImageLoader
                            ),
                            contentDescription = null,
                            modifier = modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                },
                onClick = {
                    isTvSelected.value = !isTvSelected.value
                    if (isTvSelected.value) {
                        selectedType.value = "TV"
                    } else {
                        selectedType.value = null
                    }

                    isMovieSelected.value = false
                    isOnaSelected.value = false
                    isOvaSelected.value = false
                    isSpecialSelected.value = false
                    isMusicSelected.value = false

                })
            DropdownMenuItem(
                text = {
                    Text(
                        text = "ONA",
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                },
                trailingIcon = {
                    if (isOnaSelected.value) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.filledcircle, imageLoader = svgImageLoader
                            ),
                            contentDescription = null,
                            modifier = modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.circle, imageLoader = svgImageLoader
                            ),
                            contentDescription = null,
                            modifier = modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                },
                onClick = {
                    isOnaSelected.value = !isOnaSelected.value
                    if (isOnaSelected.value) {
                        selectedType.value = "ONA"
                    } else {
                        selectedType.value = null
                    }
                    isMovieSelected.value = false
                    isTvSelected.value = false
                    isOvaSelected.value = false
                    isSpecialSelected.value = false
                    isMusicSelected.value = false
                })
            DropdownMenuItem(
                text = {
                    Text(
                        text = "OVA",
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                },
                trailingIcon = {
                    if (isOvaSelected.value) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.filledcircle, imageLoader = svgImageLoader
                            ),
                            contentDescription = null,
                            modifier = modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.circle, imageLoader = svgImageLoader
                            ),
                            contentDescription = null,
                            modifier = modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                },
                onClick = {
                    isOvaSelected.value = !isOvaSelected.value

                    if (isOvaSelected.value) {
                        selectedType.value = "OVA"
                    } else {
                        selectedType.value = null
                    }
                    isMovieSelected.value = false
                    isTvSelected.value = false
                    isOnaSelected.value = false
                    isSpecialSelected.value = false
                    isMusicSelected.value = false
                })
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Movie",
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                },
                trailingIcon = {
                    if (isMovieSelected.value) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.filledcircle, imageLoader = svgImageLoader
                            ),
                            contentDescription = null,
                            modifier = modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.circle, imageLoader = svgImageLoader
                            ),
                            contentDescription = null,
                            modifier = modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                },
                onClick = {

                    isMovieSelected.value = !isMovieSelected.value

                    if (isMovieSelected.value) {
                        selectedType.value = "Movie"
                    } else {
                        selectedType.value = null
                    }
                    isOvaSelected.value = false
                    isTvSelected.value = false
                    isOnaSelected.value = false
                    isSpecialSelected.value = false
                    isMusicSelected.value = false
                })
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Special",
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                },
                trailingIcon = {
                    if (isSpecialSelected.value) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.filledcircle, imageLoader = svgImageLoader
                            ),
                            contentDescription = null,
                            modifier = modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.circle, imageLoader = svgImageLoader
                            ),
                            contentDescription = null,
                            modifier = modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                },
                onClick = {

                    isSpecialSelected.value = !isSpecialSelected.value

                    if (isSpecialSelected.value) {
                        selectedType.value = "Special"
                    } else {
                        selectedType.value = null
                    }
                    isOvaSelected.value = false
                    isMovieSelected.value = false
                    isTvSelected.value = false
                    isOnaSelected.value = false
                    isMusicSelected.value = false
                })
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Music",
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                },
                trailingIcon = {
                    if (isMusicSelected.value) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.filledcircle, imageLoader = svgImageLoader
                            ),
                            contentDescription = null,
                            modifier = modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.circle, imageLoader = svgImageLoader
                            ),
                            contentDescription = null,
                            modifier = modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                },
                onClick = {
                    isMusicSelected.value = !isMusicSelected.value

                    if (isMusicSelected.value) {
                        selectedType.value = "Music"
                    } else {
                        selectedType.value = null
                    }
                    isOvaSelected.value = false
                    isTvSelected.value = false
                    isOnaSelected.value = false
                    isMovieSelected.value = false
                    isSpecialSelected.value = false
                })

        }
        Column(
            modifier = modifier
                .padding(top = 10.dp, end = 10.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.drawable.threelines, imageLoader = svgImageLoader
                ),
                contentDescription = null,
                modifier = modifier
                    .size(22.dp)
                    .clickable {
                        rightSortingMenu.value = true
                    }, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )

            DropdownMenu(
                expanded = rightSortingMenu.value,
                onDismissRequest = { rightSortingMenu.value = false },
                modifier = modifier
                    .fillMaxWidth(0.58f)
                    .background(MaterialTheme.colorScheme.error)

            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Alphabetical",
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                    },
                    trailingIcon = {
                        if (isSortedAlphabetically.value) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = R.drawable.filledcircle, imageLoader = svgImageLoader
                                ),
                                contentDescription = null,
                                modifier = modifier.size(22.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = R.drawable.circle, imageLoader = svgImageLoader
                                ),
                                contentDescription = null,
                                modifier = modifier.size(22.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                        }
                    },
                    onClick = {
                        isSortedByScore.value = false
                        isSortedByUsers.value = false
                        isAiredFrom.value = false
                        isSortedAlphabetically.value = !isSortedAlphabetically.value
                    })
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Score",
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    trailingIcon = {
                        if (isSortedByScore.value) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = R.drawable.filledcircle, imageLoader = svgImageLoader
                                ),
                                contentDescription = null,
                                modifier = modifier.size(22.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = R.drawable.circle, imageLoader = svgImageLoader
                                ),
                                contentDescription = null,
                                modifier = modifier.size(22.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                        }
                    },
                    onClick = {
                        isSortedByUsers.value = false
                        isSortedAlphabetically.value = false
                        isAiredFrom.value = false
                        isSortedByScore.value = !isSortedByScore.value

                    })
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Users",
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    trailingIcon = {
                        if (isSortedByUsers.value) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = R.drawable.filledcircle, imageLoader = svgImageLoader
                                ),
                                contentDescription = null,
                                modifier = modifier.size(22.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = R.drawable.circle, imageLoader = svgImageLoader
                                ),
                                contentDescription = null,
                                modifier = modifier.size(22.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                        }
                    },
                    onClick = {
                        isSortedAlphabetically.value = false
                        isSortedByScore.value = false
                        isAiredFrom.value = false
                        isSortedByUsers.value = !isSortedByUsers.value

                    })
                DropdownMenuItem(
                    text = {

                        Text(
                            text = "Aired Start Date",
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                    },
                    trailingIcon = {
                        if (isAiredFrom.value) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = R.drawable.filledcircle, imageLoader = svgImageLoader
                                ),
                                contentDescription = null,
                                modifier = modifier.size(22.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = R.drawable.circle, imageLoader = svgImageLoader
                                ),
                                contentDescription = null,
                                modifier = modifier.size(22.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                        }
                    },
                    onClick = {
                        isSortedAlphabetically.value = false
                        isSortedByScore.value = false
                        isSortedByUsers.value = false
                        isAiredFrom.value = !isAiredFrom.value
                    })
            }
        }
    }
}