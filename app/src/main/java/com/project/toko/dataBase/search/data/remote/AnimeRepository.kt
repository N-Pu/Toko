package com.project.toko.dataBase.search.data.remote

import com.project.toko.dataBase.search.data.db.entity.AnimeEntity
import com.project.toko.dataBase.search.ui.viewmodel.InitialState
import com.project.toko.dataBase.search.ui.viewmodel.PaginationState
import com.project.toko.dataBase.search.ui.viewmodel.ScrollBehavior
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface AnimeRepository {
//    suspend fun getAnimeList(
//        queryFilter: FilterParams,
//        scrollBehaviorStateFlow: MutableStateFlow<ScrollBehavior>
//    ): Flow<List<AnimeEntity>>


    suspend fun getAnimeList(
        queryFilter: FilterParams,
        scrollBehaviorStateFlow: MutableStateFlow<ScrollBehavior>
    ):     Flow<InitialState>

//    suspend fun getAnimeList(
//        queryFilter: FilterParams,
//        scrollBehaviorStateFlow: MutableStateFlow<ScrollBehavior>,
//        flowCollector: FlowCollector<AnimeListState>
//    ): AnimeListState

    suspend fun canPaginate(queryFilter: FilterParams): Boolean
//    suspend fun loadNextPage(filterParams: FilterParams)
    suspend fun loadNextPage(filterParams: FilterParams,
                                  paginationState: MutableStateFlow<PaginationState>)
    suspend fun deleteDataByFilterParam(filterParams: MutableStateFlow<FilterParams>)
}
