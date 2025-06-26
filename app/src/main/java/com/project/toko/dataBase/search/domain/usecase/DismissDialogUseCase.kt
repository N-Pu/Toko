package com.project.toko.dataBase.search.domain.usecase

import com.project.toko.dataBase.search.data.remote.PopUpDialogRepository
import com.project.toko.dataBase.search.ui.viewmodel.AnimeDialogState
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class DismissDialogUseCase @Inject constructor(private val repository: PopUpDialogRepository) {
    operator fun invoke(
        dialogState: MutableStateFlow<AnimeDialogState>
    ) {
        return repository.dismissDialog(dialogState)
    }
}
