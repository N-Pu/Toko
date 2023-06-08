package com.example.animeapp.presentation.screens.homeScreen


import HomeScreenViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.animeapp.presentation.animations.LoadingAnimation
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavHostController, viewModelProvider: ViewModelProvider
) {

    val viewModel = viewModelProvider[HomeScreenViewModel::class.java]
    val searchText by remember { viewModel.searchText }.collectAsStateWithLifecycle()
    val isSearching by remember { viewModel.isPerformingSearch }.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    Column {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchText,
                onValueChange = viewModel::onSearchTextChange,
                modifier = Modifier.weight(1f), // Изменено на использование weight для занимания доступного пространства
                label = {
                    Icon(Icons.Filled.Search, "Search Icon")
                },
                shape = RoundedCornerShape(36.dp),
                singleLine = true,
                placeholder = {
                    Text(text = "Searching anime...")
                },
                trailingIcon = {
                    DropDownGenres()
                }
            )
        }

        if (!isSearching) {
            GridAdder(
                navController = navController, viewModelProvider = viewModelProvider
            )
        } else{
            LoadingAnimation()
        }



        LaunchedEffect(key1 = searchText) {
            coroutineScope.launch {
                viewModel.onSearchTextChange(searchText)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DropDownGenres() {
    var isDropdownVisible by remember { mutableStateOf(false) }
    val verticalScrollState = rememberScrollState()
    val genreStates = remember { mutableStateListOf<MutableState<Boolean>>() }

    IconButton(onClick = { isDropdownVisible = true }, modifier = Modifier.size(65.dp)) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "GenreButton",
            modifier = Modifier.size(30.dp)
        )
    }

    DropdownMenu(
        expanded = isDropdownVisible,
        onDismissRequest = { isDropdownVisible = false },
    ) {
        Box(Modifier.size(400.dp)) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(verticalScrollState)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                content = {
                    val genres = getGenres()
                    if (genreStates.size != genres.size) {
                        genreStates.clear()
                        genreStates.addAll(genres.map { mutableStateOf(false) })
                    }

                    genres.forEachIndexed { index, genre ->
                        GenreButton(
                            genre = genre.name,
                            onClick = {
                                genreStates[index].value = !genreStates[index].value

                            },
                            isTouched = genreStates[index].value
                        )
                        Spacer(
                            modifier = Modifier
                                .width(8.dp)
                                .height(50.dp)
                        )
                    }
                }
            )
        }
    }
}


@Composable
fun GenreButton(genre: String, onClick: () -> Unit, isTouched: Boolean) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isTouched) Color.Red else Color.Cyan)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
        content = {
            Text(
                text = genre,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    )
}


@Preview
@Composable
fun PreviewGenreButton() {
//    GenreButton(genre = "Drama", onClick = {
//    })
}

data class Genre(val name: String, val number: Int)


fun getGenres(): List<Genre> {
    return listOf(
        Genre("Action", 1),
        Genre("Adventure", 2),
        Genre("Racing", 3),
        Genre("Comedy", 4),
        Genre("Avant Garde", 5),
        Genre("Mythology", 6),
        Genre("Mystery", 7),
        Genre("Drama", 8),
        Genre("Ecchi", 9),
        Genre("Fantasy", 10),
        Genre("Strategy Game", 11),
        Genre("Hentai", 12),
        Genre("Historical", 13),
        Genre("Horror", 14),
        Genre("Kids", 15),
        Genre("Martial Arts", 17),
        Genre("Mecha", 18),
        Genre("Music", 19),
        Genre("Parody", 20),
        Genre("Samurai", 21),
        Genre("Romance", 22),
        Genre("School", 23),
        Genre("Sci-Fi", 24),
        Genre("Shoujo", 25),
        Genre("Girls Love", 26),
        Genre("Shounen", 27),
        Genre("Boys Love", 28),
        Genre("Space", 29),
        Genre("Sports", 30),
        Genre("Super Power", 31),
        Genre("Vampire", 32),
        Genre("Harem", 35),
        Genre("Slice of Life", 36),
        Genre("Supernatural", 37),
        Genre("Military", 38),
        Genre("Detective", 39),
        Genre("Psychological", 40),
        Genre("Suspense", 41),
        Genre("Seinen", 42),
        Genre("Josei", 43),
        Genre("Award Winning", 46),
        Genre("Gourmet", 47),
        Genre("Workplace", 48),
        Genre("Erotica", 49),
        Genre("Adult Cast", 50),
        Genre("Anthropomorphic", 51),
        Genre("CGDCT", 52),
        Genre("Childcare", 53),
        Genre("Combat Sports", 54),
        Genre("Delinquents", 55),
        Genre("Educational", 56),
        Genre("Gag Humor", 57),
        Genre("Gore", 58),
        Genre("High Stakes Game", 59),
        Genre("Idols (Female)", 60),
        Genre("Idols (Male)", 61),
        Genre("Isekai", 62),
        Genre("Iyashikei", 63),
        Genre("Love Polygon", 64),
        Genre("Magical Sex Shift", 65),
        Genre("Mahou Shoujo", 66),
        Genre("Medical", 67),
        Genre("Organized Crime", 68),
        Genre("Otaku Culture", 69),
        Genre("Performing Arts", 70),
        Genre("Pets", 71),
        Genre("Reincarnation", 72),
        Genre("Reverse Harem", 73),
        Genre("Romantic Subtext", 74),
        Genre("Showbiz", 75),
        Genre("Survival", 76),
        Genre("Team Sports", 77),
        Genre("Time Travel", 78),
        Genre("Video Game", 79),
        Genre("Visual Arts", 80),
        Genre("Crossdressing", 81)
    )
}