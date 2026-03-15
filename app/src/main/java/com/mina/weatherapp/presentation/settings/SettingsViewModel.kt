package com.mina.weatherapp.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mina.weatherapp.data.settings.model.LocationSource
import com.mina.weatherapp.data.settings.SettingsRepository
import com.mina.weatherapp.data.settings.model.SettingsPreferences
import com.mina.weatherapp.data.settings.model.WindSpeedUnit
import com.mina.weatherapp.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettingsPrefrences()
    }

    fun loadSettingsPrefrences(){
        viewModelScope.launch {
            repository.settings.collect { prefs ->
                _uiState.value = SettingsUiState.Success(
                    SettingsPreferences(
                        units = prefs.units,
                        language = prefs.language,
                        locationSource = prefs.locationSource,
                        windSpeedUnit = prefs.windSpeedUnit,
                        homeLat = prefs.homeLat,
                        homeLon = prefs.homeLon
                    )
                )
            }
        }
    }

    fun updateUnits(units: String) {
        val wind = when (units) {
            Constants.UNITS_IMPERIAL -> WindSpeedUnit.MPH
            else -> WindSpeedUnit.MPS
        }
        repository.saveUnitsAndWind(units, wind)
    }

    fun updateLanguage(lang: String) {
        repository.saveLanguage(lang)
    }

    fun updateLocationSource(source: LocationSource) {
        repository.saveLocationSource(source)
    }

    fun updateWindSpeedUnit(unit: WindSpeedUnit) {
        val units = when (unit) {
            WindSpeedUnit.MPH -> Constants.UNITS_IMPERIAL
            WindSpeedUnit.MPS -> Constants.UNITS_METRIC
        }
        repository.saveUnitsAndWind(units, unit)
    }

    fun updateHomeLocation(lat: Double, lon: Double) {
        repository.saveHomeLocation(lat, lon)
    }
}

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(
    private val repository: SettingsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(repository) as T
    }
}