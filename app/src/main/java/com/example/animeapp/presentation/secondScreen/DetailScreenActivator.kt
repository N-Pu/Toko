package com.example.animeapp.presentation.secondScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.presentation.secondScreen.StaffList.DisplayStaffList
import com.example.animeapp.presentation.secondScreen.charactersList.DisplayCast
import com.example.animeapp.presentation.secondScreen.customVisuals.DisplayCustomGenreBoxes
import com.example.animeapp.viewModel.DetailScreenViewModel


@Composable
fun ActivateDetailScreen(id: Int) {
    DetailScreenViewModel.onTapAnime(id)

    val viewModel = viewModel<DetailScreenViewModel>()
    val animeDetailData = viewModel.animeDetails.collectAsStateWithLifecycle()
    val data = animeDetailData.value


    if (data != null) {
        val painterState = rememberAsyncImagePainter(model = data.images.jpg.large_image_url)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            item {
                DisplayPicture(painter = painterState)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = data.title,
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

                DisplayCustomGenreBoxes(genres = data.genres)
            }


            item {
                Text(text = data.synopsis ?: "")
            }



            item {
                DisplayCast(mal_id = id)
            }


            item { DisplayStaffList(mal_id = id) }

            item {

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "background: " + data.background)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "broadcast: " + data.broadcast.string)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "duration: " + data.duration)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "episodes: " + data.episodes.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "favorites: " + data.favorites.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "members: " + data.members.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "popularity: " + data.popularity.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "rank: " + data.rank.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "rating: " + data.rating)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "season: " + data.season)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "source: " + data.source)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "status: " + data.status)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "title: " + data.title)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "english title: " + data.title_english)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "jap title: " + data.title_japanese)
                Spacer(modifier = Modifier.height(16.dp))
            }


            item {
                Text(
                    text = "trailer url =" + data.trailer.url +
                            "id " + data.trailer.youtube_id
                )
            }

            item {

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "approved :" + data.type)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "url :" + data.url)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "year :" + data.year.toString())
                Spacer(modifier = Modifier.height(16.dp))

            }


            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "title_synonyms", textDecoration = TextDecoration.Underline)
                data.title_synonyms.forEach { // well, okay
                    Text(text = it)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "demographics", textDecoration = TextDecoration.Underline)
                data.demographics.forEach { // idk
                    Text(text = it.name)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "licensors", textDecoration = TextDecoration.Underline)
                data.licensors.forEach { Text(text = it.name) } //  companies that produce this animes on english television
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "producers", textDecoration = TextDecoration.Underline)
                data.producers.forEach { // producer companies
                    Text(text = it.name)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "studios", textDecoration = TextDecoration.Underline)
                data.studios.forEach { //студии
                    Text(text = it.name)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "explicit_genres", textDecoration = TextDecoration.Underline)
                data.explicit_genres.forEach {
                    Text(text = it.name)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "themes", textDecoration = TextDecoration.Underline)
                data.themes.forEach {
                    Text(text = it.name)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "titles", textDecoration = TextDecoration.Underline)
                data.titles.forEach {
                    Text(text = it.title)
                }
            }
        }


    }
}


