package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.toko.detailScreen.model.detailModel.DetailData

@Composable
fun ShowStudios(detailData: DetailData?, navController: NavController) {
    if (detailData?.studios?.isNotEmpty() ==true) {
        Text(text = "STUDIOS:")
        detailData.studios.forEach {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it.name,
                fontSize = 40.sp,
                color = Color.Blue,
                fontStyle = FontStyle.Italic,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    println("detail_on_producer/${it.mal_id}/${it.name}")
                    navController.navigate(
                        "detail_on_producer/${it.mal_id}/${it.name}"
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }

}