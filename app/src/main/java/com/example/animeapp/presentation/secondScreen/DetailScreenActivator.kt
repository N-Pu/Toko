package com.example.animeapp.presentation.secondScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.presentation.secondScreen.charactersList.DisplayCast
import com.example.animeapp.presentation.secondScreen.customVisuals.DisplayCustomGenreBoxes
import com.example.animeapp.presentation.secondScreen.staffList.DisplayStaffList
import com.example.animeapp.viewModel.CharactersViewModel
import com.example.animeapp.viewModel.DetailScreenViewModel
import com.example.animeapp.viewModel.IdViewModel
import com.example.animeapp.viewModel.StaffViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun ActivateDetailScreen(
    viewModelProvider: ViewModelProvider
) {


    val id by viewModelProvider[IdViewModel::class.java].mal_id.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = id) {
        withContext(Dispatchers.IO) {
            viewModelProvider[DetailScreenViewModel::class.java].onTapAnime(id)
            viewModelProvider[StaffViewModel::class.java].addStaffFromId(id)
            viewModelProvider[CharactersViewModel::class.java].addCharacterAndSeyu(id)
        }
    }


    val collectDetailData =
        viewModelProvider[DetailScreenViewModel::class.java].animeDetails.collectAsStateWithLifecycle()
    val detailData = collectDetailData.value

    val collectStaffData =
        viewModelProvider[StaffViewModel::class.java].staffList.collectAsStateWithLifecycle()
    val staffData = collectStaffData.value

    val collectCastData =
        viewModelProvider[CharactersViewModel::class.java].charactersList.collectAsStateWithLifecycle()
    val castData = collectCastData.value



    if (detailData != null) {
        val painterState = rememberAsyncImagePainter(model = detailData.images.jpg.large_image_url)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            item {
                DisplayPicture(painter = painterState)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = detailData.title,
                    fontSize = 50.sp,
                    modifier = Modifier,
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }

            item {

                DisplayCustomGenreBoxes(genres = detailData.genres)
            }


            item {
                Text(text = detailData.synopsis ?: "")
            }



            item {

                DisplayCast(castList = castData)
            }


            item {
                DisplayStaffList(staffList = staffData)
            }

            item {

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
            }


            item {
                Text(
                    text = "trailer url =" + detailData.trailer.url +
                            "id " + detailData.trailer.youtube_id
                )
            }

            item {

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "approved :" + detailData.type)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "url :" + detailData.url)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "year :" + detailData.year.toString())
                Spacer(modifier = Modifier.height(16.dp))

            }


            item {
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
            }
        }


    }
}

