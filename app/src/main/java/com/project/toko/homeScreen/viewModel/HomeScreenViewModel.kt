package com.project.toko.homeScreen.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.core.connectionCheck.isInternetAvailable
import com.project.toko.core.dao.MainDb
import com.project.toko.core.repository.MalApiService
import com.project.toko.daoScreen.dao.AnimeItem
import com.project.toko.homeScreen.model.linkChangerModel.OrderBy
import com.project.toko.homeScreen.model.linkChangerModel.Rating
import com.project.toko.homeScreen.model.linkChangerModel.Score
import com.project.toko.homeScreen.model.linkChangerModel.Types
import com.project.toko.homeScreen.model.linkChangerModel.getGenres
import com.project.toko.homeScreen.model.linkChangerModel.getOrderBy
import com.project.toko.homeScreen.model.linkChangerModel.getRating
import com.project.toko.homeScreen.model.linkChangerModel.getTypes
import com.project.toko.homeScreen.model.newAnimeSearchModel.Items
import com.project.toko.homeScreen.model.newAnimeSearchModel.NewAnimeSearchModel
import com.project.toko.homeScreen.model.newAnimeSearchModel.Pagination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(FlowPreview::class)
class HomeScreenViewModel @Inject constructor(
    private val malApiRepository: MalApiService,
    private val dao: MainDb,
    private val context: Context
) : ViewModel() {

    private val emptyItem = Items(0, 0, 0)
    private val emptyNewAnimeSearchModel =
        NewAnimeSearchModel(
            data = ArrayList(),
            pagination = Pagination(
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
        MutableStateFlow(getGenres())
    val selectedGenre =
        preSelectedGenre


    private var pre_genres = ""

    private val _genres = MutableStateFlow<String?>(null)

    private val _ratingList =
        MutableStateFlow(getRating())
    val ratingList =
        _ratingList


    private val preSelectedRating =
        MutableStateFlow<Rating?>(null)
    val selectedRating =
        preSelectedRating

    private val _selectedRating =
        MutableStateFlow<Rating?>(null)

    fun setSelectedRating(rating: Rating) {
        preSelectedRating.value = if (rating == preSelectedRating.value) null else rating
    }


    private val _pre_min_score = MutableStateFlow<Score?>(null)
    val pre_min_score = _pre_min_score
    private val _min_score = MutableStateFlow<Score?>(null)


//    fun setSelectedMinScore(score: Score) {
//        pre_min_score.value = if (score == pre_min_score.value) null else score
//    }

    private val _pre_max_score = MutableStateFlow<Score?>(null)
    val pre_max_score = _pre_max_score
    private val _max_score = MutableStateFlow<Score?>(null)

//    fun setSelectedMaxScore(score: Score) {
//        pre_max_score.value = if (score == pre_max_score.value) null else score
//    }

//    private val preSelectedMinMax = mutableStateOf(false)
//    val selectedMinMax: MutableState<Boolean> = preSelectedMinMax

    private val _scoreState = mutableIntStateOf(0)
    val scoreState = _scoreState

    private val _safeForWork = mutableStateOf(false)
    var safeForWork = _safeForWork

    private val _typeList =
        MutableStateFlow(getTypes())
    val typeList =
        _typeList
    private val pre_selectedType =
        MutableStateFlow<Types?>(null)
    val selectedType: StateFlow<Types?> =
        pre_selectedType
    private val _selectedType =
        MutableStateFlow<Types?>(null)

    fun setSelectedType(type: Types) {
        pre_selectedType.value = if (type == pre_selectedType.value) null else type
    }

    private val _orderByList =
        MutableStateFlow(getOrderBy())
    val orderByList =
        _orderByList
    private val pre_selectedOrderBy =
        MutableStateFlow<OrderBy?>(null)
    val selectedOrderBy: StateFlow<OrderBy?> =
        pre_selectedOrderBy
    private val _selectedOrderBy =
        MutableStateFlow<OrderBy?>(null)

    fun setSelectedOrderBy(orderBy: OrderBy) {
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

    private val _switchIndicator = mutableStateOf(false)
    val switchIndicator = _switchIndicator

    private val _isTabMenuOpen = mutableStateOf(false)
    val isTabMenuOpen = _isTabMenuOpen
    fun onSearchTextChange(text: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                _searchText.value = text
                searchDebouncer.emit(text)
                _switchIndicator.value = true
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // note: if you hit "ok" button without tapping on genres - it will show you whole list of animes.
    private suspend fun performSearch(query: String?) {
        try {
            // This "if" statement is temporary added because
            // Jikan.Api isn't working properly with query that
            // contains less then 3 letters.

            var currentQuery = query
            var currentGenres = _genres.value
            _currentPage.value = 1
            _isPerformingSearch.value = true

            if (currentQuery == "") {
                currentQuery = null
            }
            if (currentGenres == "") {
                currentGenres = null
            }

            val response = malApiRepository.getAnimeSearchByName(
                eTag = query + currentPage.value,
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

    private val _topTrendingAnime = MutableStateFlow(emptyNewAnimeSearchModel)
    val topTrendingAnime = _topTrendingAnime.asStateFlow()

    private val _topAiringAnime = MutableStateFlow(emptyNewAnimeSearchModel)
    val topAiringAnime = _topAiringAnime.asStateFlow()

    private val _topUpcomingAnime = MutableStateFlow(emptyNewAnimeSearchModel)
    val topUpcomingAnime = _topUpcomingAnime.asStateFlow()


    suspend fun getTopTrendingAnime(filter: String, limit: Int = 10) {
        try {
            if (isInternetAvailable(context)) {
                viewModelScope.launch(Dispatchers.IO) {
                    _topTrendingAnime.value =
                        malApiRepository.getTenTopAnime(filter, limit).body()
                            ?: emptyNewAnimeSearchModel
                }
            } else {
                viewModelScope.launch(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "No internet connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    suspend fun getTopAiring(filter: String, limit: Int = 10) {
        try {
            if (isInternetAvailable(context)) {
                viewModelScope.launch(Dispatchers.IO) {
                    _topAiringAnime.value =
                        malApiRepository.getTenTopAnime(filter, limit).body()
                            ?: emptyNewAnimeSearchModel
                }
            } else {
                viewModelScope.launch(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "No internet connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    suspend fun getTopUpcoming(filter: String, limit: Int = 10) {
        try {
            if (isInternetAvailable(context)) {
                viewModelScope.launch(Dispatchers.IO) {
                    _topUpcomingAnime.value =
                        malApiRepository.getTenTopAnime(filter, limit).body()
                            ?: emptyNewAnimeSearchModel
                }
            } else {
                viewModelScope.launch(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "No internet connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
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
                        eTag = query + currentPage.value,
                        sfw = safeForWork.value,
                        query = currentQuery,
                        page = nextPage,
                        genres = makeArrayToLinkWithCommas(arrayOfGenres.value),
                        rating = preSelectedRating.value?.ratingName,
                        type = pre_selectedType.value?.typeName,
                        orderBy = pre_selectedOrderBy.value?.orderBy,
                        max_score = pre_max_score.value?.score,
                        min_score = pre_min_score.value?.score
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
        try {
            viewModelScope.async(Dispatchers.IO) {
                pre_genres = makeArrayToLinkWithCommas(arrayOfGenres.value)
                _genres.value = pre_genres
            }.join()
            viewModelScope.async(Dispatchers.IO) {
                _selectedRating.value = preSelectedRating.value
            }.join()
            viewModelScope.async(Dispatchers.IO) {
                _selectedType.value = pre_selectedType.value
            }.join()
            viewModelScope.async(Dispatchers.IO) {
                _selectedOrderBy.value = pre_selectedOrderBy.value
            }.join()
            viewModelScope.async(Dispatchers.IO) {
                _min_score.value = _pre_min_score.value
                _max_score.value = _pre_max_score.value
            }.join()

            performSearch(searchText.value)
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    //------------------------------------------------------
    var isDialogShown by mutableStateOf(false)
        private set


    private val _selectedAnimeId = MutableStateFlow<Int?>(null)
    val selectedAnimeId: StateFlow<Int?> = _selectedAnimeId.asStateFlow()


    fun onDialogLongClick(animeId: Int) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                _selectedAnimeId.value = animeId
                isDialogShown = true
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onDialogDismiss() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                _selectedAnimeId.value = null
                isDialogShown = false
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun showListOfWatching(): Flow<List<AnimeItem>> {
        return dao.getDao().getLastTenAnimeFromWatchingSection()
    }

    fun showLastAdded(): Flow<List<AnimeItem>> {
        return dao.getDao().getLastTenAddedAnime()
    }
}



