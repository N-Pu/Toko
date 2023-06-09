
package com.example.animeapp.domain.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.repository.MalApiService
import com.example.animeapp.domain.models.staffModel.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StaffInDetailScreenViewModel(malApiService: MalApiService) : ViewModel() {

    private val animeRepository = malApiService
    private val staffCache = mutableMapOf<Int, List<Data>>()

    private val _staffList = MutableStateFlow<List<Data>>(emptyList())
    val staffList = _staffList.asStateFlow()

    fun addStaffFromId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val cachedStaff = staffCache[id]
            if (cachedStaff != null) {
                _staffList.value = cachedStaff
                return@launch
            }

            try {
                viewModelScope.launch(Dispatchers.IO) {
//                withContext(Dispatchers.IO) {
                    val response = animeRepository.getStaffFromId(id)
                    if (response.isSuccessful) {
                        val staff = response.body()?.data ?: emptyList()
                        staffCache[id] = staff
                        _staffList.value = staff
                    }
                }
            } catch (e: Exception) {
                Log.e("StaffViewModel", e.message.toString())
            } finally {
                if (_staffList.value.isEmpty()) {
                    _staffList.value = emptyList()
                }
            }
        }
    }
}
