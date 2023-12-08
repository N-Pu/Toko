package com.project.toko.characterDetailedScreen.presentation_layer.characterFull

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.project.toko.characterDetailedScreen.viewModel.CharacterFullByIdViewModel
import com.project.toko.core.connectionCheck.isInternetAvailable
import com.project.toko.core.presentation_layer.animations.LoadingAnimation
import com.project.toko.core.presentation_layer.expandableText.ExpandableText
import com.project.toko.core.presentation_layer.theme.BackArrowCastColor
import com.project.toko.core.presentation_layer.theme.BackArrowSecondCastColor
import com.project.toko.core.presentation_layer.theme.DarkBackArrowCastColor
import com.project.toko.core.presentation_layer.theme.DarkBackArrowSecondCastColor
import com.project.toko.core.presentation_layer.theme.evolventaBoldFamily
import com.project.toko.daoScreen.daoViewModel.DaoViewModel
import kotlinx.coroutines.delay


@Composable
fun DisplayCharacterFromId(
    mal_id: Int,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier, isInDarkTheme : Boolean
) {
    val characterViewModel = viewModelProvider[CharacterFullByIdViewModel::class.java]
    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    val context = LocalContext.current
    LaunchedEffect(mal_id) {
        if (isInternetAvailable(context)) {
            delay(300L)
            characterViewModel.getCharacterFromId(mal_id)
            delay(300L)
            characterViewModel.getPicturesFromId(mal_id)
        } else {
            Toast.makeText(
                context,
                "No internet connection!",
                Toast.LENGTH_SHORT
            ).show()
        }
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
            modifier = modifier.background(MaterialTheme.colorScheme.primary)
                .verticalScroll(rememberScrollState())
        ) {
            Row(modifier = modifier.height(80.dp)) {}
            Row(
                modifier = modifier
                    .height(250.dp)
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
        }
        BackArrow(modifier, navController,isInDarkTheme)

    } else LoadingAnimation()
}


@Composable
private fun BackArrow(modifier: Modifier, navController: NavController, isInDarkTheme: Boolean) {
    val backArrowFirstColor = if (isInDarkTheme) DarkBackArrowCastColor else BackArrowCastColor
    val backArrowSecondColor =if (isInDarkTheme) DarkBackArrowSecondCastColor else BackArrowSecondCastColor
    Column {
        Spacer(modifier = modifier.height(20.dp))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(backArrowFirstColor)
        ) {
            Text(
                text = "   <    Character                    ",
                fontSize = 24.sp,
                textAlign = TextAlign.Start,
                textDecoration = TextDecoration.Underline,
                modifier = modifier.clickable {
                    navController.popBackStack()
                },
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = evolventaBoldFamily,
                fontWeight = FontWeight.W900
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

