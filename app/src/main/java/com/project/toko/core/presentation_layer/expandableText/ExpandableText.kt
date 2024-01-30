package com.project.toko.core.presentation_layer.expandableText

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.toko.core.presentation_layer.theme.evolventaBoldFamily

//@Composable
//fun ExpandableText(text: String?, title: String, modifier: Modifier) {
//    val wordCount = text?.split(Regex("\\s+"))?.size
//    var expanded by remember { mutableStateOf(false) }
//    val maxLines = if (expanded) Int.MAX_VALUE else 6
//    val toggleExpanded: () -> Unit = { expanded = !expanded }
//    Log.d("https://synopsys NUM", text?.length.toString())
//    if (wordCount != null) {
//        if (wordCount <= 40) {
//            Column(
//                modifier = modifier
//                    .fillMaxWidth(1f)
//                    .padding(start = 20.dp, bottom = 25.dp, end = 20.dp, top = 20.dp),
//                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.Top
//            ) {
//                Text(
//                    text = title,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.W900,
//                    color = MaterialTheme.colorScheme.onPrimary,
//                    fontFamily = evolventaBoldFamily
//                )
//                Spacer(modifier = modifier.height(20.dp))
//                Text(
//                    text = text,
//                    maxLines = maxLines,
//                    overflow = TextOverflow.Ellipsis,
//                    modifier = modifier,
//                    softWrap = true,
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//            return
//        }
//
//
//
//
//        Column(
//            modifier = modifier
//                .fillMaxWidth(1f)
//                .padding(start = 20.dp, bottom = 0.dp, end = 20.dp, top = 20.dp),
//            horizontalAlignment = Alignment.Start,
//            verticalArrangement = Arrangement.Top
//        ) {
//            Text(
//                text = title,
//                fontSize = 24.sp,
//                fontWeight = FontWeight.W900,
//                color = MaterialTheme.colorScheme.onPrimary,
//                fontFamily = evolventaBoldFamily
//            )
//            Spacer(modifier = modifier.height(20.dp))
//            Text(
//                text = text,
//                maxLines = maxLines,
//                overflow = TextOverflow.Ellipsis,
//                modifier = modifier
//                    .clickable(onClick = toggleExpanded)
//                    .animateContentSize(
//                        animationSpec = spring(
//                            dampingRatio = Spring.DampingRatioLowBouncy,
//                            stiffness = Spring.StiffnessLow
//                        )
//                    ),
//                softWrap = true,
//                color = MaterialTheme.colorScheme.onPrimary
//            )
//            Spacer(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .height(10.dp)
//            )
//            Spacer(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.inversePrimary.copy(0.3f))
//                    .height(1.dp)
//            )
//            Row(
//                modifier = Modifier,
//                horizontalArrangement = Arrangement.Start,
//                verticalAlignment = Alignment.Top
//            ) {
//                Text(
//                    text = "Details",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Medium,
//                    modifier = modifier.clickable(onClick = toggleExpanded),
//                    color = MaterialTheme.colorScheme.inversePrimary
//                )
//
//                if (expanded) {
//                    Icon(
//                        imageVector = Icons.Filled.KeyboardArrowUp,
//                        contentDescription = null,
//                        modifier = modifier
//                            .clickable(onClick = toggleExpanded)
//                            .align(Alignment.CenterVertically),
//                        tint = MaterialTheme.colorScheme.inversePrimary
//
//                    )
//                } else {
//                    Icon(
//                        imageVector = Icons.Filled.KeyboardArrowDown,
//                        contentDescription = null,
//                        modifier = modifier
//                            .clickable(onClick = toggleExpanded)
//                            .align(Alignment.CenterVertically),
//                        tint = MaterialTheme.colorScheme.inversePrimary
//                    )
//                }
//            }
//            Spacer(modifier = modifier.height(15.dp))
//        }
//    }
//}


//@Composable
//fun ExpandableText(text: String?, title: String, modifier: Modifier) {
//    val wordCount = text?.split(" ")?.count()
//    var expanded by remember { mutableStateOf(false) }
//    val toggleExpanded: () -> Unit = { expanded = !expanded }
//
//    if (wordCount != null) {
//        Column(
//            modifier = modifier
//                .fillMaxWidth(1f)
//                .padding(start = 20.dp, bottom = 25.dp, end = 20.dp, top = 20.dp),
//            horizontalAlignment = Alignment.Start,
//            verticalArrangement = Arrangement.Top
//        ) {
//            Text(
//                text = title,
//                fontSize = 24.sp,
//                fontWeight = FontWeight.W900,
//                color = MaterialTheme.colorScheme.onPrimary,
//                fontFamily = evolventaBoldFamily
//            )
//            Spacer(modifier = modifier.height(20.dp))
//            if (wordCount <= 40) {
//                // If word count is less than or equal to 40, display all the text
//                Text(
//                    text = text,
//                    modifier = modifier,
//                    softWrap = true,
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//            } else {
//                // If word count is more than 40, display with max lines and toggle option
//                Text(
//                    text = text,
//                    maxLines = if (expanded) Int.MAX_VALUE else 6,
//                    overflow = TextOverflow.Ellipsis,
//                    modifier = modifier
//                        .clickable(onClick = toggleExpanded)
//                        .animateContentSize(
//                            animationSpec = spring(
//                                dampingRatio = Spring.DampingRatioLowBouncy,
//                                stiffness = Spring.StiffnessLow
//                            )
//                        ),
//                    softWrap = true,
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//
//            Spacer(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .height(10.dp)
//            )
//            Spacer(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.inversePrimary.copy(0.3f))
//                    .height(1.dp)
//            )
//            Row(
//                modifier = Modifier,
//                horizontalArrangement = Arrangement.Start,
//                verticalAlignment = Alignment.Top
//            ) {
//                Text(
//                    text = "Details",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Medium,
//                    modifier = modifier.clickable(onClick = toggleExpanded),
//                    color = MaterialTheme.colorScheme.inversePrimary
//                )
//
//                if (expanded) {
//                    Icon(
//                        imageVector = Icons.Filled.KeyboardArrowUp,
//                        contentDescription = null,
//                        modifier = modifier
//                            .clickable(onClick = toggleExpanded)
//                            .align(Alignment.CenterVertically),
//                        tint = MaterialTheme.colorScheme.inversePrimary
//
//                    )
//                } else {
//                    Icon(
//                        imageVector = Icons.Filled.KeyboardArrowDown,
//                        contentDescription = null,
//                        modifier = modifier
//                            .clickable(onClick = toggleExpanded)
//                            .align(Alignment.CenterVertically),
//                        tint = MaterialTheme.colorScheme.inversePrimary
//                    )
//                }
//            }
//            Spacer(modifier = modifier.height(15.dp))
//        }
//
//
//    }
//}


@Composable
fun ExpandableText(text: String?, title: String, modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val toggleExpanded: () -> Unit = { expanded = !expanded }

    if (text != null) {
        Column(
            modifier = modifier
                .clickable(onClick = toggleExpanded)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .fillMaxWidth()
                .padding(start = 20.dp, bottom = 25.dp, end = 20.dp, top = 20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.W900,
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = evolventaBoldFamily
            )
            Spacer(modifier = modifier.height(20.dp))
                Text(
                    text = text,
                    modifier = modifier,
                    softWrap = true,
                    color = MaterialTheme.colorScheme.onPrimary
                )
        }
    }
}

//@Composable
//fun ExpandableText(text: String?, title: String, modifier: Modifier) {
//    var expanded by remember { mutableStateOf(false) }
//
//    if (text != null) {
//
//        val wordCount = text.split(" ").count()
//        val maxLines = if (expanded) Int.MAX_VALUE else 6
//
//        if (wordCount <= 40) {
//            Column(
//                modifier = modifier
//                    .fillMaxWidth(1f)
//                    .padding(start = 20.dp, bottom = 25.dp, end = 20.dp, top = 20.dp),
//                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.Top
//            ) {
//                Text(
//                    text = title,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.W900,
//                    color = MaterialTheme.colorScheme.onPrimary,
//                    fontFamily = evolventaBoldFamily
//                )
//                Spacer(modifier = modifier.height(20.dp))
//                Text(
//                    text = text,
//                    maxLines = maxLines,
//                    overflow = TextOverflow.Ellipsis,
//                    modifier = modifier,
//                    softWrap = true,
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//            return
//        }
//
//
//
//        Column(
//            modifier = modifier
//                .clickable(onClick = { expanded = !expanded })
//                .animateContentSize(
//                    animationSpec = spring(
//                        dampingRatio = Spring.DampingRatioLowBouncy,
//                        stiffness = Spring.StiffnessLow
//                    )
//                )
//                .fillMaxWidth()
//                .padding(start = 20.dp, bottom = 25.dp, end = 20.dp, top = 20.dp),
//            horizontalAlignment = Alignment.Start,
//            verticalArrangement = Arrangement.Top
//        ) {
//            Text(
//                text = title,
//                fontSize = 24.sp,
//                fontWeight = FontWeight.W900,
//                color = MaterialTheme.colorScheme.onPrimary,
//                fontFamily = evolventaBoldFamily
//            )
//            Spacer(modifier = modifier.height(20.dp))
//            if (expanded) {
//                // If word count is less than or equal to 40, display all the text
//                Text(
//                    text = text,
//                    modifier = modifier,
//                    softWrap = true,
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//            } else {
//                // If word count is more than 40, display with max lines and toggle option
//                Text(
//                    text = text,
//                    maxLines = 6,
//                    overflow = TextOverflow.Ellipsis,
//                    softWrap = true,
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//
//            Spacer(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .height(10.dp)
//            )
//            Spacer(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.inversePrimary.copy(0.3f))
//                    .height(1.dp)
//            )
//            Row(
//                modifier = Modifier,
//                horizontalArrangement = Arrangement.Start,
//                verticalAlignment = Alignment.Top
//            ) {
//                Text(
//                    text = "Details",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = MaterialTheme.colorScheme.inversePrimary
//                )
//
//                if (expanded) {
//                    Icon(
//                        imageVector = Icons.Filled.KeyboardArrowUp,
//                        contentDescription = null,
//                        modifier = modifier
//                            .align(Alignment.CenterVertically),
//                        tint = MaterialTheme.colorScheme.inversePrimary
//
//                    )
//                } else {
//                    Icon(
//                        imageVector = Icons.Filled.KeyboardArrowDown,
//                        contentDescription = null,
//                        modifier = modifier
//                            .align(Alignment.CenterVertically),
//                        tint = MaterialTheme.colorScheme.inversePrimary
//                    )
//                }
//            }
//            Spacer(modifier = modifier.height(15.dp))
//        }
//
//
//    }
//}