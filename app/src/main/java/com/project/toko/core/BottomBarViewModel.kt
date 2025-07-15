package com.project.toko.core

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor() : ViewModel() {

    private val _bottomBarVisibility = MutableStateFlow(true)
    val bottomBarVisibility: StateFlow<Boolean> = _bottomBarVisibility

    fun showBottomBar(
    ) {
        _bottomBarVisibility.value = true
    }

    fun hideBottomBar(
    ) {
        _bottomBarVisibility.value = false
    }

}
