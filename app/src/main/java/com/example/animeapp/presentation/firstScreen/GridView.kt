package com.example.animeapp.presentation.firstScreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.domain.searchModel.Data
import com.example.animeapp.presentation.navigation.Screen
import com.example.animeapp.presentation.theme.LightYellow
import com.example.animeapp.viewModel.DetailScreenViewModel


@Composable
fun GridAdder(
    listData: List<Data>,
    navController: NavHostController
) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(listData) { _, anime ->
            AnimeCardBox(anime = anime, navController)

        }
    }
}

//private val viewModelDetail = DetailScreenViewModel()
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimeCardBox(
    anime: Data,
    navController: NavHostController
) {



    Card(
        // no background
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable {
//                viewModelDetail.onTapAnime(anime.mal_id)
                navController.navigate(route = "detail_screen/" + anime.mal_id) {
                    popUpTo(Screen.Detail.route) {
                        inclusive = true
                    }
                }
            },

        colors = CardDefaults.cardColors(containerColor = LightYellow),
        shape = RectangleShape

    ) {
        val painter = rememberAsyncImagePainter(model = anime.images.webp.large_image_url)
        // Coil image loader
        Image(
            painter = painter,
            contentDescription = "Images for each Anime",
            modifier = Modifier
                .aspectRatio(9f / 11f),

            contentScale = ContentScale.FillBounds

        )


        // DO IT LATER -> probably isn't working
//        Image(
//            painter = painter,
//            contentScale = ContentScale.Crop,
//            contentDescription = "contentDescription",
//            modifier = Modifier
////                .padding(16.dp, 0.dp, 16.dp, 0.dp)
////                .fillMaxWidth()
//                .then(
//                    // here is always null - i don't know why
//                    (painter.state as? AsyncImagePainter.State.Success)
//                        ?.painter
//                        ?.intrinsicSize
//                        ?.let { intrinsicSize ->
//                            Modifier.aspectRatio(intrinsicSize.width / intrinsicSize.height)
//                        } ?: Modifier
//                )
//        )


        Text(text = anime.score.toString())
        Text(text = anime.scored_by.toString())




        Text(
            text = anime.title,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    delayMillis = 2000,
                    initialDelayMillis = 2000,
                    velocity = 50.dp
                ),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight(1000),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1

        )
    }

//        .let {
//        DetailScreenViewModel().onTapAnime(id = anime.mal_id)
//    }


}


//    @Preview
//    @Composable
//    fun AnimeCardBoxPrev() {
//
//        AnimeCardBox(
//            anime = Data(
//                1,
//                "DSDSDSDSffff",
//                "BOOOOOO",
//                Images(
//                    Jpg(
//                        "S",
//                        "S",
//                        "s"
//                    ), Webp("b", "V", "ffd")
//                )
//            ),
//        )
//    }




