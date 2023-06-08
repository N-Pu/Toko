package com.example.animeapp.presentation.screens.detailScreen.mainPage


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.presentation.animations.LoadingAnimation
import com.example.animeapp.presentation.screens.detailScreen.sideContent.castList.DisplayCast
import com.example.animeapp.presentation.screens.detailScreen.mainPage.customVisuals.DisplayCustomGenreBoxes
import com.example.animeapp.presentation.screens.detailScreen.sideContent.staffList.DisplayStaff
import com.example.animeapp.domain.viewModel.CastInDetailScreenViewModel
import com.example.animeapp.domain.viewModel.DetailScreenViewModel
import com.example.animeapp.domain.viewModel.IdViewModel
import com.example.animeapp.domain.viewModel.StaffInDetailScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun ActivateDetailScreen(
    viewModelProvider: ViewModelProvider,
    navController: NavController,

    ) {

    val id by viewModelProvider[IdViewModel::class.java].mal_id.collectAsStateWithLifecycle()
    val isSearching by viewModelProvider[DetailScreenViewModel::class.java].isSearching.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = id) {
        withContext(Dispatchers.IO) {
            viewModelProvider[DetailScreenViewModel::class.java].onTapAnime(id)
            delay(300)
            viewModelProvider[StaffInDetailScreenViewModel::class.java].addStaffFromId(id)
            delay(300)
            viewModelProvider[CastInDetailScreenViewModel::class.java].addCastFromId(id)

        }
    }


    val collectDetailData =
        viewModelProvider[DetailScreenViewModel::class.java].animeDetails.collectAsStateWithLifecycle()
    val detailData = collectDetailData.value

    val collectStaffData =
        viewModelProvider[StaffInDetailScreenViewModel::class.java].staffList.collectAsStateWithLifecycle()
    val staffData = collectStaffData.value

    val collectCastData =
        viewModelProvider[CastInDetailScreenViewModel::class.java].castList.collectAsStateWithLifecycle()
    val castData = collectCastData.value


    if (isSearching.not()) {
        if (detailData != null) {
            val painterState =
                rememberAsyncImagePainter(model = detailData.images.jpg.large_image_url)

            Column(
                modifier = Modifier

                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                DisplayPicture(painter = painterState)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = detailData.title,

                    fontSize = 50.sp,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ), textAlign = TextAlign.Center
                )




                DisplayCustomGenreBoxes(genres = detailData.genres)



                ExpandableText(text = detailData.synopsis ?: "")

//                Text(text = detailData.synopsis ?: "")


                if (castData.isNotEmpty()) {
                    DisplayCast(
                        castList = castData, navController = navController,
                        viewModelProvider = viewModelProvider
                    )
                }




                if (staffData.isNotEmpty()) {
                    DisplayStaff(staffList = staffData, navController = navController)
                }



                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "background: " + detailData.background)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "broadcast: " + detailData.broadcast.string)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "duration: " + detailData.duration)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "episodes: " + detailData.episodes.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "favorites: " + detailData.favorites.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "members: " + detailData.members.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "popularity: " + detailData.popularity.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "rank: " + detailData.rank.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "rating: " + detailData.rating)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "season: " + detailData.season)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "source: " + detailData.source)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "status: " + detailData.status)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "title: " + detailData.title)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "english title: " + detailData.title_english)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "jap title: " + detailData.title_japanese)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "trailer url =" + detailData.trailer.url +
                            "id " + detailData.trailer.youtube_id
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "approved :" + detailData.type)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "url :" + detailData.url)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "year :" + detailData.year.toString())
                Spacer(modifier = Modifier.height(16.dp))


                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "title_synonyms", textDecoration = TextDecoration.Underline)
                detailData.title_synonyms.forEach { // well, okay
                    Text(text = it)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "demographics", textDecoration = TextDecoration.Underline)
                detailData.demographics.forEach { // idk
                    Text(text = it.name)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "licensors", textDecoration = TextDecoration.Underline)
                detailData.licensors.forEach { Text(text = it.name) } //  companies that produce this animes on english television
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "producers", textDecoration = TextDecoration.Underline)
                detailData.producers.forEach { // producer companies
                    Text(text = it.name)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "studios", textDecoration = TextDecoration.Underline)
                detailData.studios.forEach { //студии
                    Text(text = it.name)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "explicit_genres", textDecoration = TextDecoration.Underline)
                detailData.explicit_genres.forEach {
                    Text(text = it.name)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "themes", textDecoration = TextDecoration.Underline)
                detailData.themes.forEach {
                    Text(text = it.name)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "titles", textDecoration = TextDecoration.Underline)
                detailData.titles.forEach {
                    Text(text = it.title)
                }

                Spacer(modifier = Modifier.size(60.dp))

            }


        }
    } else {
        LoadingAnimation()
    }
}


@Composable
fun ExpandableText(text: String) {
    var expanded by remember { mutableStateOf(false) }
    val toggleExpanded: () -> Unit = { expanded = !expanded }

    val maxLines = if (expanded) Int.MAX_VALUE else 4

    Column {
        Text(
            text = text,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clickable(onClick = toggleExpanded),
            softWrap = true
        )

        // it does not work when you put 4 and it sometimes does not work in number 1
        if (text.count { it == '\n' } >= 1) {
            Row(modifier = Modifier.animateContentSize()) {
                if (expanded) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(onClick = toggleExpanded)
                            .align(Alignment.CenterVertically)
                            .fillMaxWidth()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(onClick = toggleExpanded)
                            .align(Alignment.CenterVertically)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}



