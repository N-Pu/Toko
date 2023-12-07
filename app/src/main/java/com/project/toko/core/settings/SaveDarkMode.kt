package com.project.toko.core.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf

class SaveDarkMode(private val context: Context) {


    private val _isDarkThemeActive = mutableStateOf(false)
    val isDarkThemeActive = _isDarkThemeActive

    fun saveData(isInDarkMode: Boolean) {
        val sharedPreferences = context.getSharedPreferences("Dark Mode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("DARK_MODE", isInDarkMode)
        editor.apply()
        _isDarkThemeActive.value = isInDarkMode

//        Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT).show()
    }

    fun loadData() {
        val sharedPreferences = context.getSharedPreferences("Dark Mode", Context.MODE_PRIVATE)
        _isDarkThemeActive.value = sharedPreferences.getBoolean("DARK_MODE", false)
    }
}

