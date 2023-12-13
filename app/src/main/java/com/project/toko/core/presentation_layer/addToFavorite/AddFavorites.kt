package com.project.toko.core.presentation_layer.addToFavorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.project.toko.R
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.daoScreen.dao.FavoriteItem
import com.project.toko.daoScreen.daoViewModel.DaoViewModel
import com.project.toko.daoScreen.model.AnimeStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


// Function that starts when you click on
// button "+" to add anime in 4 different
// categories and shows dropDownMenu that
// contains those categories (watching,
// dropped, planned, watched). User can
// tap on them and anime that was selected
// will be send to data base and placed in

@Composable
fun AddFavorites(
    mal_id: Int,
    title: String,
    score: String,
    scoredBy: String,
    animeImage: String,
    modifier: Modifier,
    viewModelProvider: ViewModelProvider,
    rating: String,
    status: String,
    secondName: String?,
    airedFrom: String?,
    type: String,
    imageLoader: ImageLoader

) {

    val daoViewModel = viewModelProvider[DaoViewModel::class.java]
    var isExpanded by remember { mutableStateOf(false) }
    val svgImageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()
    Box {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {

            when {
                daoViewModel.containsItemIdInCategory(
                    id = mal_id,
                    AnimeStatus.WATCHING.route
                ).collectAsStateWithLifecycle(initialValue = false).value -> {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.add, imageLoader = svgImageLoader
                        ),
                        contentDescription = null,
                        modifier = modifier
                            .size(30.dp)
                            .clickable {
                                isExpanded = true
                            },
                        colorFilter = ColorFilter.tint(Color.Yellow)
                    )

                }

                daoViewModel.containsItemIdInCategory(
                    id = mal_id,
                    AnimeStatus.COMPLETED.route
                ).collectAsStateWithLifecycle(initialValue = false).value -> {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.eyewhite, imageLoader = svgImageLoader
                        ),
                        contentDescription = null,
                        modifier = modifier
                            .size(30.dp)
                            .clickable {
                                isExpanded = true
                            }
                            .fillMaxSize(),
                        colorFilter = ColorFilter.tint(LightGreen))

                }

                daoViewModel.containsItemIdInCategory(
                    id = mal_id,
                    AnimeStatus.DROPPED.route
                ).collectAsStateWithLifecycle(initialValue = false).value -> {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.dropped, imageLoader = svgImageLoader
                        ),
                        contentDescription = null,
                        modifier = modifier
                            .size(30.dp)
                            .clickable {
                                isExpanded = true
                            },
                        colorFilter = ColorFilter.tint(Color.Red)
                    )

                }

                daoViewModel.containsItemIdInCategory(
                    id = mal_id,
                    AnimeStatus.PLANNED.route
                ).collectAsStateWithLifecycle(initialValue = false).value -> {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.bookmarkfilled, imageLoader = svgImageLoader
                        ),
                        contentDescription = null,
                        modifier = modifier
                            .size(30.dp)
                            .clickable {
                                isExpanded = true
                            },
                        colorFilter = ColorFilter.tint(Color(255, 152, 0, 255))
                    )

                }

                daoViewModel.containsInFavorite(
                    id = mal_id
                ).collectAsStateWithLifecycle(initialValue = false).value -> {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.favorite_touched, imageLoader = svgImageLoader
                        ),
                        contentDescription = null,
                        modifier = modifier
                            .size(30.dp)
                            .clickable {
                                isExpanded = true
                            },

                    )

                }

                else -> {
                    Image(
                        modifier = modifier
                            .size(30.dp)
                            .clickable { isExpanded = true },
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.addpluscircle,
                            imageLoader = imageLoader
                        ),
                        contentDescription = "Add circle"
                    )

                }
            }

        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = !isExpanded
            },
            modifier = modifier
                .background(MaterialTheme.colorScheme.onPrimary)
                .fillMaxHeight(0.15f)
                .fillMaxWidth(0.48f),
            offset = DpOffset((-40).dp, (-40).dp),
            properties = PopupProperties(clippingEnabled = true)
        ) {
            DropdownMenuItem(text = {
                Text(
                    text = "Planned",
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = if (daoViewModel.containsItemIdInCategory(
                            id = mal_id,
                            AnimeStatus.PLANNED.route
                        ).collectAsStateWithLifecycle(initialValue = false).value
                    ) Color(255, 152, 0, 255) else MaterialTheme.colorScheme.error,
                )
            }, onClick = {
                daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                    if (daoViewModel.containsItemIdInCategory(
                            mal_id,
                            AnimeStatus.PLANNED.route
                        ).first()
                    ) {
                        daoViewModel.removeFromDataBase(

                            AnimeItem(
                                id = mal_id,
                                animeName = title,
                                score = score,
                                scored_by = scoredBy,
                                animeImage = animeImage,
                                status = status,
                                rating = rating,
                                secondName = secondName ?: "N/A",
                                airedFrom = airedFrom ?: "N/A",
                                category = AnimeStatus.PLANNED.route,
                                type = type
                            )
                        )
                    } else {
                        daoViewModel.addToCategory(
                            AnimeItem(
                                id = mal_id,
                                animeName = title,
                                score = score,
                                scored_by = scoredBy,
                                animeImage = animeImage,
                                status = status,
                                rating = rating,
                                secondName = secondName ?: "N/A",
                                airedFrom = airedFrom ?: "N/A",
                                category = AnimeStatus.PLANNED.route,
                                type = type
                            )
                        )
                    }
                }
            }, colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.error,
                trailingIconColor = MaterialTheme.colorScheme.error
            ), trailingIcon = {

                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.bookmarkfilled, imageLoader = svgImageLoader
                    ), contentDescription = null, modifier = modifier.size(30.dp),
                    colorFilter =
                    if (daoViewModel.containsItemIdInCategory(
                            id = mal_id,
                            AnimeStatus.PLANNED.route
                        ).collectAsStateWithLifecycle(initialValue = false).value
                    ) ColorFilter.tint(
                        Color(
                            255,
                            152,
                            0,
                            255
                        )
                    ) else ColorFilter.tint(MaterialTheme.colorScheme.error)
                )

            })
            DropdownMenuItem(text = {
                Text(
                    text = "Watching",
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = if (daoViewModel.containsItemIdInCategory(
                            id = mal_id,
                            AnimeStatus.WATCHING.route
                        ).collectAsStateWithLifecycle(initialValue = false).value
                    ) Color.Yellow else MaterialTheme.colorScheme.error,
                )
            }, onClick = {
                daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                    if (daoViewModel.containsItemIdInCategory(
                            mal_id,
                            AnimeStatus.WATCHING.route
                        ).first()
                    ) {
                        daoViewModel.removeFromDataBase(

                            AnimeItem(
                                id = mal_id,
                                animeName = title,
                                score = score,
                                scored_by = scoredBy,
                                animeImage = animeImage,
                                status = status,
                                rating = rating,
                                secondName = secondName ?: "N/A",
                                airedFrom = airedFrom ?: "N/A",
                                category = AnimeStatus.WATCHING.route,
                                type = type
                            )
                        )
                    } else {
                        daoViewModel.addToCategory(
                            AnimeItem(
                                id = mal_id,
                                animeName = title,
                                score = score,
                                scored_by = scoredBy,
                                animeImage = animeImage,
                                status = status,
                                rating = rating,
                                secondName = secondName ?: "N/A",
                                airedFrom = airedFrom ?: "N/A",
                                category = AnimeStatus.WATCHING.route,
                                type = type
                            )
                        )
                    }
                }
            }, colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.error,
                trailingIconColor = MaterialTheme.colorScheme.error
            ), trailingIcon = {

                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.add, imageLoader = svgImageLoader
                    ), contentDescription = null, modifier = modifier.size(25.dp),
                    colorFilter =
                    if (daoViewModel.containsItemIdInCategory(
                            id = mal_id,
                            AnimeStatus.WATCHING.route
                        ).collectAsStateWithLifecycle(initialValue = false).value
                    ) ColorFilter.tint(Color.Yellow) else ColorFilter.tint(MaterialTheme.colorScheme.error)
                )
            })
            DropdownMenuItem(text = {
                Text(
                    text = "Completed",
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = if (daoViewModel.containsItemIdInCategory(
                            id = mal_id,
                            AnimeStatus.COMPLETED.route
                        ).collectAsStateWithLifecycle(initialValue = false).value
                    ) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                )
            }, onClick = {
                daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                    if (daoViewModel.containsItemIdInCategory(
                            mal_id,
                            AnimeStatus.COMPLETED.route
                        ).first()
                    ) {
                        daoViewModel.removeFromDataBase(
                            AnimeItem(
                                id = mal_id,
                                animeName = title,
                                score = score,
                                scored_by = scoredBy,
                                animeImage = animeImage,
                                status = status,
                                rating = rating,
                                secondName = secondName ?: "N/A",
                                airedFrom = airedFrom ?: "N/A",
                                category = AnimeStatus.COMPLETED.route,
                                type = type
                            )
                        )
                    } else {
                        daoViewModel.addToCategory(
                            AnimeItem(
                                id = mal_id,
                                animeName = title,
                                score = score,
                                scored_by = scoredBy,
                                animeImage = animeImage,
                                status = status,
                                rating = rating,
                                secondName = secondName ?: "N/A",
                                airedFrom = airedFrom ?: "N/A",
                                category = AnimeStatus.COMPLETED.route,
                                type = type
                            )
                        )
                    }
                }
            }, colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.error,
                trailingIconColor = MaterialTheme.colorScheme.error
            ), trailingIcon = {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.eyewhite, imageLoader = svgImageLoader
                    ), contentDescription = null, modifier = modifier.size(22.dp),
                    colorFilter =
                    if (daoViewModel.containsItemIdInCategory(
                            id = mal_id,
                            AnimeStatus.COMPLETED.route
                        ).collectAsStateWithLifecycle(initialValue = false).value
                    ) ColorFilter.tint(MaterialTheme.colorScheme.secondary) else ColorFilter.tint(
                        MaterialTheme.colorScheme.error
                    )
                )
            })
            DropdownMenuItem(text = {
                Text(
                    text = "Dropped",
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = if (daoViewModel.containsItemIdInCategory(
                            id = mal_id,
                            AnimeStatus.DROPPED.route
                        ).collectAsStateWithLifecycle(initialValue = false).value
                    ) Color.Red else MaterialTheme.colorScheme.error,
                )
            }, onClick = {
                daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                    if (daoViewModel.containsItemIdInCategory(
                            mal_id,
                            AnimeStatus.DROPPED.route
                        ).first()
                    ) {
                        daoViewModel.removeFromDataBase(
                            AnimeItem(
                                id = mal_id,
                                animeName = title,
                                score = score,
                                scored_by = scoredBy,
                                animeImage = animeImage,
                                status = status,
                                rating = rating,
                                secondName = secondName ?: "N/A",
                                airedFrom = airedFrom ?: "N/A",
                                category = AnimeStatus.DROPPED.route,
                                type = type
                            )
                        )
                    } else {
                        daoViewModel.addToCategory(
                            AnimeItem(
                                id = mal_id,
                                animeName = title,
                                score = score,
                                scored_by = scoredBy,
                                animeImage = animeImage,
                                status = status,
                                rating = rating,
                                secondName = secondName ?: "N/A",
                                airedFrom = airedFrom ?: "N/A",
                                category = AnimeStatus.DROPPED.route,
                                type = type
                            )
                        )
                    }
                }
            }, colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.error,
                trailingIconColor = MaterialTheme.colorScheme.error
            ), trailingIcon = {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.dropped, imageLoader = svgImageLoader
                    ), contentDescription = null, modifier = modifier.size(25.dp),
                    colorFilter =
                    if (daoViewModel.containsItemIdInCategory(
                            id = mal_id,
                            AnimeStatus.DROPPED.route
                        ).collectAsStateWithLifecycle(initialValue = false).value
                    ) ColorFilter.tint(Color.Red) else ColorFilter.tint(MaterialTheme.colorScheme.error)
                )
            })

            DropdownMenuItem(text = {
                Text(
                    text = "Favorite",
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = if (daoViewModel.containsInFavorite(
                            id = mal_id
                        ).collectAsStateWithLifecycle(initialValue = false).value
                    ) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                )
            }, onClick = {
                daoViewModel.viewModelScope.launch(Dispatchers.IO) {
                    if (daoViewModel.containsInFavorite(mal_id).first()) {
                        daoViewModel.removeFromFavorite(
                            FavoriteItem(
                                id = mal_id,
                                animeName = title,
                                score = score,
                                scored_by = scoredBy,
                                animeImage = animeImage,
                                status = status,
                                rating = rating,
                                secondName = secondName ?: "N/A",
                                airedFrom = airedFrom ?: "N/A",
                                category = AnimeStatus.DROPPED.route,
                                type = type
                            )
                        )
                    } else {
                        daoViewModel.addToFavorite(
                            FavoriteItem(
                                id = mal_id,
                                animeName = title,
                                score = score,
                                scored_by = scoredBy,
                                animeImage = animeImage,
                                status = status,
                                rating = rating,
                                secondName = secondName ?: "N/A",
                                airedFrom = airedFrom ?: "N/A",
                                category = AnimeStatus.DROPPED.route,
                                type = type
                            )
                        )
                    }
                }
            }, colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.error,
                trailingIconColor = MaterialTheme.colorScheme.error
            ), trailingIcon = {
                Image(
                    painter = rememberAsyncImagePainter(
                        model =   if (daoViewModel.containsInFavorite(
                                id = mal_id
                            ).collectAsStateWithLifecycle(initialValue = false).value
                        ) R.drawable.favorite_touched else R.drawable.favorite_untouched, imageLoader = svgImageLoader
                    ), contentDescription = null, modifier = modifier.size(25.dp),
                    colorFilter =
                    if (daoViewModel.containsInFavorite(
                            id = mal_id
                        ).collectAsStateWithLifecycle(initialValue = false).value
                    ) null else ColorFilter.tint(
                        MaterialTheme.colorScheme.error
                    )
                )
            })
        }
    }


}
