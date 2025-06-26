package com.project.toko.dataBase.search.domain.usecase

import com.project.toko.dataBase.search.data.remote.AnimeRepository
import com.project.toko.dataBase.search.data.remote.FilterParams
import javax.inject.Inject

class CheckNextPageExistsUseCase @Inject constructor(private val repository: AnimeRepository){
    suspend operator fun invoke(
        queryFilter: FilterParams
    ): Boolean {
        return repository.canPaginate(
            queryFilter = queryFilter
        )
    }
}