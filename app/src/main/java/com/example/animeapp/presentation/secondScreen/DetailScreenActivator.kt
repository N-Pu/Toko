package com.example.animeapp.presentation.secondScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.animeapp.viewModel.DetailScreenViewModel


@Composable
fun ActivateDitailScreen(id: Int) {
    DetailScreenViewModel.onTapAnime(id)
    val viewModel = viewModel<DetailScreenViewModel>()

    val animeDetailData = viewModel.animeDetails.collectAsStateWithLifecycle()

    animeDetailData.value?.let {
        Card(modifier = Modifier.fillMaxSize()) {
            Text(text = id.toString())
            Text(
                text = it.title,
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
        }
      
    }

}