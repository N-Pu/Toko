package com.example.animeapp.presentation.detailScreen.sideContent.characterFull

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
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
import com.example.animeapp.domain.characterPictures.Data
import com.example.animeapp.viewModel.CharacterFullByIdViewModel
import com.example.animeapp.viewModel.CharacterPicturesViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.animeapp.R
import com.example.animeapp.presentation.animations.LoadingAnimation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



@Composable
fun DisplayCharacterFromId(
    mal_id: Int, navController: NavController, viewModelProvider: ViewModelProvider
) {


    LaunchedEffect(mal_id) {
        withContext(Dispatchers.IO) {
            viewModelProvider[CharacterFullByIdViewModel::class.java].getCharacterFromId(mal_id)
            viewModelProvider[CharacterPicturesViewModel::class.java].getPicturesFromId(mal_id)
        }


    }


    val isSearching by viewModelProvider[CharacterFullByIdViewModel::class.java].isSearching.collectAsStateWithLifecycle()
    val characterFullState =
        viewModelProvider[CharacterFullByIdViewModel::class.java].characterFull.collectAsStateWithLifecycle()
    val painter =
        rememberAsyncImagePainter(model = characterFullState.value?.images?.jpg?.image_url)


    val characterPicturesState =
        viewModelProvider[CharacterPicturesViewModel::class.java].picturesList.collectAsStateWithLifecycle()



    if (isSearching.not()) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {


//            if (characterPicturesState.value.isNotEmpty()) {
            DisplayAlbumHorizontally(
                painterList = characterPicturesState.value
            )
//            } else {
//                Image(
//                    painter = painter,
//                    contentDescription = "Character name: ${characterFullState.value?.name}",
//                    modifier = Modifier
//                        .size(400.dp)
//                        .aspectRatio(9 / 11f)
//                )
//
//            }


            Spacer(modifier = Modifier.size(28.dp))
            characterFullState.value?.let { data ->
                DisplayKanjiAndEnglishName(data = data)

            }

            // writes null in ui if there's no "about" data
            characterFullState.value?.about?.let { about ->
                Text(
                    text = about, textAlign = TextAlign.Center
                )
            }

//            Text(text = "Also known as:")
//            characterFullState.value?.nicknames?.forEach {
//                Text(text = it, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
//            }

            characterFullState.value?.anime?.forEach { anime ->
                Text(
                    text = anime.anime.title,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = anime.role,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    } else {
        LoadingAnimation()
    }
}


//}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplayAlbumHorizontally(painterList: List<Data>) {
    val pagerState = rememberPagerState(initialPage = painterList.size)
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.size(400.dp),
        pageCount = painterList.size,
        reverseLayout = true
    ) { page ->
        val painter = rememberAsyncImagePainter(model = painterList[page].jpg.image_url)
        Log.d("pic", painterList[page].jpg.image_url)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(400.dp)
                .aspectRatio(9 / 11f),
        )
    }
}


@Composable
fun DisplayKanjiAndEnglishName(data: com.example.animeapp.domain.characterModel.Data) {
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

