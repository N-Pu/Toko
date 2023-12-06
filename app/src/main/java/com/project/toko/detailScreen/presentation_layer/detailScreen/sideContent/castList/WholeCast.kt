package com.project.toko.detailScreen.presentation_layer.detailScreen.sideContent.castList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.project.toko.R
import com.project.toko.core.presentation_layer.navigation.Screen
import com.project.toko.core.presentation_layer.theme.BackArrowCastColor
import com.project.toko.core.presentation_layer.theme.BackArrowSecondCastColor
import com.project.toko.core.presentation_layer.theme.DarkBackArrowCastColor
import com.project.toko.core.presentation_layer.theme.DarkBackArrowSecondCastColor
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.detailScreen.model.castModel.CastData
import com.project.toko.detailScreen.model.castModel.ImagesX
import com.project.toko.detailScreen.model.castModel.JpgX
import com.project.toko.detailScreen.model.castModel.Person
import com.project.toko.detailScreen.model.castModel.VoiceActor

@Composable
fun ShowWholeCast(
    navController: NavController,
    viewModel: DetailScreenViewModel,
    modifier: Modifier,
) {

    val castList by
    viewModel.castList.collectAsStateWithLifecycle()
    val castWithJapVoiceActors = hasJapVoiceActor(castList)

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = modifier.background(MaterialTheme.colorScheme.primary)) {
        item { Spacer(modifier = modifier.height(70.dp)) }
        itemsIndexed(castWithJapVoiceActors) { _, data ->
            AddCast(
                castList = data,
                navController = navController,
                modifier = modifier,
            )

        }
    }

    BackArrow(modifier, navController, viewModel.loadedId.intValue)
}

@Composable
private fun AddCast(
    castList: CastData,
    navController: NavController,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Column(
            modifier
                .fillMaxWidth()
                .height(160.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Выведите каждого персонажа из текущей карточки с индексом j
            CurrentCast(
                characterPainter = rememberAsyncImagePainter(model = castList.character.images.webp.image_url),
                modifier = modifier,
                personPainter = rememberAsyncImagePainter(model = castList.voice_actors[0].person.images.jpg.image_url),
                voiceActor = castList.voice_actors[0],
                data = castList,
                navController = navController
            )
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
    val svgImageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .size(width = 95.dp, height = 150.dp),
        ) {
            if (voiceActor.person.url == "") {
                Box(
                    modifier = Modifier
                        .fillMaxSize(0.9f)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.onSecondary),
                    contentAlignment = Alignment.Center

                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.personplaceholder, imageLoader = svgImageLoader
                        ), contentDescription = null, modifier = modifier
                            .fillMaxSize(0.6f)
                            .padding(bottom = 10.dp)
                    )
                }
            } else {
                Image(
                    painter = personPainter,
                    contentDescription = "Voice actor : ${voiceActor.person.name}",
                    modifier = customModifier
                        .fillMaxSize(0.9f)
                        .clip(RoundedCornerShape(2.dp)),
                    contentScale = ContentScale.FillBounds
                )
            }
        }


        Column(
            modifier = modifier
                .size(width = 160.dp, height = 150.dp)

        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {
                Text(
                    text = voiceActor.person.name,
                    modifier = Modifier,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = voiceActor.language,
                    modifier = Modifier,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Column(
                modifier = modifier
                    .height(70.dp)
            ) {}
            Column(
                horizontalAlignment = Alignment.End,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
            ) {
                Text(
                    text = data.character.name,
                    modifier = Modifier,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = data.role,
                    modifier = Modifier,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier

                .size(width = 95.dp, height = 150.dp),
        ) {

            if (data.character.url == "") {
                Box(
                    modifier = Modifier
                        .fillMaxSize(0.9f)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.onSecondary),
                    contentAlignment = Alignment.Center

                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.personplaceholder, imageLoader = svgImageLoader
                        ), contentDescription = null, modifier = modifier
                            .fillMaxSize(0.6f)
                            .padding(bottom = 10.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                    )
                }
            } else {
                Image(
                    painter = characterPainter,
                    contentDescription = "Character name: ${data.character.name}",
                    modifier = Modifier
                        .fillMaxSize(0.9f)
                        .clip(RoundedCornerShape(2.dp))
                        .clickable {
                            navController.navigate(route = "detail_on_character/${data.character.mal_id}")
                        },
                    contentScale = ContentScale.FillBounds
                )
            }
        }

    }
}

@Composable
private fun BackArrow(modifier: Modifier, navController: NavController, detailScreenMalId: Int) {
    val backArrowFirstColor = if (isSystemInDarkTheme()) DarkBackArrowCastColor else BackArrowCastColor
    val backArrowSecondColor =if (isSystemInDarkTheme()) DarkBackArrowSecondCastColor else BackArrowSecondCastColor
    Column {
        Spacer(modifier = modifier.height(20.dp))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(backArrowFirstColor)
        ) {
            Text(
                text = "   <    Cast                          ",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start,
                textDecoration = TextDecoration.Underline,
                modifier = modifier.clickable {
                    navController.navigate("detail_screen/$detailScreenMalId") {
                        launchSingleTop = true
                        popUpTo(route = Screen.DetailOnWholeCast.route) {
                            inclusive = true
                        }
                    }
                },
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Box(
            modifier = modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.02f)
                .background(backArrowSecondColor)
        )

        Spacer(modifier = modifier.height(20.dp))
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
