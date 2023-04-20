
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.repository.ApiService.Companion.api
import com.example.animeapp.domain.searchModel.Data

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.UnknownHostException


class HomeScreenViewModel : ViewModel() {

    private val animeRepository = api

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _animeList = MutableStateFlow<List<Data>>(emptyList())
    val animeList = _animeList.asStateFlow()

    private val searchDebouncer = MutableSharedFlow<String>()

    init {
        // Start collecting search queries with Debounce
        viewModelScope.launch {
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

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        viewModelScope.launch {
            searchDebouncer.emit(text)
        }
    }

    private suspend fun performSearch(query: String) {
        try {
            _isSearching.value = true
            val response = animeRepository.getAnimeSearchByName(query).body()
            _animeList.value = response?.data ?: emptyList()
        } catch (e: NullPointerException) {
            Log.e("HomeScreenViewModel", e.message.toString())
        } catch (e: UnknownHostException) {
            Log.e("HomeScreenViewModel", "Connection failed: " + e.message.toString())
        } finally {
            _isSearching.value = false
        }
    }
}
