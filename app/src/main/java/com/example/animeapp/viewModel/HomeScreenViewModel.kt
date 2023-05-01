import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.repository.MalApiService
import com.example.animeapp.domain.repository.MalApiService.Companion.api
import com.example.animeapp.domain.searchModel.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException


@OptIn(FlowPreview::class)
class HomeScreenViewModel(malApiRepository: MalApiService.Companion) : ViewModel() {

    private val animeRepository = malApiRepository.api

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _animeList = MutableStateFlow<List<Data>>(emptyList())
    val animeList = _animeList.asStateFlow()

    private val searchDebouncer = MutableSharedFlow<String>()

    private val searchCache = mutableMapOf<String, List<Data>>()


    init {
        // Start collecting search queries with Debounce
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchDebouncer
                    .debounce(500L)
                    .distinctUntilChanged()
                    .collectLatest { searchQuery ->
                        if (searchQuery.isNotBlank()) {
                            performSearch(searchQuery)
                        } else {
                            _animeList.value = emptyList()
                        }
                    }
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchDebouncer.emit(text)
            }

        }
    }

    private suspend fun performSearch(query: String) {
        try {
            _isSearching.value = true
            val cachedResult = searchCache[query]
            if (cachedResult != null) {
                _animeList.value = cachedResult
            } else {
                val response = animeRepository.getAnimeSearchByName(query).body()
                val anime = response?.data ?: emptyList()
                searchCache[query] = anime
                _animeList.value = anime
            }
        } catch (e: NullPointerException) {
            Log.e("HomeScreenViewModel", e.message.toString())
        } catch (e: UnknownHostException) {
            Log.e("HomeScreenViewModel", "Connection failed: " + e.message.toString())
        } finally {
            _isSearching.value = false
        }
    }


}