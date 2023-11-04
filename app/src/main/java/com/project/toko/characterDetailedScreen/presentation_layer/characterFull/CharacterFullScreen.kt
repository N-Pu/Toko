package com.project.toko.characterDetailedScreen.presentation_layer.characterFull

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.project.toko.characterDetailedScreen.viewModel.CharacterFullByIdViewModel
import com.project.toko.core.presentation_layer.animations.LoadingAnimation
import com.project.toko.core.presentation_layer.expandableText.ExpandableText
import com.project.toko.core.viewModel.daoViewModel.DaoViewModel
import kotlinx.coroutines.delay


@Composable
fun DisplayCharacterFromId(
    mal_id: Int,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier
) {
    val characterViewModel = viewModelProvider[CharacterFullByIdViewModel::class.java]
    val daoViewModel = viewModelProvider[DaoViewModel::class.java]

    LaunchedEffect(mal_id) {
        delay(300L)
        characterViewModel.getCharacterFromId(mal_id)
        delay(300L)
        characterViewModel.getPicturesFromId(mal_id)
    }

    val isSearching by characterViewModel.isSearching.collectAsStateWithLifecycle()
    val characterFullState by
    characterViewModel.characterFull.collectAsStateWithLifecycle()
    val characterPicturesState by
    characterViewModel.picturesList.collectAsStateWithLifecycle()
    val painter = rememberAsyncImagePainter(model = characterFullState?.images?.jpg?.image_url)
    val svgImageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()
    val isDialogShown = remember { mutableStateOf(false) }
    if (isSearching.not() && characterFullState != null) {

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
                ShowCharacterPicture(
                    painter = painter,
                    modifier = modifier,
                    isDialogShown = isDialogShown
                )
                ShowNamesAndInteractionIcons(
                    data = characterFullState!!,
                    modifier = modifier,
                    imageLoader = svgImageLoader,
                    daoViewModel = daoViewModel,
                    characterViewModel = characterViewModel
                )
                ShowCharacterPictureAlbum(
                    isDialogShown = isDialogShown,
                    picturesData = characterPicturesState,
                    modifier = modifier
                )


            }
            Row {
                characterFullState?.about?.let { about ->
                    ExpandableText(text = about, title = "More Information", modifier = modifier)
                }
            }
            if (
                !characterFullState?.voices.isNullOrEmpty()
            ) {
                ShowVoiceActors(
                    modifier = modifier,
                    actors = characterFullState!!.voices,
                    navController = navController
                )

            }

            if (
                !characterFullState?.anime.isNullOrEmpty()
            ) {
                ShowAnimeRelated(
                    modifier = modifier,
                    animes = characterFullState?.anime!!,
                    navController = navController
                )

            }
//            characterFullState?.anime?.let { animeList ->
//                ShowAnimeRelated(
//                    modifier = modifier,
//                    animes = animeList,
//                    navController = navController
//                )
//
//            }

        }


    } else LoadingAnimation()
}

