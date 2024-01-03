package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.toko.core.presentation_layer.theme.evolventaBoldFamily
import com.project.toko.detailScreen.model.detailModel.DetailData

@Composable
fun ShowBackground(detailData: DetailData?, modifier: Modifier) {
    if (detailData?.background?.isNotEmpty() == true) {

        Column(modifier = modifier.padding(start = 20.dp, end = 20.dp)) {

            Text(
                text = "Background",
                color = MaterialTheme.colorScheme.onPrimary,
                lineHeight = 16.sp, fontSize = 22.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = evolventaBoldFamily
            )

            Spacer(modifier = Modifier.height(20.dp))


            Text(
                text = detailData.background,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}