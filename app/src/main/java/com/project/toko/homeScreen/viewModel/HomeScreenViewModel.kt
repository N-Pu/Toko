package com.project.toko.homeScreen.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.core.utils.connectionCheck.isInternetAvailable
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@OptIn(FlowPreview::class)
class HomeScreenViewModel @Inject constructor(
    private val malApiRepository: MalApiService,
    private val dao: MainDb,
    private val context: Context
) : ViewModel() {

    private val emptyItem = Items(0, 0, 0)
    private val emptyNewAnimeSearchModel = NewAnimeSearchModel(
        data = ArrayList(), pagination = Pagination(
            false, emptyItem, 0
        )
    )


    private val _isLoadingSearch = mutableStateOf(false)
    var isLoadingSearch = _isLoadingSearch

    private val _currentPage = MutableStateFlow(1)
    private val currentPage = _currentPage.asStateFlow()

    private val _isNextPageLoading = MutableStateFlow(false)
//    val isNextPageLoading = _isNextPageLoading.asStateFlow()

    private val _searchText = MutableStateFlow<String?>(null)
    val searchText = _searchText.asStateFlow()

    private val _animeSearch = MutableStateFlow(emptyNewAnimeSearchModel)
    val animeSearch = _animeSearch.asStateFlow()

    private val searchDebouncer = MutableSharedFlow<String>()

    private var hasNextPage = MutableStateFlow(true)


    // _____________________________________________________________________ //

    //link parameters section

    private val arrayOfGenres = MutableStateFlow(arrayListOf<Int>())

    private val preSelectedGenre = MutableStateFlow(getGenres())
    val selectedGenre = preSelectedGenre


    private var pre_genres = ""

    private val _genres = MutableStateFlow<String?>(null)

    private val _ratingList = MutableStateFlow(getRating())
    val ratingList = _ratingList


    private val preSelectedRating = MutableStateFlow<Rating?>(null)
    val selectedRating = preSelectedRating

    private val _selectedRating = MutableStateFlow<Rating?>(null)

    fun setSelectedRating(rating: Rating) {
        preSelectedRating.value = if (rating == preSelectedRating.value) null else rating
    }


    private val _pre_min_score = MutableStateFlow<Score?>(null)
    val pre_min_score = _pre_min_score
    private val _min_score = MutableStateFlow<Score?>(null)


    private val _pre_max_score = MutableStateFlow<Score?>(null)
    val pre_max_score = _pre_max_score
    private val _max_score = MutableStateFlow<Score?>(null)

    private val _scoreState = mutableIntStateOf(0)
    val scoreState = _scoreState

    private val _isNSFWActive = mutableStateOf(false)
    val isNSFWActive = _isNSFWActive

    fun saveNSFWData(isActive: Boolean) {
        val sharedPreferences = context.getSharedPreferences("NSFW Mode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("NSFW_MODE", isActive)
        editor.apply()
        _isNSFWActive.value = isActive
    }

    fun loadNSFWData() {
        val sharedPreferences = context.getSharedPreferences("NSFW Mode", Context.MODE_PRIVATE)
        _isNSFWActive.value = sharedPreferences.getBoolean("NSFW_MODE", false)
    }

    private val _typeList = MutableStateFlow(getTypes())
    val typeList = _typeList

    private val pre_selectedType = MutableStateFlow<Types?>(null)
    val selectedType: StateFlow<Types?> = pre_selectedType
    private val _selectedType = MutableStateFlow<Types?>(null)

    fun setSelectedType(type: Types) {
        pre_selectedType.value = if (type == pre_selectedType.value) null else type
    }

    private val _orderByList = MutableStateFlow(getOrderBy())
    val orderByList = _orderByList
    private val pre_selectedOrderBy = MutableStateFlow<OrderBy?>(null)
    val selectedOrderBy: StateFlow<OrderBy?> = pre_selectedOrderBy
    private val _selectedOrderBy = MutableStateFlow<OrderBy?>(null)

    fun setSelectedOrderBy(orderBy: OrderBy) {
        pre_selectedOrderBy.value = if (orderBy == pre_selectedOrderBy.value) null else orderBy
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            searchDebouncer.debounce(1000L).collectLatest { searchQuery ->
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
                    context, e.message, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private val cachedSearch: MutableMap<String, NewAnimeSearchModel> = mutableMapOf()

    private fun generateRequestKey(
        query: String?,
        genres: String?,
        type: String?,
        rating: String?,
        orderBy: String?,
        maxScore: String?,
        minScore: String?
    ): String {
        // Customize this key generation based on your needs, concatenating the query parameters
        return "${MalApiService.BASE_URL}v4/anime?" +
                "q=${query ?: ""}&" +
                "genres=${genres ?: ""}&" +
                "type=${type ?: ""}&" +
                "rating=${rating ?: ""}&" +
                "orderBy=${orderBy ?: ""}&" +
                "max_score=${maxScore ?: ""}&" +
                "min_score=${minScore ?: ""}"
    }


    private suspend fun performSearch(query: String?) {
        try {
            // Existing code
            var currentQuery = query
            var currentGenres = _genres.value
            _currentPage.value = 1
            _isLoadingSearch.value = true

            if (currentQuery == "") {
                currentQuery = null
            }
            if (currentGenres == "") {
                currentGenres = null
            }
            val requestKey = generateRequestKey(
                query = currentQuery,
                genres = currentGenres,
                type = _selectedType.value?.typeName,
                rating = _selectedRating.value?.ratingName,
                orderBy = _selectedOrderBy.value?.orderBy,
                maxScore = _max_score.value?.score,
                minScore = _min_score.value?.score
            )

            if (cachedSearch.containsKey(requestKey)) {
                val cachedResponse = cachedSearch[requestKey]
                // Use cached response if needed
                _animeSearch.value = cachedResponse!!
            } else {
                val response = malApiRepository.getAnimeSearchByName(
                    eTag = query + currentPage.value,
                    sfw = !_isNSFWActive.value,
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

                    // Cache the response
                    cachedSearch[requestKey] = response
                }
            }
        } catch (e: Exception) {
            Log.e("HomeScreenViewModel", "Failed to perform search: ${e.message}")
        } finally {
            _isLoadingSearch.value = false
        }
    }


    private val _topTrendingAnime = MutableStateFlow(emptyNewAnimeSearchModel)
    val topTrendingAnime = _topTrendingAnime.asStateFlow()

    private val _topAiringAnime = MutableStateFlow(emptyNewAnimeSearchModel)
    val topAiringAnime = _topAiringAnime.asStateFlow()

    private val _topUpcomingAnime = MutableStateFlow(emptyNewAnimeSearchModel)
    val topUpcomingAnime = _topUpcomingAnime.asStateFlow()


    private val cachedTopTrendingAnime: MutableMap<String, NewAnimeSearchModel> = mutableMapOf()

    private val _loadingSectionTopAiring = mutableStateOf(false)
    val loadingSectionTopAiring = _loadingSectionTopAiring

    private val _loadingSectionTopUpcoming = mutableStateOf(false)
    val loadingSectionTopUpcoming = _loadingSectionTopUpcoming

    private val _loadingSectionTopTrending = mutableStateOf(false)
    val loadingSectionTopTrending = _loadingSectionTopTrending
    private suspend fun getTopAnime(
        filter: String,
        limit: Int,
        data: MutableStateFlow<NewAnimeSearchModel>,
        loadingCurrentSection: MutableState<Boolean>,
        sfw: Boolean
    ) {
        try {
//            if (isInternetAvailable(context)) {
            viewModelScope.launch(Dispatchers.IO) {
                val cachedData = cachedTopTrendingAnime[filter]
                if (cachedData != null) {
                    // Если данные уже есть в кэше, используем их
                    data.value = cachedTopTrendingAnime[filter]!!
                } else {
                    loadingCurrentSection.value = true
                    // Если данные отсутствуют в кэше, делаем запрос к API
                    val response = malApiRepository.getTenTopAnime(filter, limit, sfw).body()
                    val newData = response ?: emptyNewAnimeSearchModel
                    loadingCurrentSection.value = false

                    // Сохраняем новые данные в кэше
                    cachedTopTrendingAnime[filter] = newData

                    data.value = newData
                }
            }
//            } else {
//                // Если нет интернета, показываем сообщение
//                viewModelScope.launch(Dispatchers.Main) {
//                    Toast.makeText(
//                        context,
//                        "No internet connection!",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
        } catch (e: Exception) {
            // Если произошла ошибка, показываем сообщение
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context, e.message, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    suspend fun loadNextPage() {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val query = searchText.value
                var currentQuery = query
                if (currentQuery == "") {
                    currentQuery = null
                }
                if (!hasNextPage.value) {
                    return@launch
                }
                val nextPage = currentPage.value + 1

                val response = malApiRepository.getAnimeSearchByName(
                    eTag = query + currentPage.value,
                    sfw = !_isNSFWActive.value,
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
            viewModelScope.async(Dispatchers.IO) {
                performSearch(searchText.value)
            }.join()
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context, e.message, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    suspend fun reloadAllParamsAndClearCache(query: String?) {
        var currentQuery = query
        var currentGenres = _genres.value
        _currentPage.value = 1
        _isLoadingSearch.value = true

        if (currentQuery == "") {
            currentQuery = null
        }
        if (currentGenres == "") {
            currentGenres = null
        }

        val requestKey = generateRequestKey(
            query = currentQuery,
            genres = currentGenres,
            type = _selectedType.value?.typeName,
            rating = _selectedRating.value?.ratingName,
            orderBy = _selectedOrderBy.value?.orderBy,
            maxScore = _max_score.value?.score,
            minScore = _min_score.value?.score
        )
        cachedSearch.remove(requestKey)

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
            viewModelScope.async(Dispatchers.IO) {
                performSearch(searchText.value)
            }.join()
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context, e.message, Toast.LENGTH_SHORT
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
                    context, e.message, Toast.LENGTH_SHORT
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
                    context, e.message, Toast.LENGTH_SHORT
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

    suspend fun loadAllSections(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isInternetAvailable(context)) {
                getTopAnime(
                    "bypopularity",
                    25,
                    _topTrendingAnime,
                    loadingSectionTopTrending,
                    !_isNSFWActive.value
                )
                delay(500L)
                getTopAnime(
                    "airing",
                    25,
                    _topAiringAnime,
                    loadingSectionTopAiring,
                    !_isNSFWActive.value
                )
                delay(500L)
                getTopAnime(
                    "upcoming",
                    25,
                    _topUpcomingAnime,
                    loadingSectionTopUpcoming,
                    !_isNSFWActive.value
                )
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context, "No internet connection!", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    suspend fun reloadAllSectionAndCache(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isInternetAvailable(context)) {
                cachedTopTrendingAnime.clear()
                getTopAnime(
                    "bypopularity",
                    25,
                    _topTrendingAnime,
                    loadingSectionTopTrending,
                    !_isNSFWActive.value
                )
                delay(500L)
                getTopAnime(
                    "airing",
                    25,
                    _topAiringAnime,
                    loadingSectionTopAiring,
                    !_isNSFWActive.value
                )
                delay(500L)
                getTopAnime(
                    "upcoming",
                    25,
                    _topUpcomingAnime,
                    loadingSectionTopUpcoming,
                    !_isNSFWActive.value
                )
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context, "No internet connection!", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}



