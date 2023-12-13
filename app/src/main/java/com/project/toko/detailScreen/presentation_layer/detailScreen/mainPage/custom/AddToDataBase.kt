package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import com.project.toko.R
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.core.share.shareLink
import com.project.toko.daoScreen.dao.FavoriteItem
import com.project.toko.daoScreen.daoViewModel.DaoViewModel
import com.project.toko.detailScreen.viewModel.DetailScreenViewModel
import com.project.toko.daoScreen.model.AnimeStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun AddToFavorites(
    viewModelProvider: ViewModelProvider, modifier: Modifier, isDarkTheme: Boolean, svgImageLoader : ImageLoader
) {
    val detailScreenViewModel = viewModelProvider[DetailScreenViewModel::class.java]
    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    val detailScreenState by detailScreenViewModel.animeDetails.collectAsStateWithLifecycle()
    var isExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val threeDots = if (isDarkTheme) {
        R.drawable.three_dots_white
    } else {
        R.drawable.three_dots_gray
    }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(modifier = modifier.weight(1f),onClick = {
            detailScreenViewModel.viewModelScope.launch {
                if (daoViewModel.containsInFavorite(
                        detailScreenState?.mal_id ?: 0
                    ).first()
                ) {
                    daoViewModel.removeFromFavorite(
                        FavoriteItem(
                            id = detailScreenState?.mal_id,
                            animeName = detailScreenState?.title ?: "",
                            score = formatScore(detailScreenState?.score),
                            scored_by = formatScoredBy(detailScreenState?.scored_by ?: 0.0f),
                            animeImage = detailScreenState?.images?.jpg?.large_image_url ?: "",
                            category = AnimeStatus.FAVORITE.route,
                            status = detailScreenState?.status ?: "",
                            rating = detailScreenState?.rating ?: "",
                            secondName = detailScreenState?.title_japanese ?: "",
                            airedFrom = detailScreenState?.aired?.from ?: "N/A",
                            type = detailScreenState?.type ?: "N/A"
                        )
                    )
                } else {
                    daoViewModel.addToFavorite(
                        FavoriteItem(
                            id = detailScreenState?.mal_id,
                            animeName = detailScreenState?.title ?: "",
                            score = formatScore(detailScreenState?.score),
                            scored_by = formatScoredBy(detailScreenState?.scored_by ?: 0.0f),
                            animeImage = detailScreenState?.images?.jpg?.large_image_url ?: "",
                            category = AnimeStatus.FAVORITE.route,
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
                    model = if (daoViewModel.containsInFavorite(
                            id = detailScreenState?.mal_id ?: 0
                        ).collectAsStateWithLifecycle(initialValue = false).value
                    ) R.drawable.favorite_touched else
                        R.drawable.favorite_untouched
                    , imageLoader = svgImageLoader
                ),
                contentDescription = null,
                colorFilter = if (daoViewModel.containsInFavorite(
                        id = detailScreenState?.mal_id ?: 0
                    ).collectAsStateWithLifecycle(initialValue = false).value
                ) null else ColorFilter.tint(
                    MaterialTheme.colorScheme.onError
                )
            )
        }
        IconButton(modifier = modifier.weight(1f),onClick = {
            detailScreenViewModel.viewModelScope.launch {
                if (daoViewModel.containsItemIdInCategory(
                        detailScreenState?.mal_id ?: 0,
                        AnimeStatus.PLANNED.route
                    ).first()
                ) {
                    daoViewModel.removeFromDataBase(
                        AnimeItem(
                            id = detailScreenState?.mal_id,
                            animeName = detailScreenState?.title ?: "",
                            score = formatScore(detailScreenState?.score),
                            scored_by = formatScoredBy(detailScreenState?.scored_by ?: 0.0f),
                            animeImage = detailScreenState?.images?.jpg?.large_image_url ?: "",
                            category = AnimeStatus.PLANNED.route,
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
                            category = AnimeStatus.PLANNED.route,
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
                        AnimeStatus.PLANNED.route
                    ).collectAsStateWithLifecycle(initialValue = false).value
                ) ColorFilter.tint(
                    Color(
                        255,
                        152,
                        0,
                        255
                    )
                ) else ColorFilter.tint(MaterialTheme.colorScheme.onError)
            )
        }
        if (detailScreenState?.url?.isNotEmpty() == true) {
            IconButton(modifier = modifier.weight(1f),onClick = {
            }) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.links, imageLoader = svgImageLoader
                    ), contentDescription = null, modifier = modifier.clickable {
                        context.shareLink(detailScreenState!!.url)
                    },
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onError)
                )
            }
        }
        IconButton(
            onClick = {
                detailScreenViewModel.viewModelScope.launch(Dispatchers.IO) {
                    isExpanded =
                        !isExpanded
                }

            },
            modifier = modifier.weight(1f)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = threeDots, imageLoader = svgImageLoader
                ), contentDescription = null
            )
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }, modifier = modifier
                    .fillMaxHeight(0.15f)
                    .fillMaxWidth(0.48f)
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {
                DropdownMenuItem(trailingIcon = {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.eyewhite, imageLoader = svgImageLoader
                        ), contentDescription = null, modifier = modifier.size(30.dp),
                        colorFilter =
                        if (daoViewModel.containsItemIdInCategory(
                                id = detailScreenState?.mal_id ?: 0,
                                AnimeStatus.COMPLETED.route
                            ).collectAsStateWithLifecycle(initialValue = false).value
                        ) ColorFilter.tint(MaterialTheme.colorScheme.secondary) else ColorFilter.tint(
                            MaterialTheme.colorScheme.error
                        )
                    )
                }, text = {
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
                                    AnimeStatus.COMPLETED.route

                                ).collectAsStateWithLifecycle(initialValue = false).value
                            ) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }, onClick = {
                    detailScreenViewModel.viewModelScope.launch {

                        if (daoViewModel.containsItemIdInCategory(
                                detailScreenState?.mal_id ?: 0,
                                AnimeStatus.COMPLETED.route
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
                                    category = AnimeStatus.COMPLETED.route,
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
                                    category = AnimeStatus.COMPLETED.route,
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
                )
                DropdownMenuItem(trailingIcon = {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.dropped, imageLoader = svgImageLoader
                        ), contentDescription = null, modifier = modifier.size(30.dp),
                        colorFilter =
                        if (daoViewModel.containsItemIdInCategory(
                                id = detailScreenState?.mal_id ?: 0,
                                AnimeStatus.DROPPED.route
                            ).collectAsStateWithLifecycle(initialValue = false).value
                        ) ColorFilter.tint(Color.Red) else ColorFilter.tint(MaterialTheme.colorScheme.error)

                    )
                }, text = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth(1f)
                    ) {
                        Text(
                            text = "Dropped", color = if (daoViewModel.containsItemIdInCategory(
                                    id = detailScreenState?.mal_id ?: 0,
                                    AnimeStatus.DROPPED.route

                                ).collectAsStateWithLifecycle(initialValue = false).value
                            ) Color.Red else MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )

                    }
                }, onClick = {
                    detailScreenViewModel.viewModelScope.launch {

                        if (daoViewModel.containsItemIdInCategory(
                                detailScreenState?.mal_id ?: 0,
                                AnimeStatus.DROPPED.route
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
                                    category = AnimeStatus.DROPPED.route,
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
                                    category = AnimeStatus.DROPPED.route,
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
                )
                DropdownMenuItem(trailingIcon = {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.add, imageLoader = svgImageLoader
                        ), contentDescription = null, modifier = modifier.size(28.dp),
                        colorFilter =
                        if (daoViewModel.containsItemIdInCategory(
                                id = detailScreenState?.mal_id ?: 0,
                                AnimeStatus.WATCHING.route
                            ).collectAsStateWithLifecycle(initialValue = false).value
                        ) ColorFilter.tint(MaterialTheme.colorScheme.secondary) else ColorFilter.tint(
                            MaterialTheme.colorScheme.error
                        )

                    )
                }, text = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .fillMaxWidth(1f)
                    ) {
                        Text(
                            text = "Watching",

                            color = if (daoViewModel.containsItemIdInCategory(
                                    id = detailScreenState?.mal_id ?: 0,
                                    AnimeStatus.WATCHING.route

                                ).collectAsStateWithLifecycle(initialValue = false).value
                            ) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )

                    }

                }, onClick = {
                    detailScreenViewModel.viewModelScope.launch {
                        if (daoViewModel.containsItemIdInCategory(
                                detailScreenState?.mal_id ?: 0,
                                AnimeStatus.WATCHING.route
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
                                    category = AnimeStatus.WATCHING.route,
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
                                    category = AnimeStatus.WATCHING.route,
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
