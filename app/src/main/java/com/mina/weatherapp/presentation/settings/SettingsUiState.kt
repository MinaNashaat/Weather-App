package com.mina.weatherapp.presentation.settings

import com.mina.weatherapp.data.settings.model.SettingsPreferences


sealed class SettingsUiState {
    object Loading : SettingsUiState()
    data class Success(val data: SettingsPreferences) : SettingsUiState()
    data class Error(val message: String) : SettingsUiState()
}