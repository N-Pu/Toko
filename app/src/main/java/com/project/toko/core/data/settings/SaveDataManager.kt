package com.project.toko.core.data.settings

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveDarkModeManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    private val _isDarkThemeActive = mutableStateOf(loadData())
    val isDarkThemeActive get() = _isDarkThemeActive

    fun toggleTheme() {
        val newValue = !_isDarkThemeActive.value
        _isDarkThemeActive.value = newValue
        saveData(newValue)
    }

    private fun saveData(isInDarkMode: Boolean) {
        sharedPreferences.edit {
            putBoolean(TAG2, isInDarkMode)
        }
    }

    private fun loadData(): Boolean {
        return sharedPreferences.getBoolean(TAG2, false)
    }

    companion object {
        private const val TAG2 = "DARK_MODE"
    }
}
