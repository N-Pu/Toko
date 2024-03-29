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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.toko.core.presentation_layer.theme.evolventaBoldFamily
import com.project.toko.detailScreen.model.detailModel.Entry
import com.project.toko.detailScreen.model.detailModel.Relation
import com.project.toko.homeScreen.presentation_layer.homeScreen.navigateToDetailScreen

@Composable
private fun ShowRelation(relation: String, modifier: Modifier) {
    Row(
        modifier = modifier.padding(start = 20.dp, bottom = 5.dp, end = 20.dp)
    ) {
        Text(
            fontWeight = FontWeight.Medium, text = "$relation: ",
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun CurrentRelation(
    modifier: Modifier,
    entry: Entry,
    navController: NavController,
) {

    if (entry.type != "manga") {
        Row(
            modifier = modifier.padding(start = 20.dp, bottom = 5.dp, end = 20.dp)
        ) {
            Text(
                color = MaterialTheme.colorScheme.secondary,
                text = entry.name + " (" + entry.type + ")",
                modifier = modifier.clickable {
                    navigateToDetailScreen {
                        navController.navigate(route = "detail_screen/${entry.mal_id}")
                        {
                            launchSingleTop = true
                        }
                    }
                })
        }
    } else {
        Row(
            modifier = modifier.padding(start = 20.dp, bottom = 5.dp)
        ) {
            Text(
                color = MaterialTheme.colorScheme.inversePrimary,
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
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Related anime",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = evolventaBoldFamily
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
                            CurrentRelation(
                                modifier, currentEntry,
                                navController
                            )
                        }
                    }
                }
            }

            if (relations.size > maxItemsToShow) {
                Column(
                    modifier = modifier
                        .fillMaxWidth(1f)
                        .padding(start = 20.dp, bottom = 20.dp, end = 20.dp)
                ) {
                    Spacer(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                    Spacer(
                        modifier = modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.inversePrimary.copy(0.3f))
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
                            color = MaterialTheme.colorScheme.inversePrimary
                        )
                        Icon(
                            imageVector = if (itemsToShow == maxItemsToShow) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = modifier.height(20.dp))
}
