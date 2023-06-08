//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.animeapp.repository.MalApiService
//import com.example.animeapp.domain.models.newAnimeSearchModel.Items
//import com.example.animeapp.domain.models.newAnimeSearchModel.NewAnimeSearchModel
//import com.example.animeapp.domain.models.newAnimeSearchModel.Pagination
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.FlowPreview
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.net.UnknownHostException
//
//
//
//
//@OptIn(FlowPreview::class)
//class HomeScreenViewModel(malApiRepository: MalApiService.Companion) : ViewModel() {
//    private val searchCache = mutableMapOf<String, NewAnimeSearchModel>() // Move searchCache here
//    private val searchResultsCache = mutableMapOf<String, NewAnimeSearchModel>()
//
//    private val searchResults = mutableListOf<NewAnimeSearchModel>()
//
//    private val _isPerformingSearch = MutableStateFlow(false)
//    val isPerformingSearch = _isPerformingSearch.asStateFlow()
//
//    private val _currentPage = MutableStateFlow(1)
//    val currentPage = _currentPage.asStateFlow()
//
//    private val _totalPages = MutableStateFlow(0)
//    val totalPages = _totalPages.asStateFlow()
//
//    private val _isNextPageLoading = MutableStateFlow(false)
//    val isNextPageLoading = _isNextPageLoading.asStateFlow()
//
//    private val emptyItem = Items(0, 0, 0)
//    private val emptyNewAnimeSearchModel = NewAnimeSearchModel(
//        data = ArrayList(),
//        pagination = Pagination(false, emptyItem, 0)
//    )
//    private val animeRepository = malApiRepository.api
//
//    private val _searchText = MutableStateFlow("")
//    val searchText = _searchText.asStateFlow()
//
//    private val _isSearching = MutableStateFlow(false)
//    val isSearching = _isSearching.asStateFlow()
//
//    private val _animeSearch = MutableStateFlow(
//        emptyNewAnimeSearchModel
//    )
//    val animeSearch = _animeSearch.asStateFlow()
//
//    private val searchDebouncer = MutableSharedFlow<String>()
//
//    init {
//        // Start collecting search queries with Debounce
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                searchDebouncer
//                    .debounce(500L)
//                    .distinctUntilChanged()
//                    .collectLatest { searchQuery ->
//                        if (searchQuery.isNotBlank()) {
//                            _currentPage.value = 1
//                            performSearch(searchQuery)
//                        } else {
//                            _animeSearch.value = emptyNewAnimeSearchModel
//                        }
//                    }
//            }
//        }
//    }
//
//    fun onSearchTextChange(text: String) {
//        _searchText.value = text
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                if (!isPerformingSearch.value) {
//                    searchDebouncer.emit(text)
//                }
//            }
//        }
//    }
//
//
//
//
//
//    private suspend fun performSearch(query: String) {
//        try {
//            _isSearching.value = true
//            _isPerformingSearch.value = true
//
//            val cacheKey = "$query:${currentPage.value}"
//            val cachedResult = searchResultsCache[cacheKey]
//
//            if (cachedResult != null) {
//                _animeSearch.value = cachedResult
//            } else {
//                // Clear the search cache if it's a new query
//                if (currentPage.value == 1) {
//                    searchCache.clear()
//                }
//
//                val response = animeRepository.getAnimeSearchByName(
//                    nameOfAnime = query,
//                    page = currentPage.value
//                ).body()
//
//                val anime = response ?: emptyNewAnimeSearchModel
//                searchResultsCache[cacheKey] = anime
//                searchCache[query] = anime
//
//                // Store the search result in searchResults
//                if (currentPage.value == 1) {
//                    searchResults.add(anime)
//                }
//
//                _animeSearch.value = anime
//
//                // Update totalPages
//                _totalPages.value = anime.pagination.items.total
//            }
//        } catch (e: NullPointerException) {
//            Log.e("HomeScreenViewModel", e.message.toString())
//        } catch (e: UnknownHostException) {
//            Log.e("HomeScreenViewModel", "Connection failed: " + e.message.toString())
//        } finally {
//            _isSearching.value = false
//            _isPerformingSearch.value = false
//        }
//    }
//
//
//
//
//
//
//
//    fun loadNextPage(newAnimeSearchModel: NewAnimeSearchModel) {
//        val query = searchText.value
//        if (isNextPageLoading.value || query.isBlank() || currentPage.value >= totalPages.value) {
//            return
//        }
//
//        viewModelScope.launch {
//            try {
//                if (newAnimeSearchModel.pagination.has_next_page) {
//                    val nextPage = _currentPage.value + 1
//                    val response = animeRepository.getAnimeSearchByName(nameOfAnime = query, page = nextPage).body()
//
//                    if (response != null) {
//                        if (response.pagination.has_next_page) {
//                            _isNextPageLoading.value = true
//                        }
//                        searchCache[query] = response
//
//                        val currentData = _animeSearch.value.data.toMutableList()
//                        currentData.addAll(response.data)
//
//                        _animeSearch.value = response.copy(data = currentData)
//
//                        _currentPage.value = nextPage
//
//                        // Update totalPages and isNextPageLoading
//                        _totalPages.value = response.pagination.items.total
//                        _isNextPageLoading.value = response.pagination.has_next_page
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("HomeScreenViewModel", "Failed to load next page: ${e.message}")
//            } finally {
//                _isNextPageLoading.value = false
//            }
//        }
//    }
//
//}


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.models.newAnimeSearchModel.Items
import com.example.animeapp.repository.MalApiService
import com.example.animeapp.domain.models.newAnimeSearchModel.NewAnimeSearchModel
import com.example.animeapp.domain.models.newAnimeSearchModel.Pagination
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeScreenViewModel(private val malApiRepository: MalApiService) : ViewModel() {

    private val emptyItem = Items(0, 0, 0)
    private val emptyNewAnimeSearchModel = NewAnimeSearchModel(
        data = ArrayList(),
        pagination = Pagination(false, emptyItem, 0)
    )

    private val _isPerformingSearch = MutableStateFlow(false)
    val isPerformingSearch = _isPerformingSearch.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage.asStateFlow()

    private val _totalPages = MutableStateFlow(0)
    val totalPages = _totalPages.asStateFlow()

    private val _isNextPageLoading = MutableStateFlow(false)
    val isNextPageLoading = _isNextPageLoading.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _animeSearch = MutableStateFlow(emptyNewAnimeSearchModel)
    val animeSearch = _animeSearch.asStateFlow()

    private val searchDebouncer = MutableSharedFlow<String>()

    init {
        viewModelScope.launch {
            searchDebouncer
                .debounce(500L)
                .distinctUntilChanged()
                .collectLatest { searchQuery ->
                    if (searchQuery.isNotBlank()) {
                        _currentPage.value = 1
                        performSearch(searchQuery)
                    } else {
                        _animeSearch.value = emptyNewAnimeSearchModel
                    }
                }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        viewModelScope.launch {
            if (!isPerformingSearch.value) {
                searchDebouncer.emit(text)
            }
        }
    }

    private suspend fun performSearch(query: String) {
        try {
            _isPerformingSearch.value = true

            val response = malApiRepository.getAnimeSearchByName(nameOfAnime = query, page = currentPage.value).body()

            val anime = response ?: emptyNewAnimeSearchModel
            _animeSearch.value = anime

            _totalPages.value = anime.pagination.items.total
        } catch (e: Exception) {
            Log.e("HomeScreenViewModel", "Failed to perform search: ${e.message}")
        } finally {
            _isPerformingSearch.value = false
        }
    }

    fun loadNextPage() {
        val query = searchText.value
        if (isNextPageLoading.value || query.isBlank() || currentPage.value >= totalPages.value) {
            return
        }

        viewModelScope.launch {
            try {
                val nextPage = currentPage.value + 1
                val response = malApiRepository.getAnimeSearchByName(nameOfAnime = query, page = nextPage).body()

                response?.let { newAnimeSearchModel ->
                    _animeSearch.value = _animeSearch.value.copy(data = _animeSearch.value.data + newAnimeSearchModel.data)
                    _currentPage.value = nextPage
                    _isNextPageLoading.value = newAnimeSearchModel.pagination.has_next_page
                    _totalPages.value = newAnimeSearchModel.pagination.items.total
                }
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Failed to load next page: ${e.message}")
            } finally {
                _isNextPageLoading.value = false
            }
        }
    }
}

