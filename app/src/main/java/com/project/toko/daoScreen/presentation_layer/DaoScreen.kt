package com.project.toko.daoScreen.presentation_layer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.project.toko.R
import com.project.toko.characterDetailedScreen.dao.CharacterItem
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.homeScreen.presentation_layer.homeScreen.navigateToDetailScreen
import com.project.toko.core.presentation_layer.theme.SearchBarColor
import com.project.toko.core.presentation_layer.theme.favoriteTopBarColors
import com.project.toko.core.presentation_layer.theme.iconColorInSearchPanel
import com.project.toko.core.presentation_layer.theme.threeLines
import com.project.toko.daoScreen.daoViewModel.DaoViewModel
import com.project.toko.daoScreen.model.AnimeListType
import com.project.toko.personDetailedScreen.dao.PersonItem


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
    val scrollState = rememberLazyGridState()
    val arrayOfEntries = AnimeListType.values()
    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    val searchText by daoViewModel.searchText.collectAsStateWithLifecycle()
    val sortingMenu = remember {
        mutableStateOf(false)
    }
//    val currentCategory = daoViewModel.currentCategory.value

    Column(
        modifier = modifier
            .fillMaxWidth(1f)
    ) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                .height(140.dp)
                .shadow(20.dp)
                .fillMaxWidth(1f)
                .background(Color.White)
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
                    alpha = 0.8f
                )
            }
            Row(
                modifier = modifier
                    .wrapContentSize()
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SearchBarColor),
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedTextField(
                    placeholder = { Text(text = "Search...") },
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
        TwoSortingButtons(
            modifier = modifier,
            svgImageLoader = svgImageLoader,
            sortingMenu = sortingMenu,
            daoViewModel = daoViewModel,
            selectedListType = selectedListType
//            currentCategory = currentCategory
        )
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
                    daoViewModel = daoViewModel,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.PLANNED -> FavoriteAnimeList(
                    category = AnimeListType.PLANNED.route,
                    navController = navController,
                    daoViewModel = daoViewModel,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.COMPLETED -> FavoriteAnimeList(
                    category = AnimeListType.COMPLETED.route,
                    navController = navController,
                    daoViewModel = daoViewModel,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.DROPPED -> FavoriteAnimeList(
                    category = AnimeListType.DROPPED.route,
                    navController = navController,
                    daoViewModel = daoViewModel,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.FAVORITE -> FavoriteAnimeList(
                    category = AnimeListType.FAVORITE.route,
                    navController = navController,
                    daoViewModel = daoViewModel,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.PERSON -> ShowPerson(
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    scrollState = scrollState,
                    modifier = modifier
                )

                AnimeListType.CHARACTER -> ShowCharacter(
                    navController = navController,
                    viewModelProvider = viewModelProvider,
                    scrollState = scrollState,
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

    val customModifier = if (selectedListType == listType) modifier
        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
        .clickable { onClick() }
        .fillMaxHeight(1f)
        .background(favoriteTopBarColors[colorIndex])
    else modifier
        .clickable { onClick() }
        .background(favoriteTopBarColors[colorIndex])

    Box(modifier = customModifier) {
        TextButton(
            modifier = modifier.padding(horizontal = 20.dp),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = Color.Transparent,
            )
        ) {
            Text(
                text = listType.name,
                maxLines = 1,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

// List of anime in current category
// (watching, planned, watched, dropped)
@Composable
private fun FavoriteAnimeList(
    category: String,
    navController: NavController,
    daoViewModel: DaoViewModel,
    scrollState: LazyGridState,
    modifier: Modifier,
//    isAlphabeticallySorted: MutableState<Boolean>

) {

    val animeListState by daoViewModel.getAnimeInCategory(category).collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
//    daoViewModel.currentCategory.value = category

    Column(
        modifier = modifier
            .fillMaxSize(1f)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 265.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(animeListState) { animeItem ->
                FavoriteScreenCardBox(
                    animeItem = animeItem,
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
    viewModelProvider: ViewModelProvider,
    scrollState: LazyGridState,
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
                    fontSize = 25.sp
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
                            fontSize = 16.sp
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
    scrollState: LazyGridState,
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
                    fontSize = 25.sp
                )
                Text(
                    text = familyName,
                    modifier = Modifier,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp
                )
                Text(
                    text = givenName,
                    modifier = Modifier,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp
                )
            }
        }
    }
}


// Function for showing a single
// anime-card with "+" button

@Composable
private fun FavoriteScreenCardBox(
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
                    fontSize = 25.sp
                )
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
                            fontSize = 16.sp
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
                            .background(LightGreen),
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
                        fontSize = 26.sp,

                        )
                    Text(
                        text = scoredBy,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 11.sp,

                        )
                }
            }
            Column(modifier = modifier.fillMaxWidth()) {
                Text(
                    text = status,
                    modifier = Modifier,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp
                )
                Text(
                    text = rating,
                    modifier = Modifier,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp
                )

            }

        }
    }
}


@Composable
private fun TwoSortingButtons(
    modifier: Modifier,
    svgImageLoader: ImageLoader,
    sortingMenu: MutableState<Boolean>,
    daoViewModel: DaoViewModel,
    selectedListType: AnimeListType
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.07f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.drawable.sidelinegreen, imageLoader = svgImageLoader
                ), contentDescription = null
            )
            Text(
                text = "All >",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                modifier = modifier.padding(start = 20.dp)
            )
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
                        sortingMenu.value = true
                    }
            )

            DropdownMenu(
                expanded = sortingMenu.value,
                onDismissRequest = { sortingMenu.value = false },
                modifier = modifier
                    .fillMaxWidth(0.58f)
                    .background(threeLines)

            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = "Alphabetical", fontSize = 22.sp, color = Color.White)

                    },
                    trailingIcon = {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.circle, imageLoader = svgImageLoader
                            ), contentDescription = null, modifier = modifier.size(22.dp)
                        )
                    },
                    onClick = {
                            daoViewModel.sortAnimeByName(selectedListType.name)
                    })
                DropdownMenuItem(
                    text = {

                        Text(text = "Score", fontSize = 22.sp, color = Color.White)
                    },
                    trailingIcon = {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.circle, imageLoader = svgImageLoader
                            ), contentDescription = null, modifier = modifier.size(22.dp)
                        )
                    },
                    onClick = { })
                DropdownMenuItem(
                    text = {

                        Text(text = "Air Start Date", fontSize = 22.sp, color = Color.White)

                    },
                    trailingIcon = {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.circle, imageLoader = svgImageLoader
                            ), contentDescription = null, modifier = modifier.size(22.dp)
                        )
                    },
                    onClick = { })
                DropdownMenuItem(
                    text = {

                        Text(text = "Last Updated", fontSize = 22.sp, color = Color.White)

                    },
                    trailingIcon = {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.filledcircle, imageLoader = svgImageLoader
                            ), contentDescription = null, modifier = modifier.size(22.dp)
                        )
                    },
                    onClick = {})
            }
        }
    }
}