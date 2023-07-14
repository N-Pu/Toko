package com.example.animeapp.presentation.screens.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import com.example.animeapp.domain.models.newAnimeSearchModel.Data
import com.example.animeapp.presentation.screens.detailScreen.mainPage.customVisuals.DisplayCustomGenreBoxes

@Composable
fun CustomDialog(
    onDismiss: () -> Unit, data: Data, navController: NavController, painter: AsyncImagePainter, modifier: Modifier
) {
    Dialog(onDismissRequest = {
        onDismiss.invoke()
    }) {
        Card(modifier = modifier.size(550.dp)) {
            Column(
                modifier = modifier
                    .height(300.dp)
                    .fillMaxWidth()
            ) {
                DisplayDialogPicture(painter, 400f, data.mal_id, navController, modifier = modifier)

            }
            LazyColumn(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .height(300.dp)
                    .fillMaxWidth()
            ) {
                item {
                    Text(text = data.title, fontSize = 40.sp)
                    Text(text = "English title: " + data.title_english)
                    Text(text = "Score: " + data.score)
                    Text(text = "Users: " + data.scored_by)
                    Text(text = "Ranked: " + data.rank)
                    Text(text = "Popularity: " + data.popularity)
                    Text(text = "Members: " + data.members)
                    Text(text = "Season: " + data.season)
                    Text(text = "Type: " + data.type)
                    Box(
                        modifier = modifier
                            .height(
                                20.dp
                            )
                            .fillMaxWidth()
                    ) {
                        data.studios.forEach { studio ->
                            Text(text = "Studio: " + studio.name)
                        }
                    }

                    Text(text = "Episodes: " + data.episodes)
                    Text(text = "Status: " + data.status)
                    Text(text = "Rating: " + data.rating)
                    DisplayCustomGenreBoxes(genres = data.genres, modifier = modifier)
                }
                item {
                    Box(
                        modifier = Modifier
                            .height(
                                100.dp
                            )
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Synopsis: " + data.synopsis,
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun DisplayDialogPicture(
    painter: AsyncImagePainter, height: Float, mal_id: Int, navController: NavController, modifier: Modifier
) {

    Image(
        painter = painter,
        contentDescription = "Big anime picture",

        contentScale = ContentScale.Fit,
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .clickable {
                navigateToDetailScreen(navController, mal_id)
            },
        alignment = Alignment.TopCenter,
    )
}