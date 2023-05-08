package com.example.animeapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.animeapp.domain.repository.MalApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StaffFullByIdViewModel(malApiService: MalApiService.Companion) : ViewModel() {
    private val animeRepository = malApiService.api
    private val _staffFull = MutableStateFlow<com.example.animeapp.domain.staffMemberFullModel.Data?>(null)
    private val staffCache = mutableMapOf<Int, com.example.animeapp.domain.staffMemberFullModel.Data?>()
    val staffFull = _staffFull.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    fun getStaffFromId(mal_id: Int) {
        viewModelScope.launch {
            val cachedStaff = staffCache[mal_id]
            if (cachedStaff != null) {
                _staffFull.value = cachedStaff
                return@launch
            }

            try {
                _isSearching.value = true
                val response = animeRepository.getStaffFullFromId(mal_id)
                if (response.isSuccessful) {
                    val staffMember = response.body()?.data
                    staffCache[mal_id] = staffMember
                    _staffFull.value = staffMember
                }
            } catch (e: Exception) {
                Log.e("StaffFullByIdViewModel", e.message.toString())
            } finally {
                _isSearching.value = false
            }
        }
    }

}
