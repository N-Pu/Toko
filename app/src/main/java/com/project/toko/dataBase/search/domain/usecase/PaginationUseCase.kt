package com.project.toko.dataBase.search.domain.usecase

import com.project.toko.dataBase.search.data.remote.AnimeRepository
import com.project.toko.dataBase.search.data.remote.FilterParams
import com.project.toko.dataBase.search.ui.viewmodel.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

//class PaginationUseCase @Inject constructor(
//        private val repository: AnimeRepository
//    ) {
//        suspend operator fun invoke(
//            filterParams: FilterParams,
//        ) {
//             repository.loadNextPage(
//                filterParams = filterParams
//            )
//        }
//
//
//}

class PaginationUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(
        filterParams: FilterParams,
        paginationState: MutableStateFlow<PaginationState>
    ) {
        repository.loadNextPage(
            filterParams = filterParams,
            paginationState = paginationState
        )
    }


}