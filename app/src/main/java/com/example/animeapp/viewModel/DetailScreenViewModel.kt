package com.example.animeapp.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.domain.detailModel.Broadcast
import com.example.animeapp.domain.detailModel.Data
import com.example.animeapp.domain.repository.ApiService.Companion.api
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object DetailScreenViewModel : ViewModel() {
    private val animeRepository = api

    private val _animeDetails = MutableStateFlow<Data?>(null)
    val animeDetails = _animeDetails.asStateFlow()

    //    @SuppressLint("SuspiciousIndentation")
    @SuppressLint("SuspiciousIndentation")
    fun onTapAnime(id: Int) {
        viewModelScope.launch {

            val response = animeRepository.getDetailsFromAnime(id)

            try {
                if (response.isSuccessful) {
                    response.body()?.data.let { data ->
                        _animeDetails.value = data
                    }
                }
            } catch (e: java.lang.NullPointerException) {
                Log.e("DetailScreenViewModel", e.message.toString())
            }

        }
    }
}


//    val data = object {
//        fun getEmptyData(): Data {
//            return Data(approved = false, background = "nothing", broadcast = Broadcast("0","0", "0", "0"), demographics = emptyList(), dura)
//        }
//    }
//}

