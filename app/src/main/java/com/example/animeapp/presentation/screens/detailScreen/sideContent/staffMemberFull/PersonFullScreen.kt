package com.example.animeapp.presentation.screens.detailScreen.sideContent.staffMemberFull


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImagePainter
import com.example.animeapp.domain.models.personFullModel.Anime
import com.example.animeapp.presentation.animations.LoadingAnimation
import com.example.animeapp.domain.viewModel.PersonByIdViewModel
import com.example.animeapp.presentation.theme.LightGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DisplayStaffMemberFromId(
    mal_id: Int, navController: NavController, viewModelProvider: ViewModelProvider,
    modifier: Modifier
) {


    LaunchedEffect(mal_id) {
        withContext(Dispatchers.IO) {
            viewModelProvider[PersonByIdViewModel::class.java].getPersonFromId(mal_id)
        }
    }


    val isSearching by viewModelProvider[PersonByIdViewModel::class.java].isSearching.collectAsStateWithLifecycle()
    val staffFullState by
    viewModelProvider[PersonByIdViewModel::class.java].personFull.collectAsStateWithLifecycle()
    val painter =
        rememberAsyncImagePainter(model = staffFullState?.images?.jpg?.image_url)
    if (isSearching.not()) {
        BottomSheetScaffold(
            sheetContainerColor = LightGreen,
            sheetPeekHeight = 65.dp,
            sheetContent = {
                Text(
                    text = "${staffFullState?.name} roles",
                    textAlign = TextAlign.Center,
                    modifier = modifier.fillMaxWidth(),
                    fontSize = 36.sp
                )
                LazyVerticalStaggeredGrid(
                    modifier = modifier.fillMaxSize(),
                    columns = StaggeredGridCells.Adaptive(120.dp),
                    contentPadding = PaddingValues(0.dp),

                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 8.dp
                ) {
                    staffFullState?.let {
                        itemsIndexed(it.anime) { _, anime ->

                            val painterRoles =
                                rememberAsyncImagePainter(model = anime.anime.images.jpg.large_image_url)

                            DisplayProjectsRelated(
                                anime = anime,
                                painter = painterRoles,
                                navController = navController,
                                modifier = modifier
//                                viewModelProvider = viewModelProvider
                            )

                        }
                    }
                }
            }) {


            LazyColumn(modifier = modifier.fillMaxSize()) {
                item {
                    Card(
                        modifier = modifier
                            .fillMaxSize()
                    ) {


                        Image(
                            painter = painter,
                            contentDescription = "Character name: ${staffFullState?.name}",
                            modifier = modifier
                                .size(400.dp)
                                .aspectRatio(9 / 11f)
                        )
                    }


                    Spacer(modifier = modifier.size(28.dp))

                }

                item {
                    staffFullState?.let {
                        Text(
                            text = it.name,
                            textAlign = TextAlign.Center,
                            fontSize = 40.sp,
                            modifier = modifier.fillMaxWidth(),
                            maxLines = 1
                        )
                    }
                    // writes null in ui if there's no "about" data
                    staffFullState?.about?.let { about ->
                        Text(
                            text = about,
                            textAlign = TextAlign.Center,
                            modifier = modifier.fillMaxWidth()
                        )
                    }
                }


            }
        }
    } else
        LoadingAnimation()

}

@Composable
fun DisplayProjectsRelated(
    anime: Anime,
    painter: AsyncImagePainter,
    navController: NavController,
    modifier: Modifier
) {
    Card(modifier = modifier
        .clickable {
            navController.navigate("detail_screen/${anime.anime.mal_id}")
        }
        .fillMaxSize()
        .padding(PaddingValues(0.dp))) {

        Column(verticalArrangement = Arrangement.Center, modifier = modifier.fillMaxWidth()) {
            Image(
                painter = painter,
                contentDescription = anime.anime.title,
                modifier = modifier.size(200.dp)
            )
            Text(
                text = anime.anime.title,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp) // Добавляем горизонтальный отступ для центрирования текста
            )
            Spacer(
                modifier = modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.Black)
            )
            Text(
                text = anime.position,
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center // Центрируем текст по горизонтали
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewBottomSheet() {
    val imageResource = painterResource(com.example.animeapp.R.drawable.kurisu)
//    val painter = rememberAsyncImagePainter(model = com.example.animeapp.R.drawable.kurisu)
    val listOfStrings: List<String> = listOf(
        "строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1строка 1",
        "строка 2",
        "строка 3"
    )


    BottomSheetScaffold(
        sheetContent = {

            LazyColumn {
                item { Spacer(modifier = Modifier.size(20.dp)) }
                items(8) {
                    Card {
                        Row {
                            Column {
                                Image(painter = imageResource, contentDescription = "")
                                Text(
                                    text = "Kurisu",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.width(100.dp)
                                )
                            }
                            Column {
                                listOfStrings.forEach {
                                    Text(text = it)
                                }
                            }
                        }

                    }
                    Spacer(modifier = Modifier.size(10.dp))
                }


            }
        }
    ) {
        it.calculateBottomPadding()
    }

}





