package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.toko.core.presentation_layer.theme.LightGreen
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
                fontWeight = FontWeight.ExtraBold
            )

        }
        Row(modifier = modifier.height(20.dp), horizontalArrangement = Arrangement.Start) {

        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            Text(text = "Aired: ")
            Text(text = "from $airedFrom to $airedTo", color = LightGreen)
        }

        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val licensor = if (licensors?.isEmpty() == true)
                "N/A"
            else licensors
            Text(text = "Licensors: ")
            Text(text =  licensor.toString(), color = LightGreen)

        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val synonym = if (synonyms?.isEmpty() == true)
                "N/A"
            else synonyms
            Text(text = "Synonyms: ")
            Text(text =  synonym.toString(), color = LightGreen)
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val studio = if (studios?.isEmpty() == true)
                "N/A"
            else studios
            Text(text = "Studios: ")
            Text(text =  studio.toString(), color = LightGreen)
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            Text(text = "Source: ")
            Text(text = detailData?.source ?: "N/A", color = LightGreen)
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val demo = if (demographic?.isEmpty() == true)
                "N/A"
            else demographic
            Text(text = "Demographic: ")
            Text(text =  demo.toString(), color = LightGreen)

        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            Text(text = "Rating: ")
            Text(text = detailData?.rating ?: "N/A", minLines = 1, color = LightGreen)
        }
        Row {
            Text(text = "Broadcast: " )
            Text(text =  (detailData?.broadcast?.string ?: "N/A").toString(), color = LightGreen)
        }
        Row {
            val producer = if (demographic?.isEmpty() == true)
                "N/A"
            else producers
            Text(text = "Producers: ")
            Text(text =  producer.toString(), color = LightGreen)
        }


        Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
            val title = if (titles?.isEmpty() == true) {
                "N/A"
            } else titles
            Text(text = "Titles: ")
            Text(text =  title.toString(), color = LightGreen)

        }

    }
}