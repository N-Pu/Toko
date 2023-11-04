package com.project.toko.personDetailedScreen.presentation_layer.staffMemberFull


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.project.toko.core.presentation_layer.animations.LoadingAnimation
import com.project.toko.personDetailedScreen.viewModel.PersonByIdViewModel
import com.project.toko.core.presentation_layer.expandableText.ExpandableText
import com.project.toko.core.viewModel.daoViewModel.DaoViewModel
import kotlinx.coroutines.delay


@Composable
fun DisplayPersonFullScreen(
    mal_id: Int,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier
) {
    val personViewModel = viewModelProvider[PersonByIdViewModel::class.java]
    val daoViewModel = viewModelProvider[DaoViewModel::class.java]

    LaunchedEffect(mal_id) {
        delay(300L)
        personViewModel.getPersonFromId(mal_id)
        delay(300L)
        personViewModel.getPicturesFromId(mal_id)
    }

    val isSearching by personViewModel.isSearching.collectAsStateWithLifecycle()
    val personFullState by
    personViewModel.personFull.collectAsStateWithLifecycle()
    val personPicturesState by
    personViewModel.picturesList.collectAsStateWithLifecycle()
    val painter = rememberAsyncImagePainter(model = personFullState?.images?.jpg?.image_url)
    val svgImageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()
    val isDialogShown = remember { mutableStateOf(false) }
    if (isSearching.not() && personFullState != null) {

        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {}
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(250.dp)
//                    .background(Color.Red)
            ) {
                ShowPersonPicture(
                    painter = painter,
                    modifier = modifier,
                    isDialogShown = isDialogShown
                )
                ShowNamesAndInteractionIcons(
                    data = personFullState!!,
                    modifier = modifier,
                    imageLoader = svgImageLoader,
                    daoViewModel = daoViewModel,
                    personViewModel = personViewModel
                )
                ShowCharacterPictureAlbum(
                    isDialogShown = isDialogShown,
                    picturesData = personPicturesState,
                    modifier = modifier
                )


            }
            Row {
                personFullState?.about?.let { about ->
                    ExpandableText(text = about, title = "More Information", modifier = modifier)
                }
            }

            if (!personFullState?.voices.isNullOrEmpty()) {
                ShowAnimeRelated(
                    modifier = modifier,
                    voices = personFullState?.voices!!,
                    navController = navController
                )

            }
//            personFullState?.voices?.let { voicesList ->
//                ShowAnimeRelated(
//                    modifier = modifier,
//                    voices = voicesList,
//                    navController = navController
//                )
//
//            }
            if (
                !personFullState?.anime.isNullOrEmpty()
            ) {
                ShowStaffPosition(
                    modifier = modifier,
                    animes = personFullState!!.anime,
                    navController = navController
                )

            }
        }


    } else LoadingAnimation()
}


//
//@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
//@Composable
//fun DisplayStaffMemberFromId(
//    mal_id: Int, navController: NavController, viewModelProvider: ViewModelProvider,
//    modifier: Modifier
//) {
//
//    val viewModel = viewModelProvider[PersonByIdViewModel::class.java]
//
//    LaunchedEffect(mal_id) {
//        viewModel.viewModelScope.launch(Dispatchers.IO) {
//            viewModel.getPersonFromId(mal_id)
//        }
//    }
//
//    val rememberSheetState = rememberBottomSheetScaffoldState()
//    val coroutine = rememberCoroutineScope()
//    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
//    val staffFullState by
//    viewModel.personFull.collectAsStateWithLifecycle()
//    val painter =
//        rememberAsyncImagePainter(model = staffFullState?.images?.jpg?.image_url)
//    if (isSearching.not()) {
//
//        BottomSheetScaffold(
//            sheetContainerColor = LightGreen,
//            sheetPeekHeight = 0.dp,
//            scaffoldState = rememberSheetState,
//            sheetContent = {
//                Text(
//                    text = "${staffFullState?.name} roles",
//                    textAlign = TextAlign.Center,
//                    modifier = modifier.fillMaxWidth(),
//                    fontSize = 36.sp
//                )
//                LazyVerticalStaggeredGrid(
//                    modifier = modifier.fillMaxSize(),
//                    columns = StaggeredGridCells.Adaptive(120.dp),
//                    contentPadding = PaddingValues(0.dp),
//
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalItemSpacing = 8.dp
//                ) {
//                    staffFullState?.let {
//                        itemsIndexed(it.anime) { _, anime ->
//
//                            val painterRoles =
//                                rememberAsyncImagePainter(model = anime.anime.images.jpg.large_image_url)
//
//                            DisplayProjectsRelated(
//                                anime = anime,
//                                painter = painterRoles,
//                                navController = navController,
//                                modifier = modifier
//                            )
//
//                        }
//                    }
//                }
//            }) {
//
//            LazyColumn(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.912f),
//                horizontalAlignment = Alignment.CenterHorizontally
//
//            ) {
//
//                item {
//
//                    BoxWithConstraints(
//                        modifier = modifier
//                            .padding(20.dp)
//                            .aspectRatio(9 / 11f)
//                            .wrapContentSize()
//
//                    ) {
//
//                        Image(
//                            painter = painter,
//                            contentDescription = "Person's name: ${staffFullState?.name}",
//                            modifier = Modifier
//                                .fillMaxHeight(1f)
//                                .fillMaxWidth(1f)
//
//                        )
//                    }
//                }
//
//                item {
//                    staffFullState?.let {
//                        Text(
//                            text = it.name,
//                            textAlign = TextAlign.Center,
//                            fontSize = 40.sp,
//                            modifier = modifier.fillMaxWidth(),
//                            maxLines = 1
//                        )
//                    }
//                }
//                item {
//                    BottomSheetButton(modifier, coroutine, rememberSheetState)
//                }
//                item {
//                    // writes null in ui if there's no "about" data
//                    staffFullState?.about?.let { about ->
//                        Text(
//                            text = about,
//                            textAlign = TextAlign.Center,
//                            modifier = modifier.fillMaxWidth()
//                        )
//                    }
//                }
//
//
//            }
//        }
//    } else
//        LoadingAnimation()
//
//}
//
//@Composable
//fun DisplayProjectsRelated(
//    anime: Anime,
//    painter: AsyncImagePainter,
//    navController: NavController,
//    modifier: Modifier
//) {
//    Card(modifier = modifier
//        .clickable {
//            navController.navigate("detail_screen/${anime.anime.mal_id}")
//        }
//        .fillMaxSize()
//        .padding(PaddingValues(0.dp))) {
//
//        Column(verticalArrangement = Arrangement.Center, modifier = modifier.fillMaxWidth()) {
//            Image(
//                painter = painter,
//                contentDescription = anime.anime.title,
//                modifier = modifier.size(200.dp)
//            )
//            Text(
//                text = anime.anime.title,
//                textAlign = TextAlign.Center,
//                modifier = modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 8.dp) // Добавляем горизонтальный отступ для центрирования текста
//            )
//            Spacer(
//                modifier = modifier
//                    .height(1.dp)
//                    .fillMaxWidth()
//                    .background(Color.Black)
//            )
//            Text(
//                text = anime.position,
//                modifier = modifier.fillMaxWidth(),
//                textAlign = TextAlign.Center // Центрируем текст по горизонтали
//            )
//        }
//    }
//}





