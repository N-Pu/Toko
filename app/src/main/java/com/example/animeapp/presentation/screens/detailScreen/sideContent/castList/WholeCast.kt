package com.example.animeapp.presentation.screens.detailScreen.sideContent.castList

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.domain.models.castModel.Character
import com.example.animeapp.domain.models.castModel.Person
import com.example.animeapp.presentation.navigation.DetailOnCast
import com.example.animeapp.presentation.navigation.Screen
import com.example.animeapp.domain.viewModel.CastInDetailScreenViewModel
import com.example.animeapp.presentation.theme.LightYellow


@Composable
fun ShowWholeCast(navController: NavController, viewModelProvider: ViewModelProvider) {

    val castList =
        viewModelProvider[CastInDetailScreenViewModel::class.java].castList.collectAsStateWithLifecycle()

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        itemsIndexed(castList.value) { _, data ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                SingleCharacterMember(
                    character = data.character,
                    role = data.role,
                    navController = navController
                )
                Divider(modifier = Modifier.size(10.dp))
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    data.voice_actors.forEach { voiceActor ->
                        SinglePersonMember(
                            person = voiceActor.person,
                            language = voiceActor.language,
                            navController = navController
                        )
                    }
                }
            }
            Divider(
                modifier = Modifier
                    .height(5.dp)
            )
            Divider(
                modifier = Modifier
                    .height(5.dp)
            )
        }
    }
}

@Composable
fun SingleCharacterMember(character: Character, role: String, navController: NavController) {
    Card(
        modifier = Modifier
            .size(123.dp, 150.dp)
            .shadow(
                elevation = 25.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Red,
                spotColor = Color.Blue
            )
            .clickable {
                navController.navigate("detail_on_character/${character.mal_id}") {
                    launchSingleTop = true
                    popUpTo(DetailOnCast.value) {
                        saveState = true
                    }
                    restoreState = true
                }
            }, border = BorderStroke(width = 2.dp, LightYellow)
    ) {
        Box(modifier = Modifier.background(LightYellow)) {
            Image(
                painter = rememberAsyncImagePainter(character.images.jpg.image_url),
                contentDescription = "Character name: ${character.name}",
                modifier = Modifier.aspectRatio(9f / 11f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(bottom = 16.dp)
            ) {

                Text(text = role, textAlign = TextAlign.Center, color = Color.White)
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Text(text = character.name, textAlign = TextAlign.Center, color = Color.White)

            }
        }
    }
}

@Composable
fun SinglePersonMember(person: Person, language: String, navController: NavController) {

    Card(modifier = Modifier
        .size(123.dp, 150.dp)
        .clickable {
            navController.navigate("detail_on_staff/${person.mal_id}") {
                popUpTo(Screen.Detail.route) {
                    inclusive = true
                }
            }
        }) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(person.images.jpg.image_url),
                contentDescription = "Character name: ${person.name}",
                modifier = Modifier.aspectRatio(9f / 11f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(bottom = 16.dp)
            ) {

                Text(
                    text = language,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .shadow(
                            4.dp,
                            shape = MaterialTheme.shapes.large,
                            ambientColor = Color.Black.copy(alpha = 0.8f)
                        ), maxLines = 1
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = person.name,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .shadow(
                            4.dp,
                            shape = MaterialTheme.shapes.large,
                            ambientColor = Color.Black.copy(alpha = 0.8f)
                        )
                )

            }

        }
    }
}

