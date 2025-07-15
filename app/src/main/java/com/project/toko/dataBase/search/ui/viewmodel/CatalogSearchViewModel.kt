package com.project.toko.dataBase.search.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.core.data.settings.nsfw.SafeForWorkManager
import com.project.toko.dataBase.search.data.db.entity.AnimeEntity
import com.project.toko.dataBase.search.data.remote.FilterParams
import com.project.toko.dataBase.search.domain.usecase.CheckNextPageExistsUseCase
import com.project.toko.dataBase.search.domain.usecase.GetAnimeListUseCase
import com.project.toko.dataBase.search.domain.usecase.PaginationUseCase
import com.project.toko.dataBase.search.domain.usecase.DeleteDataByFilterParam
import com.project.toko.dataBase.search.domain.usecase.DismissDialogUseCase
import com.project.toko.dataBase.search.domain.usecase.ShowDialogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


//sealed class AnimeDialogState {
//    data object Hidden : AnimeDialogState()
//    data object Loading : AnimeDialogState()
//    data class Shown(val anime: AnimeEntity) : AnimeDialogState()
//    data class Error(val exception: Throwable) : AnimeDialogState()
//}
//
//enum class ScrollBehavior { KEEP_POSITION, SCROLL_TO_TOP }
//
//@HiltViewModel
//class AnimeViewModel @Inject constructor(
//    private val getAnimeListUseCase: GetAnimeListUseCase,
//    private val hasNextPageUseCase: CheckNextPageExistsUseCase,
//    private val paginationUseCase: PaginationUseCase,
//    private val showDialogUseCase: ShowDialogUseCase,
//    private val deleteDataByFilterParam: DeleteDataByFilterParam,
//    private val safeForWorkManager: SafeForWorkManager,
//    private val dismissDialogUseCase: DismissDialogUseCase
//) : ViewModel() {
//
//    // States
//    private val _filterParams =
//        MutableStateFlow(FilterParams(sfw = safeForWorkManager.isNSFWActive.value))
//    private val _scrollBehavior = MutableStateFlow(ScrollBehavior.KEEP_POSITION)
//    private val _dialogState = MutableStateFlow<AnimeDialogState>(AnimeDialogState.Hidden)
//    private val _selectedGenres = MutableStateFlow<Set<Int>>(emptySet())
//    private val _refreshEvents = MutableSharedFlow<Unit>()
//    private val _switchIndicator = mutableStateOf(false)
//    private val _showLoader = MutableStateFlow(false)
//
//    // Public exposed states
//    val scrollBehavior = _scrollBehavior.asStateFlow()
//    val dialogState = _dialogState.asStateFlow()
//    val sfwState = safeForWorkManager.isNSFWActive
//    val switchIndicator = _switchIndicator
//    val showLoader = _showLoader.asStateFlow()
//
//    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
//    val animeEntityList = merge(
//        _filterParams.debounce(1500).distinctUntilChanged().map { it to false },
//        _refreshEvents.map { _filterParams.value to true },
//    ).flatMapLatest { (params, forceRefresh) ->
//        if (!forceRefresh && shouldSkipSearch(params)) {
//            flowOf(emptyList())
//        } else {
//            if (!params.query.isNullOrBlank()) {
//                _switchIndicator.value = true
//            }
////            showLoader.tryEmit(true)
//            getAnimeListUseCase(params, _scrollBehavior)
////                .also {
////                showLoader.tryEmit(false)
////            }
//        }
//
//    }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000L),
//            initialValue = emptyList()
//        )
//
//
//
//
//
//    // Actions
//    suspend fun refresh() {
//        deleteDataByFilterParam(_filterParams)
//        _refreshEvents.emit(Unit)
//    }
//
//    fun updateFilter(update: FilterParams.() -> FilterParams) {
//        _filterParams.update { it.update() }
//    }
//
//    fun onGenreChange(genreId: Int) {
//        _selectedGenres.update { current ->
//            if (current.contains(genreId)) current - genreId else current + genreId
//        }
//        updateFilter {
//            copy(genres = _selectedGenres.value.takeIf { it.isNotEmpty() }?.joinToString(","))
//        }
//    }
//
//    // Pagination
//    suspend fun hasNextPage(): Boolean = hasNextPageUseCase(_filterParams.value)
//
//    suspend fun paginate() {
//        _showLoader.emit(true)
//        paginationUseCase(_filterParams.value)
//        _showLoader.emit(false)
//    }
//
//
//    suspend fun showDialogForAnime(malId: Int) {
//        showDialogUseCase(malId, _dialogState)
//    }
//
//    fun dismissDialog() {
//        dismissDialogUseCase(_dialogState)
//    }
//
//
//    fun toggleSFW() {
//        safeForWorkManager.toggleSFW()
//        updateFilter { copy(sfw = safeForWorkManager.isNSFWActive.value) }
//    }
//
//    private fun shouldSkipSearch(params: FilterParams): Boolean {
//        with(params) {
//            return sfw == null &&
//                    query.isNullOrBlank() &&
//                    genres.isNullOrBlank() &&
//                    type.isNullOrBlank() &&
//                    rating.isNullOrBlank() &&
//                    min_score.isNullOrBlank() &&
//                    max_score.isNullOrBlank() &&
//                    orderBy.isNullOrBlank()
//        }
//    }
//
//
//    fun resetScrollBehavior() {
//        _scrollBehavior.update { ScrollBehavior.KEEP_POSITION }
//    }
//
////    override fun onCleared() {
////        clearData()
////    }
////
////    private fun clearData(){
////
////    }
//}
//
//
//sealed class PaginationState{
//    data object Loading: PaginationState()
//    data object Empty: PaginationState()
//    data class Error(val error: Throwable): PaginationState()
//}
//
//sealed class AnimeListState {
//    data object Initial : AnimeListState() // Начальное состояние (еще не загружали)
//    data object Loading : AnimeListState() // Полноэкранная загрузка (первый запрос)
//    data object Empty : AnimeListState()
//    data class Success(
//        val data: List<AnimeEntity>,
//        val isLoadingMore: Boolean = false, // Для индикатора пагинации
////        val error: Error? = null
//    ) : AnimeListState()
//
//    data class Error(
//        val error: Throwable,
////        val hasData: Boolean = false // Есть ли уже данные (для partial error)
//    ) : AnimeListState()
//}

sealed class AnimeDialogState {
    data object Hidden : AnimeDialogState()
    data object Loading : AnimeDialogState()
    data class Shown(val anime: AnimeEntity) : AnimeDialogState()
    data class Error(val exception: Throwable) : AnimeDialogState()
}

enum class ScrollBehavior { KEEP_POSITION, SCROLL_TO_TOP }


private const val TAG = "AnimeViewModel"

@HiltViewModel
class CatalogSearchViewModel @Inject constructor(
    private val getAnimeListUseCase: GetAnimeListUseCase,
    private val hasNextPageUseCase: CheckNextPageExistsUseCase,
    private val paginationUseCase: PaginationUseCase,
    private val showDialogUseCase: ShowDialogUseCase,
    private val deleteDataByFilterParam: DeleteDataByFilterParam,
    private val safeForWorkManager: SafeForWorkManager,
    private val dismissDialogUseCase: DismissDialogUseCase
) : ViewModel() {

    // States
    private val _filterParams =
        MutableStateFlow(FilterParams(sfw = safeForWorkManager.isNSFWActive.value))
    private val _scrollBehavior = MutableStateFlow(ScrollBehavior.KEEP_POSITION)
    private val _dialogState = MutableStateFlow<AnimeDialogState>(AnimeDialogState.Hidden)
    private val _selectedGenres = MutableStateFlow<Set<Int>>(emptySet())
    private val _refreshEvents = MutableSharedFlow<Unit>()
    private val _switchIndicator = mutableStateOf(false)
    private val _showLoader = MutableStateFlow<PaginationState>(PaginationState.Empty)

    // Public exposed states
    val scrollBehavior = _scrollBehavior.asStateFlow()
    val dialogState = _dialogState.asStateFlow()
    val sfwState = safeForWorkManager.isNSFWActive
    val switchIndicator = _switchIndicator
    val showLoader = _showLoader.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val animeEntityList = merge(
        _filterParams.debounce(1500).distinctUntilChanged().map { it to false },
        _refreshEvents.map { _filterParams.value to true },
    ).flatMapLatest { (params, forceRefresh) ->
        if (!forceRefresh && shouldSkipSearch(params)) {
            flowOf(InitialState.Initial)
        } else {
            if (!params.query.isNullOrBlank()) {
                _switchIndicator.value = true
            }
            getAnimeListUseCase(params, _scrollBehavior)
        }

    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = InitialState.Initial
        )


    // Actions
    suspend fun refresh() {
            deleteDataByFilterParam(_filterParams)
            _refreshEvents.emit(Unit)
        }

    fun updateFilter(update: FilterParams.() -> FilterParams) {
        _filterParams.update { it.update() }
    }

    fun onGenreChange(genreId: Int) {
        _selectedGenres.update { current ->
            if (current.contains(genreId)) current - genreId else current + genreId
        }
        updateFilter {
            copy(genres = _selectedGenres.value.takeIf { it.isNotEmpty() }?.joinToString(","))
        }
    }

    // Pagination
    suspend fun hasNextPage(): Boolean = hasNextPageUseCase(_filterParams.value)

    suspend fun paginate() = paginationUseCase(_filterParams.value, _showLoader)


    suspend fun showDialogForAnime(malId: Int) {
        showDialogUseCase(malId, _dialogState)
    }

    fun dismissDialog() = dismissDialogUseCase(_dialogState)


    fun toggleSFW() {
        safeForWorkManager.toggleSFW()
        updateFilter { copy(sfw = safeForWorkManager.isNSFWActive.value) }
    }

    private fun shouldSkipSearch(params: FilterParams): Boolean {
        with(params) {
            return sfw == null &&
                    query.isNullOrBlank() &&
                    genres.isNullOrBlank() &&
                    type.isNullOrBlank() &&
                    rating.isNullOrBlank() &&
                    min_score.isNullOrBlank() &&
                    max_score.isNullOrBlank() &&
                    orderBy.isNullOrBlank()
        }
    }


    fun resetScrollBehavior() {
        _scrollBehavior.update { ScrollBehavior.KEEP_POSITION }
    }

}


sealed class PaginationState {
    data object Loading : PaginationState()
    data object Empty : PaginationState()
    data class Error(val error: Throwable) : PaginationState()
}

sealed class InitialState {
    data object Initial : InitialState() // Начальное состояние (еще не загружали)
    data object Loading : InitialState() // Полноэкранная загрузка (первый запрос)
    data object Empty : InitialState()
    data class Success(
        val data: List<AnimeEntity>,
    ) : InitialState()

    data class Error(
        val error: Throwable,
    ) : InitialState()
}

