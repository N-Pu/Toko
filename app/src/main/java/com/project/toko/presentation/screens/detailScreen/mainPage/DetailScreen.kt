package com.project.toko.presentation.screens.detailScreen.mainPage

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.project.toko.domain.models.newAnimeSearchModel.Genre
import com.project.toko.presentation.animations.LoadingAnimation
import com.project.toko.presentation.screens.detailScreen.sideContent.castList.DisplayCast
import com.project.toko.presentation.screens.detailScreen.mainPage.customVisuals.DisplayCustomGenreBoxes
import com.project.toko.presentation.screens.detailScreen.sideContent.staffList.DisplayStaff
import com.project.toko.domain.viewModel.DetailScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


@Composable
fun ActivateDetailScreen(
    viewModelProvider: ViewModelProvider,
    navController: NavController,
    id: Int,
    modifier: Modifier
) {
    val viewModel = viewModelProvider[DetailScreenViewModel::class.java]
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()

    val detailData by
    viewModel.animeDetails.collectAsStateWithLifecycle()

    val castData by viewModel.castList.collectAsStateWithLifecycle()
    val staffData by viewModel.staffList.collectAsStateWithLifecycle()


    LaunchedEffect(id) {
        withContext(Dispatchers.IO) {
            viewModel.onTapAnime(id)
            delay(300)
            viewModel.addStaffFromId(id)
            delay(300)
            viewModel.addCastFromId(id)

        }
    }


    val model = ImageRequest.Builder(LocalContext.current)
        .data(detailData?.images?.jpg?.large_image_url)
        .size(Size.ORIGINAL)
        .crossfade(true)
        .build()

    val painter =
        rememberAsyncImagePainter(
            model
        )


    if (isSearching.not() && detailData != null) {

        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            BoxWithConstraints(
                modifier = modifier,
                contentAlignment = Alignment.TopStart,
            ) {
                DisplayPicture(
                    painter = painter, modifier = modifier
                )
            }


            Title(title = detailData?.title ?: "Nothing", modifier)
            if (detailData
                    ?.genres
                    ?.isNotEmpty() == true
            ) {
                DisplayCustomGenreBoxes(
                    genres = detailData?.genres ?: listOf(
                        Genre(
                            mal_id = 0,
                            "Nothing",
                            "None",
                            "None"
                        )
                    ),
                    modifier = modifier
                )
            }

            ExpandableText(text = detailData?.synopsis ?: "Nothing", modifier)


            if (castData.isNotEmpty()) {
                DisplayCast(
                    castList = castData, navController = navController,
                    viewModelProvider = viewModelProvider
                )
            }




            if (staffData.isNotEmpty()) {
                DisplayStaff(staffList = staffData, navController = navController)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // ДОБАВИТЬ КАРТИНКУ СТУДИИ И ПЕРЕХОД НА ЭЭЭ ИХ ДАННЫЕ mal_id
            Text(text = "STUDIOS:")



            detailData?.studios?.forEach {

                println(it.url)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it.name,
                    fontSize = 40.sp,
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        println("detail_on_producer/${it.mal_id}/${it.name}")
                        navController.navigate(
                            "detail_on_producer/${it.mal_id}/${it.name}"
                        ) {
                            launchSingleTop = true
                        }
                    }
                )

            }


            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "background: " + (detailData?.background ?: "None"))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "broadcast: " + (detailData?.broadcast?.string ?: "None"))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "duration: " + detailData?.duration)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "episodes: " + detailData?.episodes.toString())
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "favorites: " + detailData?.favorites.toString())
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "members: " + detailData?.members.toString())
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "popularity: " + detailData?.popularity.toString())
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "rank: " + detailData?.rank.toString())
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "rating: " + (detailData?.rating ?: "None"))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "season: " + (detailData?.season ?: "None"))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "source: " + (detailData?.source ?: "None"))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "status: " + (detailData?.status ?: "None"))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "title: " + (detailData?.title ?: "None"))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "english title: " + (detailData?.title_english ?: "None"))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "jap title: " + (detailData?.title_japanese ?: "None"))
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "trailer url =" + (detailData?.trailer?.url ?: "None") +
                        "id " + (detailData?.trailer?.youtube_id ?: "None")
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "approved :" + (detailData?.type ?: "None"))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "url :" + (detailData?.url ?: "None"))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "year :" + detailData?.year.toString())
            Spacer(modifier = Modifier.height(16.dp))


            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "title_synonyms", textDecoration = TextDecoration.Underline)
            detailData?.title_synonyms?.forEach { // well, okay
                Text(text = it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "demographics", textDecoration = TextDecoration.Underline)
            detailData?.demographics?.forEach { // idk
                Text(text = it.name)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "licensors", textDecoration = TextDecoration.Underline)
            detailData?.licensors?.forEach { Text(text = it.name) } //  companies that produce this animes on english television
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "producers", textDecoration = TextDecoration.Underline)
            detailData?.producers?.forEach { // producer companies
                Text(text = it.name)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "studios", textDecoration = TextDecoration.Underline)
            detailData?.studios?.forEach { //студии
                Text(text = it.name)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "explicit_genres", textDecoration = TextDecoration.Underline)
            detailData?.explicit_genres?.forEach {
                Text(text = it.name)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "themes", textDecoration = TextDecoration.Underline)
            detailData?.themes?.forEach {
                Text(text = it.name)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "titles", textDecoration = TextDecoration.Underline)
            detailData?.titles?.forEach {
                Text(text = it.title)
            }

            Spacer(modifier = Modifier.size(60.dp))

        }


    } else {
        LoadingAnimation()
    }
}


@Composable
fun ExpandableText(text: String, modifier: Modifier) {
    val wordCount = text.split(" ").count()

    if (wordCount <= 20) {
        Text(text = text)
        return
    }

    var expanded by remember { mutableStateOf(false) }
    val toggleExpanded: () -> Unit = { expanded = !expanded }

    val maxLines = if (expanded) Int.MAX_VALUE else 4

    Column {
        Text(
            text = text,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .clickable(onClick = toggleExpanded)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
            softWrap = true
        )

        Row(modifier = Modifier) {
            if (expanded) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = modifier
                        .clickable(onClick = toggleExpanded)
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth()
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = modifier
                        .clickable(onClick = toggleExpanded)
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth()
                )
            }
        }
    }
}


@Composable
fun Title(title: String, modifier: Modifier) {
    Text(
        text = title,

        fontSize = 50.sp,
        modifier = modifier.fillMaxWidth(),
        fontWeight = FontWeight.SemiBold,
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ), textAlign = TextAlign.Center
    )
}

@Composable
fun DisplayPicture(
    painter: AsyncImagePainter, modifier: Modifier
) {

    Image(
        painter = painter,
        contentDescription = "Big anime picture",
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .fillMaxWidth(1f),

        )
}