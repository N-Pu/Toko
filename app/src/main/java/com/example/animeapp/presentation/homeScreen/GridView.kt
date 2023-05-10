package com.example.animeapp.presentation.homeScreen


import HomeScreenViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.R
import com.example.animeapp.domain.searchModel.Data
import com.example.animeapp.presentation.theme.LightYellow
import com.example.animeapp.viewModel.IdViewModel
import java.util.Locale


@Composable
fun GridAdder(
    listData: List<Data>,
    searchViewModel: HomeScreenViewModel,
    navController: NavHostController,
    idViewModel: IdViewModel,

    ) {


    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(listData) { _, anime ->
            AnimeCardBox(anime = anime, navController, idViewModel = idViewModel)

        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimeCardBox(
    anime: Data,
    navController: NavController,
    idViewModel: IdViewModel,
) {

    var isVisible by remember {
        mutableStateOf(false)
    }



    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            initialAlpha = 0.0f,
            animationSpec = TweenSpec(durationMillis = 500)
        ),
        exit = fadeOut(
            animationSpec = TweenSpec(durationMillis = 500)

        )
    ) {
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable {
                    idViewModel.setId(anime.mal_id)
                    navigateToDetailScreen(navController, idViewModel.getId())
                },
            colors = CardDefaults.cardColors(containerColor = LightYellow),
            shape = RectangleShape,
        ) {
            val painter = rememberAsyncImagePainter(model = anime.images.webp.image_url)
            Box {
                // Coil image loader
                Image(
                    painter = painter,
                    contentDescription = "Images for each Anime",
                    modifier = Modifier
                        .aspectRatio(9f / 11f),
                    contentScale = ContentScale.FillBounds
                )

                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                ) {
                    Box(
                        modifier = Modifier.size(45.dp),
                        contentAlignment = Alignment.Center
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
                            tint = MaterialTheme.colorScheme.secondary, modifier = Modifier
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

                AddFavorites()

//                IconButton(
//                    onClick = { /*TODO*/ },
//                    modifier = Modifier.align(Alignment.BottomEnd),
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.AddCircle,
//                        tint = MaterialTheme.colorScheme.primary,
//                        contentDescription = "Add anime to db"
//                    )
//                }
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

    LaunchedEffect(anime) {
        isVisible = true
    }
}


//fun scoreRecreater(score: Float): String {
//
//}

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
                    .clickable {
                    },
                colors = CardDefaults.cardColors(containerColor = LightYellow),
                shape = RectangleShape,
            ) {
                val painter = rememberAsyncImagePainter(model = R.drawable.kurisu)
                Box(modifier = Modifier.fillMaxSize()) {
                    // Coil image loader
                    Image(
                        painter = painter,
                        contentDescription = "Images for each Anime",
                        modifier = Modifier
                            .aspectRatio(9f / 11f),
                        contentScale = ContentScale.FillBounds
                    )

                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                    ) {
                        Box(
                            modifier = Modifier.size(45.dp),
                            contentAlignment = Alignment.Center
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
                                tint = MaterialTheme.colorScheme.primary, modifier = Modifier
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

//                    IconButton(
//                        onClick = { /*TODO*/ },
//                        modifier = Modifier.align(Alignment.BottomEnd),
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.AddCircle,
//                            tint = MaterialTheme.colorScheme.primary,
//                            contentDescription = "Add anime to db"
//                        )
//                    }

                    AddFavorites()

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


@Preview
@Composable
fun DropDownMenu() {

    AddFavorites()

}

@Composable
fun AddFavorites() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Planned", "Watching", "Watched", "Dropped")

    Box(modifier = Modifier.offset(130.dp, 170.dp)) {
        IconButton(onClick = { expanded = true }, modifier = Modifier.align(Alignment.BottomEnd)) {
            Icon(
                Icons.Default.AddCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.inversePrimary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }, modifier = Modifier.background(LightYellow)
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    // Handle click on item
//                    expanded = false
                }, text = {
                    Text(text = item)
                })
            }
        }
    }
}
