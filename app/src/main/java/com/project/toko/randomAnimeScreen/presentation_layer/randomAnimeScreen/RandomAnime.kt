package com.project.toko.randomAnimeScreen.presentation_layer.randomAnimeScreen

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.randomAnimeScreen.viewModel.RandomAnimeViewModel
import com.project.toko.core.presentation_layer.animations.LoadingAnimation
import com.project.toko.homeScreen.presentation_layer.homeScreen.navigateToDetailScreen
import com.project.toko.core.presentation_layer.theme.DialogColor
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.core.presentation_layer.theme.MainBackgroundColor
import com.project.toko.daoScreen.daoViewModel.DaoViewModel
import com.project.toko.daoScreen.model.AnimeListType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowRandomAnime(
    navController: NavController, modifier: Modifier, viewModelProvider: ViewModelProvider
) {
    val randomViewModel = viewModelProvider[RandomAnimeViewModel::class.java]
    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    val state by randomViewModel.animeDetails.collectAsStateWithLifecycle()
    val cardIsShown = remember {
        mutableStateOf(true)
    }


    Column(
        modifier.background(MainBackgroundColor).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (cardIsShown.value) {
            if (state == null) {
                BoxWithConstraints(
                    modifier = Modifier
                        .clip(CardDefaults.shape)
                        .background(LightGreen)
                        .combinedClickable(onDoubleClick = {
                            randomViewModel.viewModelScope.launch(Dispatchers.IO) {
                                randomViewModel.onTapRandomAnime()
                            }
                        }) {}
                        .padding(20.dp, 20.dp, 20.dp, 20.dp)
                ) {
                    Text(
                        text = "Tap 2 times",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else {
                AnimeCard(
                    data = state,
                    modifier = modifier,
                    navController = navController,
                    randomViewModel = randomViewModel,
                    cardIsShown = cardIsShown,
                    daoViewModel = daoViewModel
                )
            }

        } else {
            LoadingAnimation()
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun DraggablePreview() {

    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }

    Column {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize(1f)
        ) {
            Card(modifier = Modifier
                .size(250.dp)
                .offset {
                    IntOffset(
                        offsetX.floatValue.roundToInt(),
                        offsetY.floatValue.roundToInt()
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX.floatValue += dragAmount.x
                        offsetY.floatValue += dragAmount.y

                        if (offsetX.floatValue <= -250f && offsetY.floatValue >= -550f) {
                            println("LEFT " + " x " + offsetX.floatValue + " y " + offsetY.floatValue)
                        }
                        if (offsetY.floatValue <= -551f) {
                            println("UP" + " y " + offsetY.floatValue)
                        }
                    }
                }
                .graphicsLayer(
                    translationX = offsetX.floatValue, translationY = offsetY.floatValue
                )

            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "x = " + offsetX.floatValue.toString() + "/" + "y = " + offsetY.floatValue.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Black,
                        letterSpacing = 5.sp,
                        lineHeight = 35.sp
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CardPreview() {

    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }

    val scoreRoundedCornerShape = remember { RoundedCornerShape(bottomEnd = 10.dp) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(1f)
    ) {
        Box(modifier = Modifier
            .background(Color(104, 104, 104, 0))
            .clip(CardDefaults.shape)
            .width(340.dp)
            .height(490.dp)
            .offset { IntOffset(offsetX.floatValue.roundToInt(), offsetY.floatValue.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX.floatValue += dragAmount.x
                    offsetY.floatValue += dragAmount.y

                    if (offsetY.floatValue <= -551f) {
                        println("UP" + " y " + offsetY.floatValue)
                    }
                    if (offsetX.floatValue <= -250f && offsetY.floatValue >= -550f) {
                        println("LEFT " + " x " + offsetX.floatValue + " y " + offsetY.floatValue)
                    }

                }
            }
            .graphicsLayer(
                translationX = offsetX.floatValue, translationY = offsetY.floatValue
            )

        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    Text(
                        text = "Fushigi no Umi no Nadia",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        letterSpacing = 5.sp,
                        lineHeight = 35.sp
                    )
                }
                Row {
                    Text(
                        text = "Nadia: Secret of Blue Water",
                        textAlign = TextAlign.End,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Black,
                        letterSpacing = 2.sp,
                        lineHeight = 35.sp
                    )
                }

            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(430.dp),
                    colors = CardDefaults.cardColors(containerColor = DialogColor),
                ) {
                    Box(
                        modifier = Modifier
                            .background(LightGreen, scoreRoundedCornerShape)
                            .size(75.dp), contentAlignment = Alignment.Center
                    ) {
                        Text(text = "7.52", color = Color.White, fontSize = 25.sp)
                    }

                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .shadow(
                                    elevation = 80.dp,
                                    spotColor = Color.Black,
                                    ambientColor = Color.Black
                                )
                        ) {


                            Row(modifier = Modifier.fillMaxWidth()) {
                                DisplayCustomGenres(
                                    genres = listOf(
                                        com.project.toko.homeScreen.model.newAnimeSearchModel.Genre(
                                            0,
                                            "Adventure",
                                            "",
                                            ""
                                        ),
                                        com.project.toko.homeScreen.model.newAnimeSearchModel.Genre(
                                            0,
                                            "Comedy",
                                            "",
                                            ""
                                        ),
                                        com.project.toko.homeScreen.model.newAnimeSearchModel.Genre(
                                            0,
                                            "Romance",
                                            "",
                                            ""
                                        ),
                                    ), modifier = Modifier
                                )

                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "   Status: ",
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "Finished Airing",
                                    fontSize = 12.sp,
                                    color = LightGreen
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "   Rating: PG- 13 - Teens 13 or older",
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(text = "   Episodes:26", fontSize = 12.sp, color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                }

            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Spring 1990", color = LightGreen)
                Text(" | ", color = Color.Gray)
                Text("TV", color = LightGreen)
                Text(" | ", color = Color.Gray)
                Text("Gainax", color = LightGreen)
            }

        }
    }

}


@Composable
fun AnimeCard(
    data: com.project.toko.homeScreen.model.newAnimeSearchModel.AnimeSearchData?,
    modifier: Modifier,
    navController: NavController,
    cardIsShown: MutableState<Boolean>,
    randomViewModel: RandomAnimeViewModel,
    daoViewModel: DaoViewModel
) {

    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }

    val model = ImageRequest.Builder(LocalContext.current)
        .data(data?.images?.jpg?.large_image_url)
        .size(Size.ORIGINAL)
        .crossfade(true)
        .build()

    val painter =
        rememberAsyncImagePainter(
            model = model,
        )

    val scoreRoundedCornerShape = remember { RoundedCornerShape(bottomEnd = 10.dp) }

    val clickableModifier = modifier.clickable {
        if (data != null) {
            navigateToDetailScreen(navController, data.mal_id)
        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(1f)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(129, 129, 129, 65)),
            modifier = modifier
                .width(340.dp)
                .height(510.dp)
                .offset {
                    IntOffset(
                        offsetX.floatValue.roundToInt(),
                        offsetY.floatValue.roundToInt()
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX.floatValue += dragAmount.x
                        offsetY.floatValue += dragAmount.y

                        if (offsetX.floatValue <= -500f && offsetY.floatValue >= -550f) {
                            println("LEFT " + " x " + offsetX.floatValue + " y " + offsetY.floatValue)
                            randomViewModel.viewModelScope.launch(Dispatchers.IO) {
                                cardIsShown.value = false
                                randomViewModel.onTapRandomAnime()
                                delay(1000L)
                                cardIsShown.value = true
                                offsetX.floatValue = 0.0f
                                offsetY.floatValue = 0.0f
                            }
                        }
                        if (offsetY.floatValue <= -550f) {
                            println("UP" + " y " + offsetY.floatValue)
                            data?.let { animeData ->
                                navigateToDetailScreen(navController, animeData.mal_id)
                            }

                        }
                        if (offsetX.floatValue >= 500f && offsetY.floatValue >= -550f) {
                            println("LEFT " + " x " + offsetX.floatValue + " y " + offsetY.floatValue)

                            daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                                daoViewModel.addToCategory(
                                    animeItem = AnimeItem(
                                        id = data?.mal_id,
                                        animeName = data?.title ?: "Error",
                                        animeImage = data?.images?.jpg?.large_image_url ?: "",
                                        score = data?.score.toString(),
                                        scored_by = data?.scored_by.toString(),
                                        category = AnimeListType.PLANNED.route,
                                        status = data?.status ?: "",
                                        rating = data?.rating ?: "",
                                        secondName = data?.title_japanese ?: ""
                                    )
                                )
                                cardIsShown.value = false
                                randomViewModel.onTapRandomAnime()
                                delay(1000L)
                                cardIsShown.value = true
                                offsetX.floatValue = 0.0f
                                offsetY.floatValue = 0.0f
                            }
                        }
                    }
                }
                .graphicsLayer(
                    translationX = offsetX.floatValue, translationY = offsetY.floatValue
                )

        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.095f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxHeight(1f)
                        .padding(start = 20.dp, end = 20.dp)
                ) {

                    Text(
                        text = data?.title ?: "",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 3.sp,
                        lineHeight = 1.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }

                if (data?.title_english != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = modifier.fillMaxHeight(1f)
                    ) {

                        Text(
                            text = data.title_english,
                            textAlign = TextAlign.End,
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Light,
                            color = Color.Black,
                            letterSpacing = 2.sp,
                            lineHeight = 5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }


            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top,
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .size(300.dp, 420.dp)
                    .clip(CardDefaults.shape),
            ) {
                BoxWithConstraints {


                    Image(
                        painter = painter,
                        contentDescription = "Anime ${data?.title}",
                        modifier = modifier
                            .then(clickableModifier)
                            .fillMaxHeight(1f)
                            .fillMaxWidth(1f),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter
                    )


                    Row(
                        modifier = Modifier
                            .fillMaxHeight(1f)
                            .fillMaxWidth(1f)
                    ) {
                        Box(
                            modifier = modifier
                                .background(
                                    scoreColorChanger(data?.score ?: 0.0f),
                                    scoreRoundedCornerShape
                                )
                                .size(75.dp), contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = if (data?.score.toString() == "0.0") "N/A" else data?.score.toString(),
                                color = Color.White,
                                fontSize = 25.sp
                            )

                        }
                    }


                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = modifier
                                .wrapContentSize()
                                .shadow(
                                    elevation = 80.dp,
                                    spotColor = Color(0f, 0f, 0f, 1f),
                                    ambientColor = Color(0f, 0f, 0f, 1f)
                                )
                        ) {


                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(
                                        rememberScrollState()
                                    ), horizontalArrangement = Arrangement.Center
                            ) {
                                val numbOfGenres = data?.genres?.count()

                                if (numbOfGenres != null) {
                                    if (numbOfGenres <= 3) {
                                        DisplayCustomGenres(
                                            genres = data.genres, modifier = modifier
                                        )
                                    }

                                    if (numbOfGenres > 3) {
                                        DisplayCustomGenres(
                                            genres = data.genres.take(3),
                                            modifier = modifier
                                        )
                                    }

                                }

                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "   Status: ",
                                    fontSize = 12.sp,
                                    color = Color.White
                                )

                                Text(
                                    text = data?.status ?: "",
                                    fontSize = 12.sp,
                                    color = LightGreen
                                )

                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "   Rating: ",
                                    fontSize = 12.sp,
                                    color = Color.White
                                )

                                Text(
                                    text = data?.rating ?: "N/A",
                                    fontSize = 12.sp,
                                    color = Color.White
                                )

                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {

                                Text(
                                    text = "   Episodes: " + data?.episodes,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )

                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }

                    }


                }
            }
            val isStudioEmpty = data?.studios.isNullOrEmpty()

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
            ) {
                if (data?.year != 0 && data?.season != null) {

                    Text(data.season + " " + data.year.toString(), color = LightGreen)

                    Text(" | ", color = DialogColor)
                }
                Text(data?.type ?: "N/A", color = LightGreen)
                if (!isStudioEmpty) {
                    Text(" | ", color = DialogColor)

                    Text(data?.studios?.component1()?.name ?: "N/A", color = LightGreen)
                }
            }

        }
    }

}

@Composable
private fun ColoredBox(
    text: String,
    modifier: Modifier
) {

    Box(
        modifier = modifier
            .background(LightGreen, shape = RoundedCornerShape(15.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(4.dp)
        )
    }
}

@Composable
private fun DisplayCustomGenres(
    genres: List<com.project.toko.homeScreen.model.newAnimeSearchModel.Genre>,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        genres.forEachIndexed { index, genre ->
            if (index != 0) {
                Spacer(modifier = modifier.width(8.dp))
            }
            ColoredBox(
                text = genre.name,
                modifier
            )
        }
    }
}
