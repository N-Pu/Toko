package com.project.toko.core.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.project.toko.R
import com.project.toko.core.domain.util.share.openSite
import com.project.toko.core.ui.appConstraction.AnimeListTypesToDelete
import com.project.toko.core.ui.theme.evolventaBoldFamily
import com.project.toko.daoScreen.ui.daoViewModel.DaoViewModel
import com.project.toko.homeScreen.ui.viewModel.HomeScreenViewModel
import com.project.toko.randomAnimeScreen.presentation_layer.viewModel.RandomAnimeViewModel

@Composable
fun ShowDrawerContent(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    onThemeChange: () -> Unit,
    darkTheme: () -> Boolean
) {
    val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
    val daoViewModel: DaoViewModel = hiltViewModel()
    val randomScreenViewModel: RandomAnimeViewModel = hiltViewModel()
    val context = LocalContext.current
    var isHelpFAQOpen by remember { mutableStateOf(false) }
    var isLegalOpen by remember { mutableStateOf(false) }
    var isDeleteDaoOpen by remember { mutableStateOf(false) }
    var isExportDataPopUpDialogOpen by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .padding(top = 80.dp)
            .clip(RoundedCornerShape(topEnd = 20.dp))
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(topEnd = 20.dp)
            )
            .background(MaterialTheme.colorScheme.surfaceTint)
    ) {

        // Main drawer content
        Column(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .verticalScroll(rememberScrollState())
        ) {
            DividerSection()

            // NSFW toggle
            NsfwToggleSection(
                modifier = Modifier,
                homeScreenViewModel = homeScreenViewModel,
                randomScreenViewModel = randomScreenViewModel
            )

            DividerSection()

            // Help/FAQ section
            ExpandableSection(
                modifier = Modifier,
                title = "Help/FAQ",
                isExpanded = isHelpFAQOpen,
                imageLoader = imageLoader,
                onClick = { isHelpFAQOpen = !isHelpFAQOpen }
            )
            DividerSection()

            if (isHelpFAQOpen) {
                HelpFaqContent(Modifier, imageLoader)
            }

            // Contact Support
            NavigationItemWithIcon(
                modifier = Modifier,
                title = "Contact Support",
                imageLoader = imageLoader,
                iconRes = R.drawable.openbrowser,
                onClick = { context.openSite("https://discord.gg/arJvEJ6RJb") }
            )

            DividerSection()

            // Legal section
            ExpandableSection(
                modifier = Modifier,
                title = "Legal",
                isExpanded = isLegalOpen,
                imageLoader = imageLoader,
                onClick = { isLegalOpen = !isLegalOpen }
            )
            DividerSection()
            if (isLegalOpen) {
                LegalContent(Modifier, imageLoader)
            }

            // Data section
            ExpandableSection(
                modifier = Modifier,
                title = "Data",
                isExpanded = isDeleteDaoOpen,
                imageLoader = imageLoader,
                onClick = { isDeleteDaoOpen = !isDeleteDaoOpen }
            )

            DividerSection()

            if (isDeleteDaoOpen) {
                AnimeListTypesToDelete(
                    daoViewModel = daoViewModel,
                    modifier = Modifier
                )
            }

            // Export Data
            NavigationItemWithIcon(
                modifier = Modifier,
                title = "Export Data",
                imageLoader = imageLoader,
                iconRes = R.drawable.export,
                onClick = { /* Handle export */ }
            )

            DividerSection()


        }
        // Theme toggle at bottom
        ThemeToggleSection(
            modifier = Modifier,
            imageLoader = imageLoader,
            darkTheme = darkTheme,
            onThemeChange = onThemeChange
        )


    }

    // Export Data Dialog
    if (isExportDataPopUpDialogOpen) {
        ExportDataDialog(
            modifier = Modifier,
            onDismiss = { isExportDataPopUpDialogOpen = false },
            onExport = { daoViewModel.exportDB("Main.db", "com.project.toko") }
        )
    }
}

@Composable
private fun HelpFaqContent(
    modifier: Modifier,
    imageLoader: ImageLoader,
) {
    val context = LocalContext.current
    NavigationItemWithIcon(
        modifier = modifier,
        title = "Future of the app",
        imageLoader = imageLoader,
        iconRes = R.drawable.openbrowser,
        onClick = { context.openSite("https://sites.google.com/view/toko-yourownanimelibrary/future?authuser=0") }
    )

    DividerSection()

    NavigationItemWithIcon(
        modifier = modifier,
        title = "Bugs",
        imageLoader = imageLoader,
        iconRes = R.drawable.openbrowser,
        onClick = { context.openSite("https://sites.google.com/view/toko-yourownanimelibrary/bugs?authuser=0") }
    )

    DividerSection()
}

@Composable
private fun LegalContent(
    modifier: Modifier,
    imageLoader: ImageLoader,

    ) {
    val context = LocalContext.current
    NavigationItemWithIcon(
        modifier = modifier,
        title = "Resource",
        imageLoader = imageLoader,
        iconRes = R.drawable.openbrowser,
        onClick = { context.openSite("https://sites.google.com/view/toko-yourownanimelibrary/resource?authuser=0") }
    )

    DividerSection()
}

@Composable
private fun ThemeToggleSection(
    modifier: Modifier,
    imageLoader: ImageLoader,
    darkTheme: () -> Boolean,
    onThemeChange: () -> Unit
) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 10.dp)
        ,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = if (darkTheme()) R.drawable.sun else R.drawable.moon,
                imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = modifier
                .size(50.dp)
                .padding(bottom = 10.dp, end = 5.dp)
                .clickable { onThemeChange() }
        )
    }

}

@Composable
private fun ExportDataDialog(
    modifier: Modifier,
    onDismiss: () -> Unit,
    onExport: () -> Unit

) {
    val context = LocalContext.current
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceTint)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = modifier.fillMaxSize()
                ) {
                    // Dialog title
                    Text(
                        text = "Export Data?",
                        fontSize = 35.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = evolventaBoldFamily
                    )

                    // Save Data button
                    ButtonSection(
                        modifier = modifier,
                        text = "Save Data",
                        backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClick = onExport
                    )

                    Spacer(modifier = modifier.height(10.dp))

                    // Upload Data button
                    ButtonSection(
                        modifier = modifier,
                        text = "Upload Data",
                        backgroundColor = MaterialTheme.colorScheme.surfaceTint,
                        borderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClick = {
                            Toast.makeText(
                                context,
                                "Will be added in next update!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )

                    Spacer(modifier = modifier.height(20.dp))
                }
            }
        }
    }
}


@Composable
private fun DividerSection() {
    HorizontalDivider(
        thickness = 3.dp,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun NsfwToggleSection(
    modifier: Modifier,
    homeScreenViewModel: HomeScreenViewModel,
    randomScreenViewModel: RandomAnimeViewModel
) {
    NavigationDrawerItem(
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.surfaceTint,
            unselectedContainerColor = MaterialTheme.colorScheme.surfaceTint
        ),
        label = {
            Text(
                text = "NSFW",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                modifier = modifier.padding(start = 20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = evolventaBoldFamily
            )
        },
        selected = false,
        onClick = {},
        badge = {
            Switch(
                checked = homeScreenViewModel.isNSFWActive.value,
                onCheckedChange = {
                    homeScreenViewModel.saveNSFWData(it)
                    homeScreenViewModel.isNSFWActive.value = it
                    randomScreenViewModel.isNSFWActive.value = it
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.inversePrimary,
                    checkedTrackColor = MaterialTheme.colorScheme.surfaceTint,
                    checkedBorderColor = MaterialTheme.colorScheme.inversePrimary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.inversePrimary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceTint,
                    uncheckedBorderColor = MaterialTheme.colorScheme.inversePrimary,
                ),
                thumbContent = if (homeScreenViewModel.isNSFWActive.value) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                } else {
                    null
                }
            )
        },
    )
}

@Composable
private fun ExpandableSection(
    modifier: Modifier,
    title: String,
    isExpanded: Boolean,
    imageLoader: ImageLoader,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        modifier = modifier.background(MaterialTheme.colorScheme.inverseSurface),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color.Transparent,
            unselectedContainerColor = Color.Transparent
        ),
        label = {
            Text(
                text = title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                modifier = modifier.padding(start = 20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = evolventaBoldFamily
            )
        },
        selected = false,
        onClick = onClick,
        badge = {
            Image(
                painter = rememberAsyncImagePainter(
                    model = if (isExpanded) R.drawable.arrowdown else R.drawable.arrowright,
                    imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = modifier.size(17.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
        },
    )
}

@Composable
private fun NavigationItemWithIcon(
    modifier: Modifier,
    title: String,
    imageLoader: ImageLoader,
    iconRes: Int,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.surfaceTint,
            unselectedContainerColor = MaterialTheme.colorScheme.surfaceTint
        ),
        label = {
            Text(
                text = title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                modifier = modifier.padding(start = 20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = evolventaBoldFamily
            )
        },
        selected = false,
        onClick = onClick,
        badge = {
            Image(
                painter = rememberAsyncImagePainter(
                    model = iconRes,
                    imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = modifier.size(30.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
        },
    )
}


@Composable
private fun ButtonSection(
    modifier: Modifier,
    text: String,
    backgroundColor: Color,
    borderColor: Color? = null,
    onClick: () -> Unit
) {
    val buttonModifier = modifier
        .fillMaxWidth(0.8f)
        .height(70.dp)
        .clip(CardDefaults.shape)
        .then(
            if (borderColor != null) {
                modifier.border(4.dp, borderColor, CardDefaults.shape)
            } else {
                Modifier
            }
        )
        .background(backgroundColor)
        .clickable(onClick = onClick)

    Box(
        modifier = buttonModifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (borderColor != null) MaterialTheme.colorScheme.onPrimary else Color.White,
            fontFamily = evolventaBoldFamily
        )
    }
}
