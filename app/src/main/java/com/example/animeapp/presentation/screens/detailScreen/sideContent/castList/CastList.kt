package com.example.animeapp.presentation.screens.detailScreen.sideContent.castList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.domain.models.castModel.Data
import com.example.animeapp.domain.models.castModel.VoiceActor
import com.example.animeapp.presentation.navigation.DetailOnCast
import com.example.animeapp.presentation.navigation.Screen


@Composable
fun DisplayCast(
    castList: List<Data>,
    navController: NavController,
    viewModelProvider: ViewModelProvider
) {
    val castWithJapVoiceActors = hasJapVoiceActor(castList)

    Text(
        text = "Cast",
        textDecoration = TextDecoration.Underline,
    )

    AddCast(castList = castWithJapVoiceActors, navController = navController)
    Box(modifier = Modifier.fillMaxWidth(0.9f)) {
        Text(
            text = "More Cast",
            textAlign = TextAlign.Left,
            color = Color.Blue,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clickable {
                    navController.navigate(DetailOnCast.value) {
                        popUpTo(Screen.Detail.route) {
                            inclusive = true
                        }
                    }

                })
    }

}

@Composable
fun AddCast(castList: List<Data>, navController: NavController) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
    ) {

        castList.take(10).forEach { data ->
            val characterPainter =
                rememberAsyncImagePainter(model = data.character.images.webp.image_url)
            Column {

                Card(modifier = Modifier
                    .size(123.dp, 150.dp)
                    .shadow(
                        4.dp,
                        shape = MaterialTheme.shapes.large,
                        ambientColor = Color.Black.copy(alpha = 0.8f),
                    )
                    .clickable {
                        navController.navigate(route = "detail_on_character/${data.character.mal_id}") {
                            popUpTo(Screen.Detail.route) {
                                inclusive = true
                            }

                        }
                    }) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Image(
                            painter = characterPainter,
                            contentDescription = "Character name: ${data.character.name}",
                            modifier = Modifier.aspectRatio(9f / 11f),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            text = data.character.name,
                            color = Color.White,
                            modifier = Modifier
                                .shadow(
                                    4.dp,
                                    shape = MaterialTheme.shapes.large,
                                    ambientColor = Color.Black.copy(alpha = 0.8f),
                                )
                                .align(Alignment.BottomStart)
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                        Text(
                            text = data.role,
                            color = Color.White,
                            modifier = Modifier
                                .shadow(4.dp, shape = MaterialTheme.shapes.small)
                                .align(Alignment.TopStart)
                        )
                    }
                }

                data.voice_actors.forEach { voiceActor ->


                    val personPainter =
                        rememberAsyncImagePainter(model = voiceActor.person.images.jpg.image_url)

                    Card(modifier = Modifier
                        .size(123.dp, 150.dp)
                        .clickable {
                            navController.navigate(route = "detail_on_staff/${voiceActor.person.mal_id}") {

                                popUpTo(Screen.Detail.route) {
                                    inclusive = true
                                }

                            }
                        }) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomStart
                        ) {

                            Image(
                                painter = personPainter,
                                contentDescription = "Voice actor : ${voiceActor.person.name}",
                                modifier = Modifier.aspectRatio(9f / 11f),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = voiceActor.person.name,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 8.dp, bottom = 8.dp)
                                    .shadow(4.dp, shape = MaterialTheme.shapes.small),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.size(20.dp))
                            Text(
                                text = voiceActor.language,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(end = 8.dp, bottom = 8.dp)
                                    .shadow(4.dp, shape = MaterialTheme.shapes.small),
                                color = Color.White
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
        }

    }


}


fun hasJapVoiceActor(castList: List<Data>): List<Data> {
    return castList.mapNotNull { data ->
        val japOrFirstVoiceActor = getJapOrFirstVoiceActor(data)
        if (japOrFirstVoiceActor != null) {
            Data(
                data.character,
                data.role,
                listOf(japOrFirstVoiceActor)
            )
        } else {
            null
        }
    }
}

fun getJapOrFirstVoiceActor(data: Data): VoiceActor? {
    return data.voice_actors.firstOrNull { it.language == "Japanese" }
        ?: data.voice_actors.firstOrNull()
}


@Preview(showBackground = true)
@Composable
fun Prev() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .horizontalScroll(rememberScrollState())
            ) {
                Column {
                    Box(
                        Modifier
                            .size(200.dp)
                            .background(Color.Green)
                    ) {

                    }
                    Box(
                        Modifier
                            .size(200.dp)
                            .background(Color.Blue)
                    ) {

                    }

                }
                Column {
                    Box(
                        Modifier
                            .size(200.dp)
                            .background(Color.Red)
                    ) {

                    }
                    Box(
                        Modifier
                            .size(200.dp)
                            .background(Color.White)
                    ) {

                    }
                }
                Column {
                    Box(
                        Modifier
                            .size(200.dp)
                            .background(Color.Green)
                    ) {

                    }
                    Box(
                        Modifier
                            .size(200.dp)
                            .background(Color.Blue)
                    ) {

                    }

                }
                Column {
                    Box(
                        Modifier
                            .size(200.dp)
                            .background(Color.Red)
                    ) {

                    }
                    Box(
                        Modifier
                            .size(200.dp)
                            .background(Color.White)
                    ) {

                    }
                }
            }

        }
    }
}

