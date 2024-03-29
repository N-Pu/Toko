package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.project.toko.detailScreen.model.detailModel.DetailData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplayJapAndEnglishTitles(detailData: DetailData?, modifier: Modifier) {

    Log.d("https::// title jap and english ", (detailData?.title_english + " / " + detailData?.title_japanese).length.toString())
    Row(
        modifier = modifier
            .fillMaxWidth()
            .basicMarquee(
                iterations = Int.MAX_VALUE,
                delayMillis = 2000,
                initialDelayMillis = 2000,
                velocity = 50.dp
            )
            .padding(start = 20.dp, end = 20.dp)
        ,
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top
    ) {

        if (detailData?.title_english?.isNotEmpty() == true) {
            Text(
                text = detailData.title_english,
                minLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = " / ",
                minLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Text(
            text = detailData?.title_japanese ?: "",
            minLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}