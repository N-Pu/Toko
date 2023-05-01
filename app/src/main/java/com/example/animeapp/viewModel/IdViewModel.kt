package com.example.animeapp.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class IdViewModel : ViewModel() {
    //    private val _mal_Id = MutableStateFlow("")
//    val mal_id = _mal_Id.asStateFlow()
//
//
    private val _mal_id = MutableStateFlow(0)
    val mal_id: StateFlow<Int> = _mal_id

    fun setId(id: Int) {
        _mal_id.value = id
    }

    fun getId() =
        _mal_id.value


}