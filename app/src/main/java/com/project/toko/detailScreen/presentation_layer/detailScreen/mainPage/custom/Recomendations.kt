package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.project.toko.core.presentation_layer.theme.DarkSectionColor
import com.project.toko.core.presentation_layer.theme.SectionColor
import com.project.toko.core.presentation_layer.theme.evolventaBoldFamily
import com.project.toko.detailScreen.model.recommendationsModel.RecommendationsData
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.homeScreen.presentation_layer.homeScreen.navigateToDetailScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Recommendations(
    recommendationsDataList: List<RecommendationsData>,
    navController: NavController,
    viewModelProvider: ViewModelProvider,
    modifier: Modifier,
    isInDarkTheme: Boolean
) {
    if (recommendationsDataList.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth(), horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = modifier
                    .fillMaxWidth(0.85f)
                    .background(if (isInDarkTheme) DarkSectionColor else SectionColor)
                    .padding(bottom = 5.dp)
            ) {
                Box(
                    modifier = modifier
                ) {
                    Text(
                        text = "    Anime Alike >  ",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Start,
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = evolventaBoldFamily
                    )
                }

            }
            Spacer(modifier = modifier.height(20.dp))
            Row(
                modifier = modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                recommendationsDataList.forEach { recommendationsData ->

                    val painter =
                        rememberAsyncImagePainter(model = recommendationsData.entry.images.jpg.large_image_url)
                    Spacer(modifier = modifier.width(20.dp))
                    SingleRecommendationCard(
                        modifier = modifier,
                        navController = navController,
                        detailScreenViewModel = viewModelProvider[DetailScreenViewModel::class.java],
                        recommendationsData = recommendationsData,
                        painter = painter
                    )
                }
                Spacer(modifier = modifier.width(20.dp))
            }
        }
    }
    Spacer(modifier = modifier.height(60.dp))

}


@Composable
fun SingleRecommendationCard(
    modifier: Modifier,
    navController: NavController,
    detailScreenViewModel: DetailScreenViewModel,
    recommendationsData: RecommendationsData,
    painter: AsyncImagePainter,

    ) {
    Card(
        modifier = modifier
            .width(180.dp)
            .shadow(20.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                detailScreenViewModel.viewModelScope.launch(Dispatchers.Main) {
                    navigateToDetailScreen(
                        navController, recommendationsData.entry.mal_id
                    )
                }
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onTertiaryContainer),
        shape = RectangleShape,
    ) {


        // Coil image loader
        Image(
            painter = painter,
            contentDescription = "Images for each Anime",
            modifier = modifier
                .aspectRatio(9f / 11f)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.FillBounds
        )


        Text(
            text = recommendationsData.entry.title,
            textAlign = TextAlign.Start,
            modifier = modifier
                .fillMaxWidth()
                .padding(end = 5.dp, top = 5.dp, bottom = 5.dp, start = 10.dp),
            lineHeight = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            minLines = 2,
            maxLines = 2,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = evolventaBoldFamily
        )


    }
}