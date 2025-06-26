package com.project.toko.dataBase.search.domain.usecase

import com.project.toko.dataBase.search.data.remote.AnimeRepository
import com.project.toko.dataBase.search.data.remote.FilterParams
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class DeleteDataByFilterParam @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(
        filterParams: MutableStateFlow<FilterParams>,
    ) {
        repository.deleteDataByFilterParam(
            filterParams = filterParams,
        )
    }

}