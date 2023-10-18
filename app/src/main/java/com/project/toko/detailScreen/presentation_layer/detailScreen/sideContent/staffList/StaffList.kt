package com.project.toko.detailScreen.presentation_layer.detailScreen.sideContent.staffList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.project.toko.core.presentation_layer.navigation.Screen
import com.project.toko.detailScreen.model.staffModel.StaffData
import java.lang.Integer.min

@Composable
fun DisplayStaff(
    staffList: List<StaffData>,
    navController: NavController,
    modifier: Modifier
) {
    if (staffList.isNotEmpty()) {
        ListEditor(listData = staffList, navController = navController, modifier = modifier)
    }
}


@Composable
private fun ListEditor(
    listData: List<StaffData>,
    navController: NavController,
    modifier: Modifier
) {
    val minListSize = min(12, listData.size)

    Row(
        modifier = modifier
            .fillMaxWidth(1f)
            .padding(start = 20.dp, bottom = 0.dp, end = 20.dp, top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Staff",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Column {
            Text(
                text = " $minListSize>",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .height(180.dp)
//            .background(Color.Red)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listData.take(12).forEach { data ->
            val personPainter = rememberAsyncImagePainter(model = data.person.images.jpg.image_url)

            Spacer(modifier = Modifier.size(20.dp))

            StaffComponentsCard(
                data = data,
                personPainter = personPainter,
                navController = navController,
                modifier = modifier
            )
        }
        Column(modifier = modifier.width(20.dp)) {
            // Пустая колонка для выравнивания
        }
        Column(
            modifier
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(5.dp))
                .width(160.dp)
                .height(160.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = modifier.size(100.dp)) {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(198, 198, 198))
                        .clickable {
                            navController.navigate(Screen.DetailOnStaff.route) {
                                popUpTo(Screen.Detail.route) {
                                    inclusive = true
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "More Staff",
                        modifier = modifier.width(140.dp)
                    )
                }
            }

            Text(
                text = "More Staff",
                textAlign = TextAlign.Left,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = modifier
            )
        }
        Column(modifier = modifier.width(20.dp)) {
            // Пустая колонка для выравнивания
        }
    }
}

@Composable
private fun StaffComponentsCard(
    modifier: Modifier,
    data: StaffData,
    personPainter: AsyncImagePainter,
    navController: NavController
) {
    val positions = data.positions.joinToString(separator = ", ")
    Row(
        modifier = modifier
            .width(320.dp)
            .height(160.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .padding(start = 22.dp, top = 22.dp)
                .fillMaxHeight(1f)
        ) {
            Image(
                painter = personPainter,
                contentDescription = "Staff member: ${data.person.name}",
                modifier = modifier
                    .clip(RoundedCornerShape(5.dp))
                    .width(70.dp)
                    .height(107.dp)
                    .clickable {
                        navController.navigate("detail_on_staff/${data.person.mal_id}") {
                            popUpTo(Screen.Detail.route) {
                                inclusive = true
                            }
                        }
                    },
                contentScale = ContentScale.FillBounds
            )

        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .fillMaxWidth(1f)
                .padding(start = 22.dp)
        ) {
            Row(
                modifier = modifier
                    .padding(top = 24.dp)
            ) {
                Text(
                    text = data.person.name,
                    modifier = modifier,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
            Row(
                modifier = modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(1f)
//                    .background(Color.Red)
                    .padding(end = 22.dp)
            ) {

                Text(
                    text = positions,
                    modifier = modifier,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 11.sp,
                    lineHeight = 10.sp
                )


            }
        }

    }
}