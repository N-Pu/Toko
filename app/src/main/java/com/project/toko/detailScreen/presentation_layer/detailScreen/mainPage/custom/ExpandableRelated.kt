package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.detailScreen.model.detailModel.Entry
import com.project.toko.detailScreen.model.detailModel.Relation
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.homeScreen.presentation_layer.homeScreen.navigateToDetailScreen
import kotlinx.coroutines.launch

@Composable
fun ShowRelation(relation: String, modifier: Modifier) {
    Row(
        modifier = modifier.padding(start = 20.dp, bottom = 5.dp, end = 20.dp)
    ) {
        Text(fontWeight = FontWeight.Medium, text = "$relation: ")
    }
}

@Composable
fun CurrentRelation(
    modifier: Modifier,
    entry: Entry,
    viewModel: DetailScreenViewModel,
    navController: NavController,
) {

    if (entry.type != "manga") {
        Row(
            modifier = modifier.padding(start = 20.dp, bottom = 5.dp, end = 20.dp)
        ) {
            Text(color = LightGreen,
                text = entry.name + " (" + entry.type + ")",
                modifier = modifier.clickable {
                    viewModel.viewModelScope.launch {
                        navigateToDetailScreen(navController, entry.mal_id)
                    }
                })
        }
    } else {
        Row(
            modifier = modifier.padding(start = 20.dp, bottom = 5.dp)
        ) {
            Text(
                color = Color.LightGray,
                text = entry.name + " (" + entry.type + ")",
                modifier = modifier
            )
        }
    }
}

@Composable
fun ExpandableRelated(
    relations: List<Relation>?,
    modifier: Modifier,
    navController: NavController,
    viewModel: DetailScreenViewModel
) {
    val maxItemsToShow = 2
    var itemsToShow by remember { mutableIntStateOf(maxItemsToShow) }
    val toggleExpanded: () -> Unit = {
        itemsToShow = if (itemsToShow == maxItemsToShow) {
            relations?.size ?: 0
        } else {
            maxItemsToShow
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween) {
        if (relations?.isNotEmpty() == true) {
            Box(modifier = modifier.height(20.dp))

            Box(
                modifier = modifier
                    .padding(start = 20.dp, bottom = 5.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Related anime",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start
                )
            }

            if (relations.isNotEmpty()) {
                // Apply animateContentSize to the content that should expand or collapse
                Column(
                    modifier = modifier.animateContentSize(),
                ) {
                    relations.take(itemsToShow).forEach { related ->
                        ShowRelation(relation = related.relation, modifier = modifier)
                        related.entry.forEach { currentEntry ->
                            CurrentRelation(modifier, currentEntry, viewModel, navController)
                        }
                    }
                }
            }

            if (relations.size > maxItemsToShow) {
                Column(
                    modifier = modifier
                        .fillMaxWidth(1f)
                        .padding(start = 20.dp, bottom = 25.dp, end = 20.dp)
                ) {
                    Spacer(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                    Spacer(
                        modifier = modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(0.3f))
                            .height(1.dp)
                    )
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .clickable(onClick = toggleExpanded),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Details",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = modifier.clickable(onClick = toggleExpanded),
                            color = Color(0xB2000000)
                        )
                        Icon(
                            imageVector = if (itemsToShow == maxItemsToShow) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}
