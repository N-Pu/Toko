package com.example.animeapp.domain.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


object IdViewModel : ViewModel() {

    private val _mal_id = MutableStateFlow(0)
    val mal_id: StateFlow<Int> = _mal_id

    fun setId(id: Int) {
        _mal_id.value = id
    }

    fun getId() =
        _mal_id.value


}