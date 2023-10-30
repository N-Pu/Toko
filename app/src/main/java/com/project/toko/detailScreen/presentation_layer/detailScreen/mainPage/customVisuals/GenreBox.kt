package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.customVisuals

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.toko.core.presentation_layer.theme.LightGreen

@Composable
private fun ColorBox(
    text: String,
    modifier: Modifier
) {

    Box(
        modifier = modifier
            .background(LightGreen, shape = RoundedCornerShape(size =  14.dp) )
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(4.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DisplayCustomGenreBoxes(genres: List<com.project.toko.detailScreen.model.detailModel.Genre>, modifier: Modifier) {

    if (genres
        .isNotEmpty()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .height(80.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            genres.forEachIndexed { index, genre ->
                if (index != 0 || index == genres.size) {
                    Spacer(modifier = modifier.width(8.dp))
                }
                ColorBox(
                    text = genre.name,
                    modifier
                )
            }
        }
    }
}
