package com.project.toko.personDetailedScreen.ui.staffMemberFull


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.project.toko.core.ui.animations.LoadingAnimation
import com.project.toko.personDetailedScreen.ui.viewModel.PersonByIdViewModel
import com.project.toko.core.ui.expandableText.ExpandableText
import com.project.toko.core.ui.pullToRefpresh.PullToRefreshLayout
import com.project.toko.core.ui.theme.BackArrowCastColor
import com.project.toko.core.ui.theme.BackArrowSecondCastColor
import com.project.toko.core.ui.theme.DarkBackArrowCastColor
import com.project.toko.core.ui.theme.DarkBackArrowSecondCastColor
import com.project.toko.core.ui.theme.evolventaBoldFamily
import com.project.toko.savedScreen.ui.daoViewModel.DaoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun DisplayPersonFullScreen(
    id: Int,
    onNavigateToDetailScreen: (Int) -> Unit,
    onNavigateToDetailOnCharacter: (Int) -> Unit,
    onNavigateBack: () -> Unit,

    modifier: Modifier = Modifier, isInDarkTheme: () -> Boolean, svgImageLoader: ImageLoader
) {
    val personViewModel: PersonByIdViewModel = hiltViewModel()
    val daoViewModel: DaoViewModel = hiltViewModel()
    val context = LocalContext.current
    LaunchedEffect(id) {
        withContext(Dispatchers.IO) {
            personViewModel.loadAllInfo(id, context)
        }
    }
    val swipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = personViewModel.isLoading.value)
    val personFullState by
    personViewModel.personFull.collectAsStateWithLifecycle()
    val personPicturesState by
    personViewModel.picturesList.collectAsStateWithLifecycle()
    val painter = rememberAsyncImagePainter(model = personFullState?.images?.jpg?.image_url)

    val isDialogShown = remember { mutableStateOf(false) }
    if (personViewModel.isLoading.value.not() && personFullState != null) {
        PullToRefreshLayout(composable = {
            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = modifier
                        .height(100.dp)
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
                        ExpandableText(
                            text = about,
                            title = "More Information",
                            modifier = modifier
                        )
                    }
                }

                if (!personFullState?.voices.isNullOrEmpty()) {
                    ShowAnimeRelated(
                        modifier = modifier,
                        voices = personFullState?.voices!!,
                        onNavigateToDetailScreen = onNavigateToDetailScreen,
                        onNavigateToDetailOnCharacter = onNavigateToDetailOnCharacter
                    )

                }

                if (
                    !personFullState?.anime.isNullOrEmpty()
                ) {
                    ShowStaffPosition(
                        modifier = modifier,
                        animes = personFullState!!.anime,
                        onNavigateToDetailScreen = onNavigateToDetailScreen
                    )

                }
            }
            BackArrow(
                modifier,
                onNavigateBack, isInDarkTheme
            )
        }, onLoad = {
            personViewModel.viewModelScope.launch {
                personViewModel.loadAllInfo(id, context)
            }
        }, swipeRefreshState = swipeRefreshState)


    } else LoadingAnimation()
}

@Composable
private fun BackArrow(
    modifier: Modifier,
    onNavigateBack: () -> Unit,
    isInDarkTheme: () -> Boolean
) {
    val backArrowFirstColor = if (isInDarkTheme()) DarkBackArrowCastColor else BackArrowCastColor
    val backArrowSecondColor =
        if (isInDarkTheme()) DarkBackArrowSecondCastColor else BackArrowSecondCastColor
    Column {
        Spacer(modifier = modifier.height(40.dp))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(backArrowFirstColor)
        ) {
            Text(
                text = "   <    Person                       ",
                fontSize = 24.sp,
                textAlign = TextAlign.Start,
                textDecoration = TextDecoration.Underline,
                modifier = modifier.clickable {
                    onNavigateBack()
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