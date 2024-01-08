package com.project.toko.characterDetailedScreen.presentation_layer.characterFull

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.project.toko.characterDetailedScreen.model.characterFullModel.Voice
import com.project.toko.core.presentation_layer.theme.evolventaBoldFamily

@Composable
fun ShowVoiceActors(modifier: Modifier, actors: List<Voice>, navController: NavController) {

    Row(modifier = modifier.padding(start = 20.dp)) {
        Text(text = "Voice Actors", fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = evolventaBoldFamily
        )
    }
    Spacer(modifier = Modifier.height(20.dp).fillMaxWidth())
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        repeat(actors.size) { i ->
            Row(modifier = modifier
                .clip(CardDefaults.shape)
                .clickable {
                    navController.navigate("detail_on_staff/${actors[i].person.mal_id}")
                }) {
                Column(
                    modifier = modifier
                        .height(140.dp)
                        .fillMaxWidth(0.28f)
                        .clip(CardDefaults.shape)
                    ,
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = actors[i].person.images.jpg.image_url),
                        contentDescription = actors[i].person.name,
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .fillMaxSize()
                        ,
                        alignment = Alignment.Center
                    )
                }
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = 0.dp, start = 10.dp)

                ) {
                    Text(text = actors[i].person.name, fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = evolventaBoldFamily)
                    Text(text = actors[i].language,
                        color = MaterialTheme.colorScheme.onPrimary)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

}



