package com.example.animeapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.castModel.Data
import com.example.animeapp.domain.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CastInDetailScreenViewModel(malApiService: MalApiService.Companion) : ViewModel() {
    private val animeRepository = malApiService.api
    private val castCache = mutableMapOf<Int, List<Data>>()
    private val _castList = MutableStateFlow<List<Data>>(emptyList())
    val castList = _castList.asStateFlow()

    fun addCastFromId(id: Int) {
        viewModelScope.launch {

            val cachedCharacters = castCache[id]
            if (cachedCharacters != null) {
                _castList.value = cachedCharacters
                return@launch
            }

            withContext(Dispatchers.IO) {
                try {
                    castCache.clear()
                    val response = animeRepository.getCharactersFromId(id)
                    if (response.isSuccessful) {
                        val characters = response.body()?.data ?: emptyList()
                        castCache[id] = characters
                        _castList.value = characters
                    }
                } catch (e: Exception) {
                    Log.e("CastInDetailScreenViewModel", e.message.toString())
                }
            }
        }
    }
}
