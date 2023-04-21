package com.example.animeapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.repository.ApiService
import com.example.animeapp.domain.staffModel.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaffViewModel : ViewModel() {

    private val animeRepository = ApiService.api
    private val _staffList = MutableStateFlow<List<Data>>(emptyList())
    val staffList = _staffList.asStateFlow()


    fun addStaffFromId(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = animeRepository.getStaffFromId(id)
                    if (response.isSuccessful) {
                        _staffList.value = response.body()?.data ?: emptyList()
                    }
                } catch (e: Exception) {
                    Log.e("DetailScreenViewModel", e.message.toString())
                }
            }
        }
    }
}