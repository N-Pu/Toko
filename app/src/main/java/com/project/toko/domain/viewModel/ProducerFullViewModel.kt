package com.project.toko.domain.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.toko.repository.MalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProducerFullViewModel(private val malApiService: MalApiService): ViewModel() {
    private val _producerFull = MutableStateFlow<com.project.toko.domain.models.producerModel.Data?>(null)
    private val producerCache = mutableMapOf<Int, com.project.toko.domain.models.producerModel.Data?>()
    val producerFull = _producerFull.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    fun getProducerFromId(mal_id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val cachedProducer = producerCache[mal_id]
            if (cachedProducer != null) {
                _producerFull.value = cachedProducer
                return@launch
            }

            try {
                _isSearching.value = true
                val response = malApiService.getProducerFullFromId(mal_id)
                if (response.isSuccessful) {
                    val producer = response.body()?.data
                    producerCache[mal_id] = producer
                    _producerFull.value = producer
                }
            } catch (e: Exception) {
                Log.e("StaffFullByIdViewModel", e.message.toString())
            } finally {
                _isSearching.value = false
            }
        }
    }

}