package com.project.toko.personDetailedScreen.presentation_layer.staffMemberFull

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.project.toko.R
import  com.project.toko.personDetailedScreen.model.personFullModel.Data
import com.project.toko.core.presentation_layer.theme.LightGreen
import com.project.toko.core.viewModel.daoViewModel.DaoViewModel
import com.project.toko.personDetailedScreen.dao.PersonItem
import com.project.toko.personDetailedScreen.viewModel.PersonByIdViewModel
import kotlinx.coroutines.launch

@Composable
fun ShowNamesAndInteractionIcons(
    data: Data,
    modifier: Modifier,
    imageLoader: ImageLoader,
    daoViewModel: DaoViewModel,
    personViewModel: PersonByIdViewModel
) {

    val isPersonInDao by daoViewModel.isPersonInDao(data.mal_id).collectAsStateWithLifecycle(
        initialValue = false
    )
//    val containsInCategory by daoViewModel.containsItemIdInCategory(
//        data.mal_id,
//        AnimeListType.PERSON.route
//    ).collectAsStateWithLifecycle(initialValue = false)

    Column(
        modifier = modifier
            .fillMaxWidth(1f)
            .fillMaxHeight()

    ) {
        Text(
            text = data.name,
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            modifier = modifier.fillMaxWidth(),
            minLines = 1,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 10.sp
        )
        Spacer(modifier = modifier.size(2.dp))
        if (!data.given_name.isNullOrBlank()) {
            Text(
                text = "Given name:" + data.given_name,
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                modifier = modifier.fillMaxWidth(),
                minLines = 1,
                fontWeight = FontWeight.Medium,
                lineHeight = 14.sp
            )
        }
        Spacer(modifier = modifier.size(2.dp))
        if (!data.family_name.isNullOrBlank()) {
            Text(
                text = "Family name:" + data.family_name,
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                modifier = modifier.fillMaxWidth(),
                minLines = 1,
                fontWeight = FontWeight.Medium,
                lineHeight = 14.sp
            )
        }
        Spacer(modifier = modifier.size(2.dp))
        if (!data.birthday.isNullOrBlank()) {
            Text(
                text = "Birthday" + birthday(data.birthday),
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                modifier = modifier.fillMaxWidth(),
                minLines = 1,
                fontWeight = FontWeight.Medium,
                lineHeight = 14.sp
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 60.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.star,
                        imageLoader = imageLoader
                    ), contentDescription = null,
                    modifier = modifier
                        .size(40.dp)
                        .clickable {
                            personViewModel.viewModelScope.launch {
                                if (isPersonInDao
                                ) {
                                    daoViewModel.removePersonFromDataBase(data.mal_id)
                                } else {
                                    daoViewModel.addPerson(
                                        PersonItem(
                                            id = data.mal_id,
                                            name = data.name,
                                            givenName = data.given_name,
                                            familyName = data.family_name,
                                            image = data.images.jpg.image_url
                                        )

                                    )
                                }
                            }
                        },
                    colorFilter = if (isPersonInDao) ColorFilter.tint(LightGreen) else null
                )
                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.links,
                        imageLoader = imageLoader
                    ), contentDescription = null,
                    modifier = modifier.size(40.dp)
                )
            }
        }
    }

}


private fun birthday(birthday: String): String {
    return birthday.slice(0..9).replace("-", ".")
}