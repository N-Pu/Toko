package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.toko.core.presentation_layer.theme.evolventaBoldFamily
import com.project.toko.detailScreen.model.detailModel.DetailData

@Composable
fun ShowMoreInformation(
    modifier: Modifier,
    detailData: DetailData?
) {

    val licensors = detailData?.licensors?.joinToString(", ") { licensor -> licensor.name }
    val studios = detailData?.studios?.joinToString(", ") { studio -> studio.name }
    val producers = detailData?.producers?.joinToString(", ") { producer -> producer.name }
    val titles = detailData?.titles?.joinToString(", ") { title -> title.title }
    val demographic = detailData?.demographics?.joinToString(", ") { demo -> demo.name }
    val synonyms = detailData?.title_synonyms?.joinToString(", ") { it }
    val airedFrom = detailData?.aired?.from?.dropLast(15) ?: "N/A"
    val airedTo = detailData?.aired?.to?.dropLast(15) ?: "N/A"

    Column(
        modifier = modifier
            .fillMaxWidth(1f)
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {

            Text(
                text = "More Information",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = evolventaBoldFamily
            )

        }
        Row(modifier = modifier.height(20.dp), horizontalArrangement = Arrangement.Start) {

        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            Text(
                text = "Aired: ",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "from $airedFrom to $airedTo",
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val licensor = if (licensors?.isEmpty() == true)
                "N/A"
            else licensors
            Text(
                text = "Licensors: ",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(text = licensor.toString(), color = MaterialTheme.colorScheme.secondary)

        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val synonym = if (synonyms?.isEmpty() == true)
                "N/A"
            else synonyms
            Text(
                text = "Synonyms: ",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(text = synonym.toString(), color = MaterialTheme.colorScheme.secondary)
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val studio = if (studios?.isEmpty() == true)
                "N/A"
            else studios
            Text(
                text = "Studios: ",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(text = studio.toString(), color = MaterialTheme.colorScheme.secondary)
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            Text(
                text = "Source: ",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(text = detailData?.source ?: "N/A", color = MaterialTheme.colorScheme.secondary)
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val demo = if (demographic?.isEmpty() == true)
                "N/A"
            else demographic
            Text(
                text = "Demographic: ",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(text = demo.toString(), color = MaterialTheme.colorScheme.secondary)

        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            Text(
                text = "Rating: ",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = detailData?.rating ?: "N/A",
                minLines = 1,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Row {
            Text(
                text = "Broadcast: ",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = (detailData?.broadcast?.string ?: "N/A").toString(),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Row {
            val producer = if (demographic?.isEmpty() == true)
                "N/A"
            else producers
            Text(
                text = "Producers: ",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(text = producer.toString(), color = MaterialTheme.colorScheme.secondary)
        }


        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val title = if (titles?.isEmpty() == true) {
                "N/A"
            } else titles
            Text(
                text = "Titles: ",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(text = title.toString(), color = MaterialTheme.colorScheme.secondary)

        }

    }
}