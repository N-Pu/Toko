package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.detailScreen.model.detailModel.DetailData

@Composable
fun YearTypeEpisodesTimeStatusStudio(
    data: DetailData?,
    modifier: Modifier
) {
    val isStudioEmpty = data?.studios.isNullOrEmpty()
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(1f)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .padding(start = 20.dp, top = 5.dp, bottom = 0.dp, end = 20.dp)
                .fillMaxWidth(1f)
        ) {
            val yearSeasonText = buildAnnotatedString {
                if (data?.year != 0 && data?.season != null) {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                        append("${data.season} ${data.year}")
                    }
                    append("|")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                    append(data?.type ?: "N/A")
                }
                withStyle(style = SpanStyle(color = Color.Gray.copy(0.5f))) {
                    append("|")
                }
                if (data?.episodes != 0 && data?.duration != null) {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                        append("${data.episodes} ep/${data.duration}")
                    }
                    append("|")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                    append(data?.status ?: "N/A")
                }
                if (!isStudioEmpty) {
                    append("|")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                        append(data?.studios?.component1()?.name ?: "N/A")
                    }
                }
            }

            Text(
                text = yearSeasonText,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onPrimary.copy(0.5f),
                textAlign = TextAlign.Center
            )

        }
    }
}