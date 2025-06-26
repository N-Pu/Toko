package com.project.toko.dataBase.search.data.repository

import android.util.Log
import com.project.toko.dataBase.search.data.db.AppDatabase
import com.project.toko.dataBase.search.data.remote.PopUpDialogRepository
import com.project.toko.dataBase.search.ui.viewmodel.AnimeDialogState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


private const val TAG = "PopUpDialogRepositoryImpl"

class PopUpDialogRepositoryImpl @Inject constructor(private val appDatabase: AppDatabase) : PopUpDialogRepository{

    private val animeDao by lazy { appDatabase.animeDao() }

    override suspend fun showDialogWithAnimeEntity(malId: Int, dialogState: MutableStateFlow<AnimeDialogState>) {
        dialogState.update { AnimeDialogState.Loading }
        try {
            animeDao.getAnimeByMalId(malId).let { anime ->
                dialogState.update { AnimeDialogState.Shown(anime) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading Dialog Screen", e)
            dialogState.update {  AnimeDialogState.Error(e) }
        }
    }

    override fun dismissDialog(dialogState: MutableStateFlow<AnimeDialogState>) {
        dialogState.update { AnimeDialogState.Hidden }
    }

}