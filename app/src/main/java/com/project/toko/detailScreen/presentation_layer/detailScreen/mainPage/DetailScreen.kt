package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.project.toko.core.presentation_layer.animations.LoadingAnimation
import com.project.toko.detailScreen.presentation_layer.detailScreen.sideContent.castList.DisplayCast
import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.customVisuals.DisplayCustomGenreBoxes
import com.project.toko.detailScreen.presentation_layer.detailScreen.sideContent.staffList.DisplayStaff
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.homeScreen.model.newAnimeSearchModel.Genre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
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
    val scrollState = viewModel.scrollState


    LaunchedEffect(key1 = id) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            val previousId = viewModel.previousId.value
            if (id != previousId) {
                scrollState.scrollTo(0)
                viewModel.previousId.value = id
            }

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
                .verticalScroll(scrollState)
                .background(Color(0xFFF4F4F4)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            DisplayPicture(
                painter = painter, modifier = modifier
//                , navController = navController
            )
            Row(
                modifier = modifier
                    .fillMaxWidth(1f),
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top
            ) {
                DisplayTitle(title = detailData?.title ?: "No title name", modifier)
            }
            Row(
                modifier = modifier
                    .fillMaxWidth(1f)
                    .height(20.dp)
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        delayMillis = 2000,
                        initialDelayMillis = 2000,
                        velocity = 50.dp
                    )
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top
            ) {

                if (detailData?.title_english?.isNotEmpty() == true) {
                    Text(
                        text = detailData?.title_english ?: "",
                        minLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "/",
                        minLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = detailData?.title_japanese ?: "",
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth(1f)
            ) {
                YearTypeEpisodesTimeStatusStudio(data = detailData, modifier = modifier)
            }

            Row(
                modifier = modifier
                    .fillMaxWidth(1f)
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier.size(150.dp)
                ) {

                    ScoreLabel(modifier = modifier)
                    ScoreNumber(modifier = modifier, score = detailData?.score ?: 0.0f)
                    ScoreByNumber(scoreBy = detailData?.scored_by ?: 0.0f, modifier = modifier)

                }
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier.size(height = 150.dp, width = 150.dp)
                ) {

                    RankedLine(
                        rank = detailData?.rank ?: 0,
                        modifier = modifier
                    )
                    PopularityLine(
                        popularity = detailData?.popularity ?: 0,
                        modifier = modifier
                    )
                    MembersLine(
                        members = detailData?.members ?: 0,
                        modifier = modifier
                    )

                    FavoritesLine(
                        favorites = detailData?.favorites ?: 0,
                        modifier = modifier
                    )
                }
            }


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

            if (detailData?.synopsis?.isNotBlank() == true) {
                ExpandableText(text = detailData!!.synopsis, modifier)
            }


            ShowMoreInformation(modifier = modifier, detailData = detailData)

            if (detailData?.background?.isNotEmpty() == true) {

                Column(modifier = modifier.padding(start = 20.dp, end = 20.dp)) {
                    Row {
                        Text(
                            text = "Background",
                            color = Color.Black,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        Text(text = detailData?.background ?: "")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }


            if (castData.isNotEmpty()) {
                DisplayCast(
                    castList = castData, navController = navController,
                    modifier = modifier
                )
            }

            if (staffData.isNotEmpty()) {
                DisplayStaff(
                    staffList = staffData,
                    navController = navController,
                    modifier = modifier
                )
            }
            Spacer(modifier = Modifier.height(16.dp))


            Text(text = "STUDIOS:")
            detailData?.studios?.forEach {
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

            Text(
                text = "trailer url =" + (detailData?.trailer?.url ?: "None") +
                        "id " + (detailData?.trailer?.youtube_id ?: "None")
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "url :" + (detailData?.url ?: "None"))
            Box(modifier = modifier.size(100.dp))


        }


    } else {
        LoadingAnimation()
    }
}


@Composable
private fun ShowMoreInformation(
    modifier: Modifier,
    detailData: com.project.toko.homeScreen.model.newAnimeSearchModel.Data?
) {

    val licensors = detailData?.licensors?.joinToString(", ") { licensor -> licensor.name }
    val studios = detailData?.studios?.joinToString(", ") { studio -> studio.name }
    val producers = detailData?.producers?.joinToString(", ") { producer -> producer.name }
    val titles = detailData?.titles?.joinToString(", ") { title -> title.title }
    val demographic = detailData?.demographics?.joinToString(", ") { demo -> demo.name }
    val synonyms = detailData?.title_synonyms?.joinToString(", ") { it }
    val airedFrom = detailData?.aired?.from?.dropLast(15) ?: "N/A"
    val airedTo = detailData?.aired?.to?.dropLast(15) ?: "N/A"

    Column(
        modifier = modifier
            .fillMaxWidth(1f)
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {

            Text(
                text = "More Information",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

        }
        Row(modifier = modifier.height(20.dp), horizontalArrangement = Arrangement.Start) {

        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            Text(text = "Aired: ")
            Text(text = "from $airedFrom to $airedTo")
        }

        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val licensor = if (licensors?.isEmpty() == true)
                "N/A"
            else licensors
            Text(text = "Licensors: $licensor")

        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val synonym = if (synonyms?.isEmpty() == true)
                "N/A"
            else synonyms
            Text(text = "Synonyms: $synonym")

        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val studio = if (studios?.isEmpty() == true)
                "N/A"
            else studios
            Text(text = "Studios: $studio")
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            Text(text = "Source: ")
            Text(text = detailData?.source ?: "N/A")
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val demo = if (demographic?.isEmpty() == true)
                "N/A"
            else demographic
            Text(text = "Demographic: $demo")


        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            Text(text = "Rating: ")
            Text(text = detailData?.rating ?: "N/A", minLines = 1)
        }
        Row {
            Text(text = "Broadcast: " + (detailData?.broadcast?.string ?: "N/A"))
        }
        Row {
            val producer = if (demographic?.isEmpty() == true)
                "N/A"
            else producers
            Text(text = "Producers: $producer")
        }


        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val title = if (titles?.isEmpty() == true) {
                "N/A"
            } else titles
            Text(text = "Titles: $title")

        }

    }
}


@Composable
private fun ExpandableText(text: String, modifier: Modifier) {
    val wordCount = text.split(" ").count()
    var expanded by remember { mutableStateOf(false) }
    val toggleExpanded: () -> Unit = { expanded = !expanded }

    val maxLines = if (expanded) Int.MAX_VALUE else 4

    if (wordCount <= 20) {
        Column(
            modifier = modifier
                .fillMaxWidth(1f)
                .padding(start = 20.dp, bottom = 25.dp, end = 20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Synopsis",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = modifier.height(10.dp))
            Text(
                text = text,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier,
                softWrap = true
            )
        }
        return
    }



    Column(
        modifier = modifier
            .fillMaxWidth(1f)
            .padding(start = 20.dp, bottom = 25.dp, end = 20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Synopsis",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = modifier.height(10.dp))
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

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.clickable(onClick = toggleExpanded)
            )
            if (expanded) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = modifier
                        .clickable(onClick = toggleExpanded)
                        .align(Alignment.CenterVertically)

                )
            } else {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = modifier
                        .clickable(onClick = toggleExpanded)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
private fun DisplayTitle(title: String, modifier: Modifier) {

    Text(
        text = title,
        fontSize = 40.sp,
        modifier = modifier.fillMaxWidth(),
        fontWeight = FontWeight.SemiBold,
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            shadow = Shadow(
                offset = Offset(x = 0f, y = 6f),
                blurRadius = 5f,
                color = Color.Black.copy(alpha = 0.5f)
            )
        ), textAlign = TextAlign.Center
    )
}

@Composable
private fun DisplayPicture(
    painter: AsyncImagePainter,
    modifier: Modifier,
//    navController: NavController
) {


    Box {
        Image(
            painter = painter,
            contentDescription = "Big anime picture",
            contentScale = ContentScale.FillWidth,
            modifier = modifier
                .fillMaxWidth(1f),
        )
    }
}

@Composable
private fun ScoreLabel(modifier: Modifier) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth(1f)

    ) {
        Card(
            modifier = modifier.size(120.dp, 35.dp),
            colors = CardDefaults.cardColors(containerColor = LightGreen),
            shape = RoundedCornerShape(5.dp)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "SCORE",
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 23.sp

                )

            }

        }
    }

}

@Composable
private fun ScoreNumber(modifier: Modifier, score: Float) {

    val scoreNumb = {
        if (score == 0.0f) {
            "N/A"
        } else {
            score.toString()
        }

    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = scoreNumb(),
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            fontSize = 35.sp
        )
    }
}

@Composable
private fun ScoreByNumber(modifier: Modifier, scoreBy: Float) {

    val scoreByNumb = {
        if (scoreBy == 0.0f) {
            "N/A"
        } else {
            scoreBy.toInt().toString()
        }

    }
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = scoreByNumb() + " users",
            color = Color.Black,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun RankedLine(rank: Int, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        Text(
            text = ("Ranked "),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black,
            style = TextStyle(
                shadow = Shadow(
                    offset = Offset(x = 0f, y = 6f),
                    blurRadius = 5f,
                    color = Color.Black.copy(alpha = 0.5f)
                )
            )
        )
        Text(
            text = "#$rank",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            color = Color.Black,
            style = TextStyle(
                shadow = Shadow(
                    offset = Offset(x = 0f, y = 6f),
                    blurRadius = 5f,
                    color = Color.Black.copy(alpha = 0.5f)
                )
            )
        )
    }
}

@Composable
private fun PopularityLine(popularity: Int, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        Text(
            text = ("Popularity "),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black,
            style = TextStyle(
                shadow = Shadow(
                    offset = Offset(x = 0f, y = 6f),
                    blurRadius = 5f,
                    color = Color.Black.copy(alpha = 0.5f)
                )
            )
        )
        Text(
            text = "#$popularity",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Black,
            style = TextStyle(
                shadow = Shadow(
                    offset = Offset(x = 0f, y = 6f),
                    blurRadius = 5f,
                    color = Color.Black.copy(alpha = 0.5f)
                )
            )
        )
    }
}

@Composable
private fun MembersLine(members: Int, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {

        Text(
            text = ("Members "),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black,
            style = TextStyle(
                shadow = Shadow(
                    offset = Offset(x = 0f, y = 6f),
                    blurRadius = 5f,
                    color = Color.Black.copy(alpha = 0.5f)
                )
            )
        )
        Text(
            text = members.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Black,
            style = TextStyle(
                shadow = Shadow(
                    offset = Offset(x = 0f, y = 6f),
                    blurRadius = 5f,
                    color = Color.Black.copy(alpha = 0.5f)
                )
            )
        )
    }
}

@Composable
private fun FavoritesLine(favorites: Int, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {

        Text(
            text = ("Favorites "),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black,
            style = TextStyle(
                shadow = Shadow(
                    offset = Offset(x = 0f, y = 6f),
                    blurRadius = 5f,
                    color = Color.Black.copy(alpha = 0.5f)
                )
            )
        )
        Text(
            text = favorites.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Black,
            style = TextStyle(
                shadow = Shadow(
                    offset = Offset(x = 0f, y = 6f),
                    blurRadius = 5f,
                    color = Color.Black.copy(alpha = 0.5f)
                )
            )
        )
    }
}

@Composable
private fun YearTypeEpisodesTimeStatusStudio(
    data: com.project.toko.homeScreen.model.newAnimeSearchModel.Data?,
    modifier: Modifier
) {
    val isStudioEmpty = data?.studios.isNullOrEmpty()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(start = 20.dp, top = 5.dp, bottom = 0.dp, end = 20.dp)
            .fillMaxWidth(1f)
    ) {
        val yearSeasonText = buildAnnotatedString {
            if (data?.year != 0 && data?.season != null) {
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append("${data.season} ${data.year}")
                }
                append("/")
            }
            withStyle(style = SpanStyle(color = Color.Black)) {
                append(data?.type ?: "N/A")
                append("/")
            }

            if (data?.episodes != 0 && data?.duration != null) {
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append("${data.episodes} ep/${data.duration}")
                }
                append("/")
            }
            withStyle(style = SpanStyle(color = Color.Black)) {
                append(data?.status ?: "N/A")
            }
            if (!isStudioEmpty) {
                append("/")
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append(data?.studios?.component1()?.name ?: "N/A")
                }
            }
        }

        Text(
            text = yearSeasonText,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 20.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

    }
}
