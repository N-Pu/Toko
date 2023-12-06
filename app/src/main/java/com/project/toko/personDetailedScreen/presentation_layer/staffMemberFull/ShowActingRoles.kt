package com.project.toko.personDetailedScreen.presentation_layer.staffMemberFull

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.project.toko.homeScreen.presentation_layer.homeScreen.navigateToDetailScreen
import com.project.toko.personDetailedScreen.model.personFullModel.Voice

@Composable
fun ShowAnimeRelated(modifier: Modifier, voices: List<Voice>, navController: NavController) {
    Row(modifier = modifier.padding(start = 20.dp, top = 10.dp)) {
        Text(text = "Voice Acting Roles", fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onPrimary)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        repeat(voices.size) { i ->
            Row(
                modifier = modifier
                    .clip(CardDefaults.shape)
            ) {
                Column(
                    modifier = modifier
                        .height(140.dp)
                        .fillMaxWidth(0.27f)
                        .clip(CardDefaults.shape),
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = voices[i].anime.images.jpg.large_image_url),
                        contentDescription = voices[i].anime.title,
                        contentScale = ContentScale.FillBounds,
                        modifier = modifier
                            .fillMaxSize()
                            .clickable {
                                navigateToDetailScreen(navController, voices[i].anime.mal_id)
                            },
                        alignment = Alignment.Center
                    )
                }
                Column(
                    modifier = modifier
                        .height(140.dp)
                        .fillMaxWidth(0.66f)
                        .padding(horizontal = 10.dp)

                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Text(text = voices[i].anime.title, fontSize = 16.sp, minLines = 1, maxLines = 3, overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onPrimary)

                    }
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Text(text = voices[i].role, maxLines = 1, overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onPrimary)
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom,
                        modifier = modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    ) {
                        Text(text = voices[i].character.name, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
                Column(
                    modifier = modifier
                        .height(140.dp)
                        .fillMaxWidth()
                        .clip(CardDefaults.shape),
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = voices[i].character.images.jpg.image_url),
                        contentDescription = voices[i].character.name,
                        contentScale = ContentScale.FillBounds,
                        modifier = modifier
                            .fillMaxSize()
                            .clickable {
                                navController.navigate(route = "detail_on_character/${voices[i].character.mal_id}")
                            },
                        alignment = Alignment.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

}