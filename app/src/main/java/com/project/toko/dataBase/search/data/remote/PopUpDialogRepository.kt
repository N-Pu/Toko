package com.project.toko.dataBase.search.data.remote

import com.project.toko.dataBase.search.ui.viewmodel.AnimeDialogState
import kotlinx.coroutines.flow.MutableStateFlow

interface PopUpDialogRepository {
    suspend fun showDialogWithAnimeEntity(malId: Int, dialogState: MutableStateFlow<AnimeDialogState>)
    fun dismissDialog(dialogState: MutableStateFlow<AnimeDialogState>)
}