package com.project.toko.dataBase.search.domain.usecase

import com.project.toko.dataBase.search.data.remote.PopUpDialogRepository
import com.project.toko.dataBase.search.ui.viewmodel.AnimeDialogState
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject



class ShowDialogUseCase @Inject constructor(
    private val repository: PopUpDialogRepository
) {
    suspend operator fun invoke(
        malId: Int,
        dialogState: MutableStateFlow<AnimeDialogState>
    ) {
        return repository.showDialogWithAnimeEntity(
            malId = malId,
            dialogState = dialogState
        )
    }
}