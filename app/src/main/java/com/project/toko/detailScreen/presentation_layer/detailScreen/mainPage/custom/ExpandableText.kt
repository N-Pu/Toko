package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpandableText(text: String, modifier: Modifier) {
    val wordCount = text.split(" ").count()
    var expanded by remember { mutableStateOf(false) }
    val toggleExpanded: () -> Unit = { expanded = !expanded }

    val maxLines = if (expanded) Int.MAX_VALUE else 4

    if (wordCount <= 20) {
        Column(
            modifier = modifier
                .fillMaxWidth(1f)
                .padding(start = 20.dp, bottom = 25.dp, end = 20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = modifier.height(10.dp))
            Text(
                text = text,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier,
                softWrap = true
            )
        }
        return
    }



    Column(
        modifier = modifier
            .fillMaxWidth(1f)
            .padding(start = 20.dp, bottom = 0.dp, end = 20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Synopsis",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = modifier.height(10.dp))
        Text(
            text = text,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .clickable(onClick = toggleExpanded)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
            softWrap = true
        )
        Spacer(modifier = modifier
            .fillMaxWidth()
            .height(10.dp))
        Spacer(modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(0.3f))
            .height(1.dp))
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Details",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = modifier.clickable(onClick = toggleExpanded),
                color = Color(0xB2000000)
            )
            if (expanded) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = modifier
                        .clickable(onClick = toggleExpanded)
                        .align(Alignment.CenterVertically)

                )
            } else {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = modifier
                        .clickable(onClick = toggleExpanded)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}
