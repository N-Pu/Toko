package com.project.toko.core

import android.view.View
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

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
