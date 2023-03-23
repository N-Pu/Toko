package com.example.animeapp.presentation.custom


import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.ui.domain.models.searchModel.Data
import com.example.animeapp.ui.domain.repository.ApiService
import kotlinx.coroutines.*


class MainViewModel : ViewModel() {
    private val api = ApiService.api
    private val _dataListResponse = MutableLiveData<List<Data>>()
    val dataListResponse = _dataListResponse
    private var errorMessage: String by mutableStateOf("")


    fun loadList() {
        try {
            viewModelScope.launch {
                dataListResponse.value = api.getAnimeModel().body()?.data
            }
        } catch (e: Exception) {
            errorMessage = e.message.toString()
            Log.e("ViewModel", errorMessage)
        }
    }


}

