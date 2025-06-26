package com.project.toko.dataBase.search.domain.usecase

import com.project.toko.dataBase.search.data.remote.AnimeRepository
import com.project.toko.dataBase.search.data.db.entity.AnimeEntity
import com.project.toko.dataBase.search.data.remote.FilterParams
import com.project.toko.dataBase.search.ui.viewmodel.InitialState
import com.project.toko.dataBase.search.ui.viewmodel.ScrollBehavior
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GetAnimeListUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
//    suspend operator fun invoke(
//        queryFilter: FilterParams, scrollBehaviorStateFlow: MutableStateFlow<ScrollBehavior>
//    ): Flow<List<AnimeEntity>> {
//        return repository.getAnimeList(
//            queryFilter = queryFilter, scrollBehaviorStateFlow = scrollBehaviorStateFlow
//        )
//    }

    suspend operator fun invoke(
        queryFilter: FilterParams, scrollBehaviorStateFlow: MutableStateFlow<ScrollBehavior>
    ): Flow<InitialState> {
        return repository.getAnimeList(
            queryFilter = queryFilter, scrollBehaviorStateFlow = scrollBehaviorStateFlow
        )
    }

}



