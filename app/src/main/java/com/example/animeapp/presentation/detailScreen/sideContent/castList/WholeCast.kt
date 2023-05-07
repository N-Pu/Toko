package com.example.animeapp.presentation.detailScreen.sideContent.castList

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.domain.castModel.Character
import com.example.animeapp.domain.castModel.Person
import com.example.animeapp.presentation.navigation.DetailOnCast
import com.example.animeapp.presentation.navigation.Screen
import com.example.animeapp.viewModel.CastInDetailScreenViewModel

@Composable
fun ShowWholeCast(navController: NavController, viewModelProvider: ViewModelProvider) {


    val castState =
        viewModelProvider[CastInDetailScreenViewModel::class.java].castList.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
//            .background(Color.White)
    ) {
        castState.value.forEach { data ->
            item {
                Row(
//                    modifier = Modifier.horizontalScroll(rememberScrollState())
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.error)
                            .weight(0.5f)
                    ) {
                        SingleCharacterMember(
                            character = data.character,
                            role = data.role,
                            navController = navController
                        )
                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.primary)
                            .weight(0.5f)
                    ) {
                        data.voice_actors.forEach { voiceActor ->
                            SinglePersonMember(
                                person = voiceActor.person,
                                language = voiceActor.language,
                                navController = navController
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                        }

                    }

                }


            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Black)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PrevWholeCast() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
//            .background(Color.White)
    ) {
//        Box(modifier = Modifier.fillMaxSize()){}
        item {
            Row(
                modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()

                        .weight(0.5f)
                ) {
                    Card(
                        modifier = Modifier
                            .size(140.dp)
                            .aspectRatio(9f / 11f)
                            .offset(x = 10.dp, y = 20.dp)
                    ) {

                    }

                    Column(
                        modifier = Modifier
                            .offset(x = 20.dp, y = 35.dp)
                            .width(140.dp)
                            .background(Color.Red)
                    ) {
                        Text(text = "name of character", textAlign = TextAlign.Center)
                    }
                }
                Spacer(modifier = Modifier.size(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.5f)
                ) {
                    Card(
                        modifier = Modifier
                            .size(140.dp)
                            .aspectRatio(9f / 11f)
                            .offset(x = 30.dp, y = 20.dp)
                            .background(Color.Blue)
                    ) {

                    }
                    Column(
                        modifier = Modifier
                            .offset(x = 50.dp, y = 35.dp)
                            .width(140.dp)
                    ) {
                        Text(text = "name of person", textAlign = TextAlign.Center)
                    }
                }

            }

        }


    }


}


@Composable
fun SingleCharacterMember(character: Character, role: String, navController: NavController) {
//    character.name
//    character.images.jpg
//    character.mal_id
//    character.url

    val painter = rememberAsyncImagePainter(model = character.images.jpg.image_url)
    Card(modifier = Modifier
        .size(123.dp, 150.dp)
        .aspectRatio(9f / 11f)
        .clickable {
            Log.d("character id ->", character.mal_id.toString())

            navController.navigate(route = "detail_on_character/${character.mal_id}") {
                launchSingleTop = true
                popUpTo(DetailOnCast.value) {
                    saveState = true
                }
                restoreState = true
            }
        }) {
        Image(
            painter = painter,
            contentDescription = "Character name: ${character.name}",
            modifier = Modifier.aspectRatio(9f / 11f),
            contentScale = ContentScale.Crop
        )


    }
    Spacer(modifier = Modifier.size(30.dp))
    Text(text = character.name, textAlign = TextAlign.Center, color = Color.Black)
    Spacer(modifier = Modifier.size(20.dp))
    Text(text = role, textAlign = TextAlign.Center, color = Color.Black)

}


@Composable
fun SinglePersonMember(person: Person, language: String, navController: NavController) {
//    person.name
//    person.images.jpg
//    person.mal_id
//    person.url

    val painter = rememberAsyncImagePainter(model = person.images.jpg.image_url)
    Card(modifier = Modifier
        .size(123.dp, 150.dp)
        .offset(x = (0).dp, y = 35.dp)
        .clickable {
            navController.navigate(route = "detail_on_staff/${person.mal_id}") {

                popUpTo(Screen.Detail.route) {
                    inclusive = true
                }

            }
            Log.d("person id ->", person.mal_id.toString())
        }) {
        Image(
            painter = painter,
            contentDescription = "Character name: ${person.name}",
            modifier = Modifier.aspectRatio(9f / 11f),
            contentScale = ContentScale.Crop
        )


    }
    Spacer(modifier = Modifier.size(30.dp))
    Text(text = person.name, textAlign = TextAlign.Center, color = Color.Black)
    Spacer(modifier = Modifier.size(20.dp))
    Text(text = language, textAlign = TextAlign.Center, color = Color.Black)

}

