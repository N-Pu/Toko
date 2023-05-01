package com.example.animeapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.repository.MalApiService
import com.example.animeapp.domain.staffModel.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaffViewModel(malApiService: MalApiService.Companion) : ViewModel() {

    private val animeRepository = malApiService.api
    private val staffCache = mutableMapOf<Int, List<Data>>()

    private val _staffList = MutableStateFlow<List<Data>>(emptyList())
    val staffList = _staffList.asStateFlow()

    fun addStaffFromId(id: Int) {
        viewModelScope.launch {
            val cachedStaff = staffCache[id]
            if (cachedStaff != null) {
                _staffList.value = cachedStaff
                return@launch
            }

            withContext(Dispatchers.IO) {
                staffCache.clear()
                try {
                    val response = animeRepository.getStaffFromId(id)
                    if (response.isSuccessful) {
                        val staff = response.body()?.data ?: emptyList()
                        staffCache[id] = staff
                        _staffList.value = staff
                    }
                } catch (e: Exception) {
                    Log.e("StaffViewModel", e.message.toString())
                }
            }
        }
    }

}