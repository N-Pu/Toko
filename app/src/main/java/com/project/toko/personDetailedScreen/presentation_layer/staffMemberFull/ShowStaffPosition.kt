package com.project.toko.personDetailedScreen.presentation_layer.staffMemberFull

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.project.toko.homeScreen.presentation_layer.homeScreen.navigateToDetailScreen
import com.project.toko.personDetailedScreen.model.personFullModel.Anime

@Composable
fun ShowStaffPosition(modifier: Modifier, animes: List<Anime>, navController: NavController) {


    Row(modifier = modifier.padding(start = 20.dp, top = 10.dp)) {
        Text(text = "Anime Staff Positions", fontSize = 24.sp)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        repeat(animes.size) { i ->
            Row(modifier = modifier
                .clip(CardDefaults.shape)
                .clickable {
                    navigateToDetailScreen(navController, animes[i].anime.mal_id)
                }) {
                Column(
                    modifier = modifier
                        .height(140.dp)
                        .fillMaxWidth(0.28f)
                        .clip(CardDefaults.shape)
                    ,
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = animes[i].anime.images.jpg.large_image_url),
                        contentDescription = animes[i].anime.title,
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
                    Text(text = animes[i].position, fontSize = 18.sp, minLines = 1)
//                    Text(text = animes[i].position)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

}


@Preview(showSystemUi = true)
@Composable
fun PreviewTopSection() {
    Column(modifier = Modifier.padding(start = 20.dp, top = 10.dp)) {
        Text(text = "Voice Actors", fontSize = 24.sp)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        repeat(7) { i ->
            Row(Modifier.background(Color.Green)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .background(Color.Red)
                ) {
//                    Image(
//                        painter = rememberAsyncImagePainter(model = actors[i].person.images.jpg.image_url),
//                        contentDescription = actors[i].person.name,
//                        contentScale = ContentScale.Fit,
//                        modifier = modifier
//                            .height(200.dp)
//                            .width(120.dp)
//                            .clip(RoundedCornerShape(8.dp))
//                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp, start = 10.dp)
                        .background(Color.Blue)
                ) {
                    Text(text = "ctors[i].person.name", fontSize = 18.sp)
                    Text(text = "actors[i].language")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

