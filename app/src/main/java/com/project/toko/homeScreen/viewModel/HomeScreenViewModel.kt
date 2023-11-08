package com.project.toko.homeScreen.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.core.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(FlowPreview::class)
class HomeScreenViewModel @Inject constructor(
    private val malApiRepository: MalApiService,
) : ViewModel() {

    private var _isDropdownVisible = mutableStateOf(false)
    val isDropdownMenuVisible = _isDropdownVisible

    private val emptyItem = com.project.toko.homeScreen.model.newAnimeSearchModel.Items(0, 0, 0)
    private val emptyNewAnimeSearchModel =
        com.project.toko.homeScreen.model.newAnimeSearchModel.NewAnimeSearchModel(
            data = ArrayList(),
            pagination = com.project.toko.homeScreen.model.newAnimeSearchModel.Pagination(
                false,
                emptyItem,
                0
            )
        )

    private val _isPerformingSearch = MutableStateFlow(false)
    val isPerformingSearch = _isPerformingSearch.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage.asStateFlow()

    private val _isNextPageLoading = MutableStateFlow(false)
    val isNextPageLoading = _isNextPageLoading.asStateFlow()

    private val _searchText = MutableStateFlow<String?>(null)
    val searchText = _searchText.asStateFlow()

    private val _animeSearch = MutableStateFlow(emptyNewAnimeSearchModel)
    val animeSearch = _animeSearch.asStateFlow()

    private val searchDebouncer = MutableSharedFlow<String>()

    private var hasNextPage = MutableStateFlow(true)


    // _____________________________________________________________________ //

    //link parameters section

    private val arrayOfGenres = MutableStateFlow(arrayListOf<Int>())

    private val preSelectedGenre =
        MutableStateFlow(com.project.toko.homeScreen.model.linkChangerModel.getGenres())
    val selectedGenre: MutableStateFlow<List<com.project.toko.homeScreen.model.linkChangerModel.Genre>> =
        preSelectedGenre


    private var pre_genres = ""
    private val _genres = MutableStateFlow<String?>(null)

    private val _type = MutableStateFlow("")
    val type = _type.value

    private val _ratingList =
        MutableStateFlow(com.project.toko.homeScreen.model.linkChangerModel.getRating())
    val ratingList: StateFlow<List<com.project.toko.homeScreen.model.linkChangerModel.Rating>> =
        _ratingList


    private val preSelectedRating =
        MutableStateFlow<com.project.toko.homeScreen.model.linkChangerModel.Rating?>(null)
    val selectedRating: StateFlow<com.project.toko.homeScreen.model.linkChangerModel.Rating?> =
        preSelectedRating

    private val _selectedRating =
        MutableStateFlow<com.project.toko.homeScreen.model.linkChangerModel.Rating?>(null)

    fun setSelectedRating(rating: com.project.toko.homeScreen.model.linkChangerModel.Rating) {
        preSelectedRating.value = if (rating == preSelectedRating.value) null else rating
    }


    private val pre_min_score =
        MutableStateFlow<com.project.toko.homeScreen.model.linkChangerModel.Score?>(null)
    private val _min_score =
        MutableStateFlow<com.project.toko.homeScreen.model.linkChangerModel.Score?>(null)


    fun setSelectedMinScore(score: com.project.toko.homeScreen.model.linkChangerModel.Score) {
        pre_min_score.value = if (score == pre_min_score.value) null else score
    }

    private val pre_max_score =
        MutableStateFlow<com.project.toko.homeScreen.model.linkChangerModel.Score?>(null)
    private val _max_score =
        MutableStateFlow<com.project.toko.homeScreen.model.linkChangerModel.Score?>(null)

    fun setSelectedMaxScore(score: com.project.toko.homeScreen.model.linkChangerModel.Score) {
        pre_max_score.value = if (score == pre_max_score.value) null else score
    }

    private val preSelectedMinMax = mutableStateOf(false)
    val selectedMinMax: MutableState<Boolean> = preSelectedMinMax

    private val _scoreState = mutableStateOf(0)
    val scoreState = _scoreState


    private val _safeForWork = mutableStateOf(false)
    var safeForWork = _safeForWork

    private val _typeList =
        MutableStateFlow(com.project.toko.homeScreen.model.linkChangerModel.getTypes())
    val typeList: StateFlow<List<com.project.toko.homeScreen.model.linkChangerModel.Types>> =
        _typeList
    private val pre_selectedType =
        MutableStateFlow<com.project.toko.homeScreen.model.linkChangerModel.Types?>(null)
    val selectedType: StateFlow<com.project.toko.homeScreen.model.linkChangerModel.Types?> =
        pre_selectedType
    private val _selectedType =
        MutableStateFlow<com.project.toko.homeScreen.model.linkChangerModel.Types?>(null)

    fun setSelectedType(type: com.project.toko.homeScreen.model.linkChangerModel.Types) {
        pre_selectedType.value = if (type == pre_selectedType.value) null else type
    }

    private val _orderByList =
        MutableStateFlow(com.project.toko.homeScreen.model.linkChangerModel.getOrderBy())
    val orderByList: StateFlow<List<com.project.toko.homeScreen.model.linkChangerModel.OrderBy>> =
        _orderByList
    private val pre_selectedOrderBy =
        MutableStateFlow<com.project.toko.homeScreen.model.linkChangerModel.OrderBy?>(null)
    val selectedOrderBy: StateFlow<com.project.toko.homeScreen.model.linkChangerModel.OrderBy?> =
        pre_selectedOrderBy
    private val _selectedOrderBy =
        MutableStateFlow<com.project.toko.homeScreen.model.linkChangerModel.OrderBy?>(null)

    fun setSelectedOrderBy(orderBy: com.project.toko.homeScreen.model.linkChangerModel.OrderBy) {
        pre_selectedOrderBy.value = if (orderBy == pre_selectedOrderBy.value) null else orderBy
    }


    init {
        viewModelScope.launch(Dispatchers.IO) {
            searchDebouncer
                .debounce(1000L)
                .collectLatest { searchQuery ->
                    performSearch(searchQuery)
                }
        }
    }

    fun onSearchTextChange(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchText.value = text
            searchDebouncer.emit(text)
        }
    }

    // note: if you hit the1"ok" button without tapping on genres - it will show you whole list of animes.
    private suspend fun performSearch(query: String?) {
        var currentQuery = query
        var currentGenres = _genres.value
        try {
            // This "if" statement is temporary added because
            // Jikan.Api isn't working properly with query that
            // contains less then 3 letters.

            _currentPage.value = 1
            _isPerformingSearch.value = true

            if (currentQuery == "") {
                currentQuery = null
            }
            if (currentGenres == "") {
                currentGenres = null
            }
            val response = malApiRepository.getAnimeSearchByName(
                sfw = safeForWork.value,
                query = currentQuery,
                page = currentPage.value,
                genres = currentGenres,
                rating = _selectedRating.value?.ratingName,
                type = _selectedType.value?.typeName,
                orderBy = _selectedOrderBy.value?.orderBy,
                max_score = _max_score.value?.score,
                min_score = _min_score.value?.score

            ).body()

            if (response != null) {
                hasNextPage.value = response.pagination.has_next_page
                _animeSearch.value = response
            }


        } catch (e: Exception) {
            Log.e("HomeScreenViewModel", "Failed to perform search: ${e.message}")
        } finally {
            _isPerformingSearch.value = false
        }
    }

    fun loadNextPage() {
        val query = searchText.value
        var currentGenres = _genres.value
        var currentQuery = query
        if (currentQuery == "") {
            currentQuery = null
        }
        if (currentGenres == "") {
            currentGenres = null
        }
        if (!hasNextPage.value) {
            return
        }


        viewModelScope.launch(Dispatchers.IO) {
            try {
                val nextPage = currentPage.value + 1
                val response =
                    malApiRepository.getAnimeSearchByName(
                        sfw = safeForWork.value,
                        query = currentQuery,
                        page = nextPage,
                        genres = currentGenres,
                        rating = _selectedRating.value?.ratingName,
                        type = _selectedType.value?.typeName,
                        orderBy = _selectedOrderBy.value?.orderBy,
                        max_score = _max_score.value?.score,
                        min_score = _min_score.value?.score

                    ).body()


                if (response != null) {
                    hasNextPage.value = response.pagination.has_next_page
                }

                response?.let { newAnimeSearchModel ->
                    _animeSearch.value =
                        _animeSearch.value.copy(data = _animeSearch.value.data + newAnimeSearchModel.data)
                    _currentPage.value = nextPage
                    _isNextPageLoading.value = newAnimeSearchModel.pagination.has_next_page
                }

            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Failed to load next page: ${e.message}")
            } finally {
                _isNextPageLoading.value = false
            }
        }
    }


    fun tappingOnGenre(number: Int) {
        if (arrayOfGenres.value.contains(number)) {
            arrayOfGenres.value.remove(number)
        } else {
            arrayOfGenres.value.add(number)
        }
    }


    private fun makeArrayToLinkWithCommas(array: ArrayList<Int>): String {
        if (array.isEmpty()) {
            return ""
        }

        val stringBuilder = StringBuilder()
        for (i in 0 until array.size - 1) {
            stringBuilder.append(array[i])
            stringBuilder.append(",")
        }
        stringBuilder.append(array[array.size - 1])

        Log.d("link", stringBuilder.toString())
        return stringBuilder.toString()
    }


    suspend fun addAllParams() {
        val jobGenres = viewModelScope.async(Dispatchers.IO) {
            pre_genres = makeArrayToLinkWithCommas(arrayOfGenres.value)
            _genres.value = pre_genres
        }
        val jobRating = viewModelScope.async(Dispatchers.IO) {
            _selectedRating.value = preSelectedRating.value
        }
        val jobTypes = viewModelScope.async(Dispatchers.IO) {
            _selectedType.value = pre_selectedType.value
        }
        val jobOrderBy = viewModelScope.async(Dispatchers.IO) {
            _selectedOrderBy.value = pre_selectedOrderBy.value
        }
        val jobMinMaxScore = viewModelScope.async(Dispatchers.IO) {
            _min_score.value = pre_min_score.value
            _max_score.value = pre_max_score.value
        }

        jobGenres.join()
        jobRating.join()
        jobTypes.join()
        jobOrderBy.join()
        jobMinMaxScore.join()


        performSearch(searchText.value)

    }


    //------------------------------------------------------
    var isDialogShown by mutableStateOf(false)
        private set


    private val _selectedAnimeId = MutableStateFlow<Int?>(null)
    val selectedAnimeId: StateFlow<Int?> = _selectedAnimeId.asStateFlow()


    fun onDialogLongClick(animeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedAnimeId.value = animeId
            isDialogShown = true
        }
    }

    fun onDialogDismiss() {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedAnimeId.value = null
            isDialogShown = false
        }
    }
}


