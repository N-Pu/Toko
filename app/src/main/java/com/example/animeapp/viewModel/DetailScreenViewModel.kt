package com.example.animeapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.detailModel.Data
import com.example.animeapp.domain.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailScreenViewModel(malApiService: MalApiService.Companion) : ViewModel() {
    private val animeRepository = malApiService.api
    private val _animeDetails = MutableStateFlow<Data?>(null)
    val animeDetails: StateFlow<Data?> get() = _animeDetails
    private val loadedIds = mutableMapOf<Int, Boolean>()
    private val animeCache = mutableMapOf<Int, Data?>()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

//    Таким образом, сначала мы проверяем, есть ли данные в кеше, и если они есть,
//    то устанавливаем их как текущие данные и выходим из функции.
//    Затем мы проверяем, загружались ли данные ранее, но их нет в кеше.
//    Если они загружались, то мы просто выходим из функции.
//    Если же данных еще нет, то мы загружаем их с сервера, сохраняем в кеше и устанавливаем как текущие.

    fun onTapAnime(id: Int) {
        val cachedAnime = animeCache[id]
        if (cachedAnime != null) {
            _animeDetails.value = cachedAnime
            return // Если данные есть в кеше, выходим
        }
        if (loadedIds[id] == true) {
            return // Если данные уже загружены, но их нет в кеше, выходим
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _isSearching.value = true
                    val response = animeRepository.getDetailsFromAnime(id)
                    if (response.isSuccessful) {
                        response.body()?.data.let { data ->
                            withContext(Dispatchers.Main) {
                                _animeDetails.value = data
                                loadedIds[id] = true // Помечаем, что данные загружены
                                animeCache[id] = data // Сохраняем данные в кеш
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("DetailScreenViewModel", e.message.toString())
                } finally {
                    _isSearching.value = false
                }
            }
        }
    }

}
