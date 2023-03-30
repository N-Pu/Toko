package com.example.animeapp.presentation.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animeapp.domain.exampleModels.Images
import com.example.animeapp.domain.exampleModels.Webp
import com.example.animeapp.domain.searchModel.Data
import com.example.animeapp.domain.searchModel.Jpg

@Composable
fun CustomItem(data: Data) {


    Row(
        modifier = Modifier
            .background(color = Color.DarkGray)
            .padding(24.dp),

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


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CustomItem(
        data = Data(
            1,
            "DSDSDSDS",
            "BOOOOOO",
            Images(
                Jpg(
                    "S",
                    "S",
                    "s"
                ), Webp("b", "V", "ffd")
            )
        ),
    )
}