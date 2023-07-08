package com.example.animeapp.presentation.screens.detailScreen.sideContent.characterFull

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.domain.viewModel.CharacterFullByIdViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImagePainter
import com.example.animeapp.R
import com.example.animeapp.domain.models.characterModel.Anime
import com.example.animeapp.presentation.animations.LoadingAnimation
import com.example.animeapp.presentation.theme.LightYellow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DisplayCharacterFromId(
    mal_id: Int, navController: NavController, viewModelProvider: ViewModelProvider
) {

    val viewModel = viewModelProvider[CharacterFullByIdViewModel::class.java]

    LaunchedEffect(mal_id) {
        withContext(Dispatchers.IO) {
            delay(300L)
            viewModel.getCharacterFromId(mal_id)
            delay(300L)
            viewModel.getPicturesFromId(mal_id)
        }


    }


    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val characterFullState by
        viewModel.characterFull.collectAsStateWithLifecycle()
    val characterPicturesState by
        viewModel.picturesList.collectAsStateWithLifecycle()


    if (isSearching.not()) {

        BottomSheetScaffold(
            sheetContainerColor = LightYellow,
            sheetPeekHeight = 65.dp,
            sheetContent = {

                Text(
                    text = "${characterFullState?.name} roles",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 36.sp
                )

                LazyVerticalStaggeredGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = StaggeredGridCells.Adaptive(120.dp),
                    contentPadding = PaddingValues(0.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 8.dp
                ) {
                    characterFullState?.anime?.let {
                        itemsIndexed(it) { _, anime ->
                            val painter =
                                rememberAsyncImagePainter(model = anime.anime.images.jpg.large_image_url)

                            DisplayProjectsRelated(
                                anime = anime,
                                painter = painter,
                                navController = navController,
//                                viewModelProvider = viewModelProvider
                            )
                        }
                    }
                }
            }) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())

            ) {

                DisplayHorizontalPagerWithIndicator(
                    painterList = characterPicturesState
                )



                Spacer(modifier = Modifier.size(8.dp))
                characterFullState?.let { data ->
                    DisplayKanjiAndEnglishName(data = data)

                }

                // writes null in ui if there's no "about" data
                characterFullState?.about?.let { about ->
                    Text(
                        text = about, textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.size(28.dp))
            }

        }


    } else
        LoadingAnimation()
}


//}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplayHorizontalPagerWithIndicator(painterList: List<com.example.animeapp.domain.models.characterPictures.Data>) {

    val pagerState = rememberPagerState(initialPage = 0)

    Spacer(
        modifier = Modifier
            .height(40.dp)
    )
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.size(400.dp),
        pageCount = painterList.size,
        reverseLayout = false
    ) { page ->
        val painter = rememberAsyncImagePainter(model = painterList[page].jpg.image_url)
        Log.d("Picture #${page}", painterList[page].jpg.image_url)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(400.dp),
//                .aspectRatio(9 / 11f)
        )
    }

    Spacer(
        modifier = Modifier
            .height(10.dp)
    )
    // Dot indicators
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        repeat(painterList.size) { index ->
            val color = if (index == pagerState.currentPage) Color.DarkGray else LightYellow
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}


@Composable
fun DisplayKanjiAndEnglishName(data: com.example.animeapp.domain.models.characterModel.Data) {
    Text(
        text = data.name,
        textAlign = TextAlign.Center,
        fontSize = 40.sp,
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1
    )
    Spacer(modifier = Modifier.size(5.dp))
    Text(
        text = "Kanji: " + data.name_kanji,
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true)
@Composable
fun ImagePager() {
    val painter1 = rememberAsyncImagePainter(model = R.drawable.kurisu)
    val painter2 = rememberAsyncImagePainter(model = R.drawable.kurisu)
    val painterList = listOf(painter1, painter2)
    val pagerState = rememberPagerState(initialPage = painterList.size)


    HorizontalPager(
        state = pagerState, modifier = Modifier.fillMaxSize(), pageCount = painterList.size
    ) { page ->
        val painter = painterList[page]
        Image(
            painter = painter, contentDescription = null, modifier = Modifier.fillMaxSize()
        )
    }
}


//change and optimize with generics
//same functions contains in PersonFullScreen.kt
@Composable
fun DisplayProjectsRelated(
    anime: Anime,
    painter: AsyncImagePainter,
    navController: NavController,
//    viewModelProvider: ViewModelProvider
) {
    Card(modifier = Modifier
        .clickable {
            navController.navigate("detail_screen/${anime.anime.mal_id}")
        }
        .fillMaxSize()
        .padding(PaddingValues(0.dp))) {

        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painter,
                contentDescription = anime.anime.title,
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = anime.anime.title,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp) // Добавляем горизонтальный отступ для центрирования текста
            )
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.Black)
            )
            Text(
                text = anime.role,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center // Центрируем текст по горизонтали
            )
        }
    }
}
