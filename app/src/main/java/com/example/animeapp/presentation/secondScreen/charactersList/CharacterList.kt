package com.example.animeapp.presentation.secondScreen.charactersList

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.domain.charactersModel.Data
import com.example.animeapp.domain.charactersModel.VoiceActor
import com.example.animeapp.viewModel.CharactersViewModel

@Composable
fun DisplayCast(mal_id: Int) {
    val viewModel = viewModel<CharactersViewModel>()
    viewModel.addCharacterAndSeyu(mal_id)
    val charactersList by viewModel.charactersList.collectAsStateWithLifecycle()

    Text(text = "Cast", textDecoration = TextDecoration.Underline)
    LazyRow(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(charactersList) { _, data ->
            val characterPainter =
                rememberAsyncImagePainter(model = data.character.images.webp.image_url)
            CharacterComponentsCard(characterData = data, characterPainter = characterPainter)
            Spacer(modifier = Modifier.size(20.dp))
            data.voice_actors.forEach {
                val personPainter =
                    rememberAsyncImagePainter(model = it.person.images.jpg.image_url)
                PersonComponentsCard(voiceActor = it, personPainter = personPainter)
                Spacer(modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.size(60.dp))
        }


    }
}


@Composable
fun PersonComponentsCard(voiceActor: VoiceActor, personPainter: AsyncImagePainter) {
    Card(modifier = Modifier.size(123.dp, 150.dp)) {
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

@Composable
fun CharacterComponentsCard(characterData: Data, characterPainter: AsyncImagePainter) {
    Card(modifier = Modifier.size(123.dp, 150.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
            Image(
                painter = characterPainter,
                contentDescription = "Character name: ${characterData.character.name}",
                modifier = Modifier.aspectRatio(9f / 11f),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = characterData.character.name,
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
                text = characterData.role,
                color = Color.White,
                modifier = Modifier
                    .shadow(4.dp, shape = MaterialTheme.shapes.small)
                    .align(Alignment.TopStart)
            )
        }
    }
}