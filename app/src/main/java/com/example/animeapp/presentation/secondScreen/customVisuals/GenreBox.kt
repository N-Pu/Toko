package com.example.animeapp.presentation.secondScreen.customVisuals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animeapp.domain.detailModel.Genre
import kotlin.random.Random


@Composable
fun RandomColorBox(
    text: String, color: Color, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color, shape = MaterialTheme.shapes.small)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun DisplayCustomGenreBoxes(genres: List<Genre>) {
    val colors = rememberSaveable {
        List(genres.size) {
            Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
        }
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        itemsIndexed(colors.take(genres.size)) { index, color ->
            if (index != 0) Spacer(modifier = Modifier.width(8.dp))
            RandomColorBox(text = genres[index].name, color = color)
        }
    }


}


@Preview
@Composable
fun PrevDisplayCustomGenreBoxes() {
    val genres = listOf("Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Thriller")
//    DisplayCustomGenreBoxes(genres = genres)
}