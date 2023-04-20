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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.presentation.secondScreen.CharactersList.ShowList
import com.example.animeapp.viewModel.DetailScreenViewModel


@Composable
fun ActivateDetailScreen(id: Int) {
    DetailScreenViewModel.onTapAnime(id)

    val viewModel = viewModel<DetailScreenViewModel>()
    val animeDetailData = viewModel.animeDetails.collectAsStateWithLifecycle()
    val data = animeDetailData.value


    if (data != null) {
        val painterState = rememberAsyncImagePainter(model = data.images.webp.large_image_url)

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
                // All primitive data types
                Text(text = data.synopsis ?: "")
            }
            item {
                ShowList(mal_id = id)
            }
            item {

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "background =" + data.background)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "broadcast =" + data.broadcast.string)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "duration =" + data.duration)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "episodes =" + data.episodes.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "favorites" + data.favorites.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "members =" + data.members.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "popularity" + data.popularity.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "rank" + data.rank.toString())
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "rating =" + data.rating)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "season =" + data.season)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "source =" + data.source)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "status =" + data.status)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "title =" + data.title)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "english title =" + data.title_english)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "jap title =" + data.title_japanese)
                Spacer(modifier = Modifier.height(16.dp))

//                ExoPlayerView(data.trailer.youtube_id)
                Text(
                    text = "trailer url =" + data.trailer.url +
                            "id " + data.trailer.youtube_id
                )

//                Box(modifier = Modifier.size(20.dp)) {
//                    PlayYoutubeVideo(youtubeId = data.trailer.youtube_id)
//                }


                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "approved =" + data.type)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "url =" + data.url)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "year =" + data.year.toString())

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = """List<title_synonyms> """)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }


    }
}
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    items(data.title_synonyms) {
//
//                        Text(text = "title_synonyms $it")
//                    }
//                }


//                Spacer(modifier = Modifier.height(16.dp))
//                Text(text = """List<Demographic> """)
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    items(data.demographics) {
//
////                        Text(text = "url " + it.url)
//                        Log.d("URL", it.url)
////                        Text(text = "type " + it.type)
//                        Text(text = "name " + it.name)
////                        Text(text = "mail id " + it.mal_id.toString())
//                    }
//
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(text = """List<ExplicitGenre> """)
//                Spacer(modifier = Modifier.height(16.dp))
//                LazyRow(
//                    modifier = Modifier
//
//                        .fillMaxSize(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    items(data.explicit_genres) {
////                        Text(text = "url " + it.url)
//                        Log.d("URL", it.url)
////                        Text(text = "type " + it.type)
//                        Text(text = "name " + it.name)
////                        Text(text = "mail id " + it.mal_id.toString())
//                    }
//
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(text = """List<Genre> """)
//                Spacer(modifier = Modifier.height(16.dp))
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    items(data.genres) {
////                        Text(text = "url " + it.url)
//                        Log.d("URL", it.url)
////                        Text(text = "type " + it.type)
//                        Text(text = "name " + it.name)
////                        Text(text = "mail id " + it.mal_id.toString())
//                    }
//
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(text = """List<Licensor> """)
//                Spacer(modifier = Modifier.height(16.dp))
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    items(data.licensors) {
////                        Text(text = "url " + it.url)
//                        Log.d("URL", it.url)
////                        Text(text = "type " + it.type)
//                        Text(text = "name " + it.name)
////                        Text(text = "mail id " + it.mal_id.toString())
//                    }
//
//                }
//
//
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(text = """List<Producer> """)
//                Spacer(modifier = Modifier.height(16.dp))
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    items(data.producers) {
//                       val painterState2 = rememberAsyncImagePainter(model = it.url)
//                        DisplayPicture(painter = painterState2)
//                        Log.d("URL", it.url)
////                        Text(text = "type " + it.type)
//                        Text(text = "name " + it.name)
////                        Text(text = "mail id " + it.mal_id.toString())
//                    }
//
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(text = """List<Studio> """)
//                Spacer(modifier = Modifier.height(16.dp))
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    items(data.studios) {
////                        Text(text = "url " + it.url)
//                        Log.d("URL", it.url)
////                        Text(text = "type " + it.type)
//                        Text(text = "name " + it.name)
////                        Text(text = "mail id " + it.mal_id.toString())
//                    }
//
//                }
//
//
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(text = """List<Theme> """)
//                Spacer(modifier = Modifier.height(16.dp))
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    items(data.themes) {
////                        Text(text = "url " + it.url)
//                        Log.d("URL", it.url)
////                        Text(text = "type " + it.type)
//                        Text(text = it.name)
////                        Text(text = "mail id " + it.mal_id.toString())
//                    }
//
//                }
//
//
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(text = """List<titles> """)
//                Spacer(modifier = Modifier.height(16.dp))
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    items(data.titles) {
//                        Text(text = "title " + it.title)
//                        Log.d("URL", it.title)
////                        Text(text = "type " + it.type)
//
//                    }