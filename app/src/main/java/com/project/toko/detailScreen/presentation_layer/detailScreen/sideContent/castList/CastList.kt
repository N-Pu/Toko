package com.project.toko.detailScreen.presentation_layer.detailScreen.sideContent.castList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.project.toko.core.presentation_layer.navigation.Screen
import com.project.toko.detailScreen.model.castModel.CastData
import com.project.toko.detailScreen.model.castModel.ImagesX
import com.project.toko.detailScreen.model.castModel.JpgX
import com.project.toko.detailScreen.model.castModel.Person
import com.project.toko.detailScreen.model.castModel.VoiceActor
import java.lang.Integer.min


@Composable
fun DisplayCast(
    castList: List<CastData>, navController: NavController, modifier: Modifier
) {

    if (castList.isNotEmpty()) {
        val castWithJapVoiceActors = hasJapVoiceActor(castList)
        val numCharacterAndActors =
            min(12, castList.size) // Количество персонажей для вывода (не более 12)
        Row(
            modifier = modifier
                .fillMaxWidth(1f)
                .padding(start = 20.dp, bottom = 15.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = "Cast", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold
                )
            }
            Column {
                Text(
                    text = " $numCharacterAndActors>",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        AddCast(
            castList = castWithJapVoiceActors,
            navController = navController,
            modifier = modifier,
            numCharacterAndActors = numCharacterAndActors
        )
    }
}

@Composable
private fun AddCast(
    castList: List<CastData>,
    navController: NavController,
    modifier: Modifier,
    numCharacterAndActors: Int
) {
    val numCards = (numCharacterAndActors + 2) / 3 // Определение количества карточек
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        repeat(numCards) { i ->
            val startIdx = i * 3
            val endIdx = min(startIdx + 3, numCharacterAndActors)

            Column(modifier = modifier.width(20.dp)) {
                // Пустая колонка для выравнивания
            }
            Column(
                modifier
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .width(320.dp)
                    .height(440.dp)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                for (j in startIdx until endIdx) {
                    // Выведите каждого персонажа из текущей карточки с индексом j
                    CurrentCast(
                        characterPainter = rememberAsyncImagePainter(model = castList[j].character.images.webp.image_url),
                        modifier = modifier,
                        personPainter = rememberAsyncImagePainter(model = castList[j].voice_actors[0].person.images.jpg.image_url),
                        voiceActor = castList[j].voice_actors[0],
                        data = castList[j],
                        navController = navController
                    )
                }
            }
        }
        Column(modifier = modifier.width(20.dp)) {
            // Пустая колонка для выравнивания
        }
        Column(
            modifier
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(5.dp))
                .width(120.dp)
                .height(440.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(modifier = modifier.size(100.dp)) {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(198, 198, 198))
                        .clickable {
                            navController.navigate(Screen.DetailOnCast.route) {}
                        }, contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "More cast",
                        modifier = modifier.width(140.dp)
                    )
                }
            }

            Text(
                text = "More Cast",
                textAlign = TextAlign.Left,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = modifier
            )
        }
        Column(
            modifier = modifier
                .width(30.dp)
                .background(Color.Red)
        ) {
            // Пустая колонка для выравнивания
        }
    }

}

@Composable
private fun CurrentCast(
    modifier: Modifier,
    personPainter: AsyncImagePainter,
    characterPainter: AsyncImagePainter,
    voiceActor: VoiceActor,
    data: CastData,
    navController: NavController
) {

    val customModifier = if (voiceActor.language == "") {
        modifier
    } else {
        modifier.clickable {
            navController.navigate("detail_on_staff/${voiceActor.person.mal_id}") {}
        }
    }

    Row(
        modifier = modifier.height(150.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.size(85.dp, 135.dp)
        ) {
            Image(
                painter = personPainter,
                contentDescription = "Voice actor : ${voiceActor.person.name}",
                modifier = customModifier
                    .width(70.dp)
                    .height(107.dp),
                contentScale = ContentScale.FillBounds
            )

        }
        Column(modifier = modifier.width(140.dp)) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = modifier
                    .fillMaxWidth(1f)
                    .padding(start = 5.dp)
            ) {
                Row {
                    Text(
                        text = voiceActor.person.name,
                        modifier = Modifier,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Row {
                    Text(
                        text = voiceActor.language,
                        modifier = Modifier,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 11.sp
                    )
                }
            }
            Row(modifier = modifier.height(40.dp)) {}
            Column(
                horizontalAlignment = Alignment.End,
                modifier = modifier
                    .fillMaxWidth(1f)
                    .padding(end = 5.dp, bottom = 5.dp)
            ) {
                Row {
                    Text(
                        text = data.character.name,
                        modifier = Modifier,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Row {
                    Text(
                        text = data.role,
                        modifier = Modifier,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 11.sp
                    )
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.size(85.dp, 135.dp)
        ) {
            Image(
                painter = characterPainter,
                contentDescription = "Character name: ${data.character.name}",
                modifier = Modifier
                    .width(70.dp)
                    .height(107.dp)
                    .clickable {
                        navController.navigate(route = "detail_on_character/${data.character.mal_id}") {
//                            popUpTo(Screen.Detail.route) {
//                                inclusive = true
//                            }
                        }
                    },
                contentScale = ContentScale.Fit // Масштабирование изображения, чтобы вмещалось в квадрат
            )
        }

    }
}

private fun hasJapVoiceActor(castList: List<CastData>): List<CastData> {
    return castList.map { data ->
        val japOrFirstVoiceActor = getJapOrFirstVoiceActor(data)
        CastData(
            data.character, data.role, listOf(japOrFirstVoiceActor)
        )
    }
}

private fun getJapOrFirstVoiceActor(data: CastData): VoiceActor {
    return if (data.voice_actors.isNotEmpty()) {
        data.voice_actors.firstOrNull { it.language == "Japanese" } ?: data.voice_actors[0]
    } else {
        VoiceActor(
            "", Person(
                ImagesX(
                    JpgX(
                        ""
                    )
                ), 0, "", ""
            )
        )
    }
}
