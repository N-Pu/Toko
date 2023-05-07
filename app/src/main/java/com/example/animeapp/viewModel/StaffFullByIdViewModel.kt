package com.example.animeapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.animeapp.domain.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StaffFullByIdViewModel(malApiService: MalApiService.Companion) : ViewModel() {
    private val animeRepository = malApiService.api
    private val _staffFull =
        MutableStateFlow<com.example.animeapp.domain.staffMemberFullModel.Data?>(null)
    private val staffCache =
        mutableMapOf<Int, com.example.animeapp.domain.staffMemberFullModel.Data?>()
    val staffFull = _staffFull.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    fun getStaffFromId(mal_id: Int) {
        viewModelScope.launch {
            val cachedCharacters = staffCache[mal_id]
            if (cachedCharacters != null) {
                _staffFull.value = cachedCharacters
                return@launch
            }

            withContext(Dispatchers.IO) {
                try {
                    _isSearching.value = true
                    staffCache.clear()
                    val response = animeRepository.getStaffFullFromId(mal_id)
                    if (response.isSuccessful) {
                        val staffMembor = response.body()?.data
                        staffCache[mal_id] = staffMembor
                        _staffFull.value = staffMembor
                    }
                } catch (e: Exception) {
                    Log.e("StaffFullByIdViewModel", e.message.toString())
                } finally {
                    _isSearching.value = false
                }
            }


        }
    }


}