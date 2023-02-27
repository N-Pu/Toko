package com.example.animeapp.presentation.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animeapp.ui.theme.domain.models.Data

@Composable
fun CustomItem(data: Data) {


    Row(
        modifier = Modifier
            .background(color = Color.DarkGray)
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = data.title,
            color = Color.Cyan,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Episodes: " + data.episodes.toString(),
            color = Color.White,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif
        )
        Text(
            text = data.rating,
            color = Color.Magenta,
            fontWeight = FontWeight.Normal
        )


    }

}

@Composable
@Preview(showBackground = true)
fun CustomItemPreview() {

    CustomItem(
        data = Data(
            title = "Steins;Gate",
            episodes = 24,
            rating = "9/10"
        )
    )

}

