package com.project.toko.personDetailedScreen.presentation_layer.staffMemberFull


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.project.toko.core.connectionCheck.isInternetAvailable
import com.project.toko.core.presentation_layer.animations.LoadingAnimation
import com.project.toko.personDetailedScreen.viewModel.PersonByIdViewModel
import com.project.toko.core.presentation_layer.expandableText.ExpandableText
import com.project.toko.core.presentation_layer.theme.BackArrowCastColor
import com.project.toko.core.presentation_layer.theme.BackArrowSecondCastColor
import com.project.toko.core.presentation_layer.theme.DarkBackArrowCastColor
import com.project.toko.core.presentation_layer.theme.DarkBackArrowSecondCastColor
import com.project.toko.daoScreen.daoViewModel.DaoViewModel
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
    val context = LocalContext.current
    LaunchedEffect(mal_id) {
        if (isInternetAvailable(context)) {
            delay(300L)
            personViewModel.getPersonFromId(mal_id)
            delay(300L)
            personViewModel.getPicturesFromId(mal_id)
        } else {
            Toast.makeText(
                context,
                "No internet connection!",
                Toast.LENGTH_SHORT
            ).show()
        }
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
            modifier = modifier.background(MaterialTheme.colorScheme.primary)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(80.dp)
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
        BackArrow(
            modifier,
            navController
        )

    } else LoadingAnimation()
}

@Composable
private fun BackArrow(modifier: Modifier, navController: NavController) {
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
                text = "   <    Person                       ",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start,
                textDecoration = TextDecoration.Underline,
                modifier = modifier.clickable {
                    navController.popBackStack()
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