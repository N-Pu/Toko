package com.project.toko.core.data.settings.nsfw

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SafeForWorkManager @Inject constructor(
    @Named("sfw") private val sharedPreferences: SharedPreferences,
) {
    private val _isSFWActive = MutableStateFlow(loadData())
    val isNSFWActive = _isSFWActive
        .asStateFlow()

    fun toggleSFW() {
        _isSFWActive.update { !_isSFWActive.value }
        saveData(_isSFWActive.value)
    }

    private fun saveData(isInDarkMode: Boolean) = sharedPreferences.edit { putBoolean(TAG, isInDarkMode) }

    private fun loadData(): Boolean = sharedPreferences.getBoolean(TAG, true)

    companion object {
        private const val TAG = "SFW_MODE"
    }
}