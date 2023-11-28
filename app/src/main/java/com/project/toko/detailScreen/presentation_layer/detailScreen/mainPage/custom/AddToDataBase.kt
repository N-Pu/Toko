package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.project.toko.R
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.daoScreen.daoViewModel.DaoViewModel
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.daoScreen.model.AnimeListType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun AddToFavorites(
    viewModelProvider: ViewModelProvider, modifier: Modifier
) {
    val detailScreenViewModel = viewModelProvider[DetailScreenViewModel::class.java]
    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    val detailScreenState by detailScreenViewModel.animeDetails.collectAsStateWithLifecycle()
    var isExpanded by remember { mutableStateOf(false) }
    val svgImageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()

    Row(
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = {
            detailScreenViewModel.viewModelScope.launch {
                if (daoViewModel.containsItemIdInCategory(
                        detailScreenState?.mal_id ?: 0,
                        AnimeListType.FAVORITE.route
                    ).first()
                ) {
                    daoViewModel.removeFromDataBase(
                        AnimeItem(
                            id = detailScreenState?.mal_id,
                            animeName = detailScreenState?.title ?: "",
                            score = formatScore(detailScreenState?.score),
                            scored_by = formatScoredBy(detailScreenState?.scored_by ?: 0.0f),
                            animeImage = detailScreenState?.images?.jpg?.large_image_url ?: "",
                            category = AnimeListType.FAVORITE.route,
                            status = detailScreenState?.status ?: "",
                            rating = detailScreenState?.rating ?: "",
                            secondName = detailScreenState?.title_japanese ?: "",
                            airedFrom = detailScreenState?.aired?.from ?: "N/A",
                            type = detailScreenState?.type ?: "N/A"
                        )
                    )
                } else {
                    daoViewModel.addToCategory(
                        AnimeItem(
                            id = detailScreenState?.mal_id,
                            animeName = detailScreenState?.title ?: "",
                            score = formatScore(detailScreenState?.score),
                            scored_by = formatScoredBy(detailScreenState?.scored_by ?: 0.0f),
                            animeImage = detailScreenState?.images?.jpg?.large_image_url ?: "",
                            category = AnimeListType.FAVORITE.route,
                            status = detailScreenState?.status ?: "",
                            rating = detailScreenState?.rating ?: "",
                            secondName = detailScreenState?.title_japanese ?: "",
                            airedFrom = detailScreenState?.aired?.from ?: "N/A",
                            type = detailScreenState?.type ?: "N/A"
                        )
                    )
                }
            }
        }
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.drawable.star, imageLoader = svgImageLoader
                ),
                contentDescription = null,
                colorFilter = if (daoViewModel.containsItemIdInCategory(
                        id = detailScreenState?.mal_id ?: 0,
                        AnimeListType.FAVORITE.route
                    ).collectAsStateWithLifecycle(initialValue = false).value
                ) ColorFilter.tint(LightGreen) else null
            )
        }
        IconButton(onClick = {
            detailScreenViewModel.viewModelScope.launch {
                if (daoViewModel.containsItemIdInCategory(
                        detailScreenState?.mal_id ?: 0,
                        AnimeListType.PLANNED.route
                    ).first()
                ) {
                    daoViewModel.removeFromDataBase(
                        AnimeItem(
                            id = detailScreenState?.mal_id,
                            animeName = detailScreenState?.title ?: "",
                            score = formatScore(detailScreenState?.score),
                            scored_by = formatScoredBy(detailScreenState?.scored_by ?: 0.0f),
                            animeImage = detailScreenState?.images?.jpg?.large_image_url ?: "",
                            category = AnimeListType.PLANNED.route,
                            status = detailScreenState?.status ?: "",
                            rating = detailScreenState?.rating ?: "",
                            secondName = detailScreenState?.title_japanese ?: "",
                            airedFrom = detailScreenState?.aired?.from ?: "N/A",
                            type = detailScreenState?.type ?: "N/A"
                        )
                    )
                } else {
                    daoViewModel.addToCategory(
                        AnimeItem(
                            id = detailScreenState?.mal_id,
                            animeName = detailScreenState?.title ?: "",
                            score = formatScore(detailScreenState?.score),
                            scored_by = formatScoredBy(detailScreenState?.scored_by ?: 0.0f),
                            animeImage = detailScreenState?.images?.jpg?.large_image_url ?: "",
                            category = AnimeListType.PLANNED.route,
                            status = detailScreenState?.status ?: "",
                            rating = detailScreenState?.rating ?: "",
                            secondName = detailScreenState?.title_japanese ?: "",
                            airedFrom = detailScreenState?.aired?.from ?: "N/A",
                            type = detailScreenState?.type ?: "N/A"
                        )

                    )
                }
            }

        }) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.drawable.bookmarkfilled, imageLoader = svgImageLoader
                ),
                contentDescription = null,
                colorFilter = if (daoViewModel.containsItemIdInCategory(
                        id = detailScreenState?.mal_id ?: 0,
                        AnimeListType.PLANNED.route
                    ).collectAsStateWithLifecycle(initialValue = false).value
                ) ColorFilter.tint(LightGreen) else null
            )
        }
        IconButton(onClick = {
        }) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.drawable.links, imageLoader = svgImageLoader
                ), contentDescription = null
            )
        }
        IconButton(
            onClick = {
                detailScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
                    isExpanded =
                        !isExpanded
                }

            },
            colors = IconButtonDefaults.iconButtonColors(containerColor = LightGreen),
            modifier = modifier
                .height(50.dp)
                .width(50.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.drawable.threedots, imageLoader = svgImageLoader
                ), contentDescription = null
            )
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }, modifier = modifier
                    .height(120.dp)
                    .width(170.dp)
                    .background(Color(65, 65, 65))
            ) {
                DropdownMenuItem(text = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth(1f)
                    ) {
                        Text(
                            text = "Completed",
                            color = if (daoViewModel.containsItemIdInCategory(
                                    id = detailScreenState?.mal_id ?: 0,
                                    AnimeListType.COMPLETED.route

                                ).collectAsStateWithLifecycle(initialValue = false).value
                            ) LightGreen else Color.White,
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,

                            )
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.eyewhite, imageLoader = svgImageLoader
                            ), contentDescription = null, modifier = modifier.size(22.dp),
                            colorFilter =
                            if (daoViewModel.containsItemIdInCategory(
                                    id = detailScreenState?.mal_id ?: 0,
                                    AnimeListType.COMPLETED.route
                                ).collectAsStateWithLifecycle(initialValue = false).value
                            ) ColorFilter.tint(LightGreen) else null
                        )
                    }
                }, onClick = {


                    detailScreenViewModel.viewModelScope.launch {

                        if (daoViewModel.containsItemIdInCategory(
                                detailScreenState?.mal_id ?: 0,
                                AnimeListType.COMPLETED.route
                            ).first()
                        ) {
                            daoViewModel.removeFromDataBase(
                                AnimeItem(
                                    id = detailScreenState?.mal_id,
                                    animeName = detailScreenState?.title ?: "",
                                    score = formatScore(detailScreenState?.score),
                                    scored_by = formatScoredBy(
                                        detailScreenState?.scored_by ?: 0.0f
                                    ),
                                    animeImage = detailScreenState?.images?.jpg?.large_image_url
                                        ?: "",
                                    category = AnimeListType.COMPLETED.route,
                                    status = detailScreenState?.status ?: "",
                                    rating = detailScreenState?.rating ?: "",
                                    secondName = detailScreenState?.title_japanese ?: "",
                                    airedFrom = detailScreenState?.aired?.from ?: "N/A",
                                    type = detailScreenState?.type ?: "N/A"
                                )
                            )
                        } else {
                            daoViewModel.addToCategory(
                                AnimeItem(
                                    id = detailScreenState?.mal_id,
                                    animeName = detailScreenState?.title ?: "",
                                    score = formatScore(detailScreenState?.score),
                                    scored_by = formatScoredBy(
                                        detailScreenState?.scored_by ?: 0.0f
                                    ),
                                    animeImage = detailScreenState?.images?.jpg?.large_image_url
                                        ?: "",
                                    category = AnimeListType.COMPLETED.route,
                                    status = detailScreenState?.status ?: "",
                                    rating = detailScreenState?.rating ?: "",
                                    secondName = detailScreenState?.title_japanese ?: "",
                                    airedFrom = detailScreenState?.aired?.from ?: "N/A",
                                    type = detailScreenState?.type ?: "N/A"
                                )

                            )
                        }
                    }

                }, modifier = modifier
                    .height(35.dp)
                    .width(170.dp)
                )
                DropdownMenuItem(text = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth(1f)
                    ) {
                        Text(
                            text = "Dropped", color = if (daoViewModel.containsItemIdInCategory(
                                    id = detailScreenState?.mal_id ?: 0,
                                    AnimeListType.DROPPED.route

                                ).collectAsStateWithLifecycle(initialValue = false).value
                            ) LightGreen else Color.White,
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.dropped, imageLoader = svgImageLoader
                            ), contentDescription = null, modifier = modifier.size(22.dp),
                            colorFilter =
                            if (daoViewModel.containsItemIdInCategory(
                                    id = detailScreenState?.mal_id ?: 0,
                                    AnimeListType.DROPPED.route
                                ).collectAsStateWithLifecycle(initialValue = false).value
                            ) ColorFilter.tint(LightGreen) else null

                        )
                    }
                }, onClick = {
                    detailScreenViewModel.viewModelScope.launch {

                        if (daoViewModel.containsItemIdInCategory(
                                detailScreenState?.mal_id ?: 0,
                                AnimeListType.DROPPED.route
                            ).first()
                        ) {
                            daoViewModel.removeFromDataBase(
                                AnimeItem(
                                    id = detailScreenState?.mal_id,
                                    animeName = detailScreenState?.title ?: "",
                                    score = formatScore(detailScreenState?.score),
                                    scored_by = formatScoredBy(
                                        detailScreenState?.scored_by ?: 0.0f
                                    ),
                                    animeImage = detailScreenState?.images?.jpg?.large_image_url
                                        ?: "",
                                    category = AnimeListType.DROPPED.route,
                                    status = detailScreenState?.status ?: "",
                                    rating = detailScreenState?.rating ?: "",
                                    secondName = detailScreenState?.title_japanese ?: "",
                                    airedFrom = detailScreenState?.aired?.from ?: "N/A",
                                    type = detailScreenState?.type ?: "N/A"
                                )
                            )
                        } else {

                            daoViewModel.addToCategory(
                                AnimeItem(
                                    id = detailScreenState?.mal_id,
                                    animeName = detailScreenState?.title ?: "",
                                    score = formatScore(detailScreenState?.score),
                                    scored_by = formatScoredBy(
                                        detailScreenState?.scored_by ?: 0.0f
                                    ),
                                    animeImage = detailScreenState?.images?.jpg?.large_image_url
                                        ?: "",
                                    category = AnimeListType.DROPPED.route,
                                    status = detailScreenState?.status ?: "",
                                    rating = detailScreenState?.rating ?: "",
                                    secondName = detailScreenState?.title_japanese ?: "",
                                    airedFrom = detailScreenState?.aired?.from ?: "N/A",
                                    type = detailScreenState?.type ?: "N/A"
                                )

                            )
                        }
                    }
                }, modifier = modifier
                    .height(35.dp)
                    .width(170.dp)
                )
                DropdownMenuItem(text = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth(1f)
                    ) {
                        Text(
                            text = "Add to list",

                            color = if (daoViewModel.containsItemIdInCategory(
                                    id = detailScreenState?.mal_id ?: 0,
                                    AnimeListType.WATCHING.route

                                ).collectAsStateWithLifecycle(initialValue = false).value
                            ) LightGreen else Color.White,
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.add, imageLoader = svgImageLoader
                            ), contentDescription = null, modifier = modifier.size(22.dp),
                            colorFilter =
                            if (daoViewModel.containsItemIdInCategory(
                                    id = detailScreenState?.mal_id ?: 0,
                                    AnimeListType.WATCHING.route
                                ).collectAsStateWithLifecycle(initialValue = false).value
                            ) ColorFilter.tint(LightGreen) else null

                        )
                    }

                }, onClick = {
                    detailScreenViewModel.viewModelScope.launch {
                        if (daoViewModel.containsItemIdInCategory(
                                detailScreenState?.mal_id ?: 0,
                                AnimeListType.WATCHING.route
                            ).first()
                        ) {
                            daoViewModel.removeFromDataBase(
                                AnimeItem(
                                    id = detailScreenState?.mal_id,
                                    animeName = detailScreenState?.title ?: "",
                                    score = formatScore(detailScreenState?.score),
                                    scored_by = formatScoredBy(
                                        detailScreenState?.scored_by ?: 0.0f
                                    ),
                                    animeImage = detailScreenState?.images?.jpg?.large_image_url
                                        ?: "",
                                    category = AnimeListType.WATCHING.route,
                                    status = detailScreenState?.status ?: "",
                                    rating = detailScreenState?.rating ?: "",
                                    secondName = detailScreenState?.title_japanese ?: "",
                                    airedFrom = detailScreenState?.aired?.from ?: "N/A",
                                    type = detailScreenState?.type ?: "N/A"
                                )
                            )
                        } else {
                            daoViewModel.addToCategory(
                                AnimeItem(
                                    id = detailScreenState?.mal_id,
                                    animeName = detailScreenState?.title ?: "",
                                    score = formatScore(detailScreenState?.score),
                                    scored_by = formatScoredBy(
                                        detailScreenState?.scored_by ?: 0.0f
                                    ),
                                    animeImage = detailScreenState?.images?.jpg?.large_image_url
                                        ?: "",
                                    category = AnimeListType.WATCHING.route,
                                    status = detailScreenState?.status ?: "",
                                    rating = detailScreenState?.rating ?: "",
                                    secondName = detailScreenState?.title_japanese ?: "",
                                    airedFrom = detailScreenState?.aired?.from ?: "N/A",
                                    type = detailScreenState?.type ?: "N/A"
                                )

                            )
                        }
                    }
                }, modifier = modifier
                    .height(35.dp)
                    .width(170.dp)
                )
            }
        }
    }
}

private fun formatScoredBy(float: Float): String {
    return if (float == 0f) {
        "N/A"
    } else {
        val formattedString = String.format(Locale.US, "%.1f", float)
        if (formattedString.endsWith(".0")) {
            formattedString.substring(0, formattedString.length - 2)
        } else {
            formattedString.replace(",", ".")
        }
    }
}


private fun formatScore(float: Float?): String {
    return if (float == null || float == 0f) {
        "N/A"
    } else {
        float.toString()
    }
}
