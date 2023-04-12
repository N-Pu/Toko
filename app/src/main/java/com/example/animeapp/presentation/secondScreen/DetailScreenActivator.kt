package com.example.animeapp.presentation.secondScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.animeapp.viewModel.DetailScreenViewModel


@SuppressLint("SuspiciousIndentation")
@Composable
fun ActivateDetailScreen(id: Int) {
    DetailScreenViewModel.onTapAnime(id)

    val viewModel = viewModel<DetailScreenViewModel>()
    val animeDetailData = viewModel.animeDetails.collectAsStateWithLifecycle()


    val scrollState = rememberScrollState()
    animeDetailData.value?.let { data ->


        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            val painterState = rememberAsyncImagePainter(model = data.images.webp.large_image_url)
            DisplayPicture(painter = painterState)



            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = data.title,
                fontSize = 50.sp,
                modifier = Modifier,
                fontWeight = FontWeight.SemiBold,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
//            data.synopsis?.let { Text(text = it) }
            Text(text = data.synopsis ?: "")
        }


    }


}









