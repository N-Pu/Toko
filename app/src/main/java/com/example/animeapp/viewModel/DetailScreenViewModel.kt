package com.example.animeapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.detailModel.Data
import com.example.animeapp.domain.repository.ApiService.Companion.api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DetailScreenViewModel : ViewModel() {
    private val animeRepository = api

    private val _animeDetails = MutableStateFlow<Data?>(null)
    val animeDetails: StateFlow<Data?> get() = _animeDetails

    fun onTapAnime(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = animeRepository.getDetailsFromAnime(id)
                    if (response.isSuccessful) {
                        response.body()?.data.let { data ->
                            withContext(Dispatchers.Main) {
                                _animeDetails.value = data
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("DetailScreenViewModel", e.message.toString())
                }
            }
        }
    }
}
