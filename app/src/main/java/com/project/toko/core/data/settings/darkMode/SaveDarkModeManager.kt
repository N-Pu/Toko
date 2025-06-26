package com.project.toko.core.data.settings.darkMode

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SaveDarkModeManager @Inject constructor(
    @Named("dark_mode")  private val sharedPreferences: SharedPreferences
) {
    private val _isDarkThemeActive = MutableStateFlow(loadData())
    val isDarkThemeActive = _isDarkThemeActive.asStateFlow()

    fun toggleTheme() {
        val newValue = !_isDarkThemeActive.value
        _isDarkThemeActive.value = newValue
        saveData(newValue)
    }

    private fun saveData(isInDarkMode: Boolean) {
        sharedPreferences.edit {
            putBoolean(TAG, isInDarkMode)
        }
    }

    private fun loadData(): Boolean = sharedPreferences.getBoolean(TAG, false)

    companion object {
        private const val TAG = "Dark Mode"
    }
}
