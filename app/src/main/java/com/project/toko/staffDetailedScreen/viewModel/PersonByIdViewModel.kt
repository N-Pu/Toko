package com.project.toko.staffDetailedScreen.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.staffDetailedScreen.model.personFullModel.Data
import com.project.toko.core.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonByIdViewModel(malApiService: MalApiService) : ViewModel() {
    private val animeRepository = malApiService
    private val _personFull = MutableStateFlow<Data?>(null)
    private val personCache = mutableMapOf<Int, Data?>()
    val personFull = _personFull.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    fun getPersonFromId(mal_id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val cachedStaff = personCache[mal_id]
            if (cachedStaff != null) {
                _personFull.value = cachedStaff
                return@launch
            }

            try {
                _isSearching.value = true
                val response = animeRepository.getPersonFullFromId(mal_id)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    personCache[mal_id] = data
                    _personFull.value = data
                }
            } catch (e: Exception) {
                Log.e("StaffFullByIdViewModel", e.message.toString())
            } finally {
                _isSearching.value = false
            }
        }
    }

}
