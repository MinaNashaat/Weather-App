package com.mina.weatherapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mina.weatherapp.data.settings.model.LocationSource
import com.mina.weatherapp.data.settings.SettingsRepository
import com.mina.weatherapp.data.weather.WeatherRepository
import com.mina.weatherapp.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private var lastGpsLat: Double? = null
    private var lastGpsLon: Double? = null

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect {
                refresh()
            }
        }
    }

    fun updateGpsLocation(lat: Double, lon: Double) {
        lastGpsLat = lat
        lastGpsLon = lon
        refresh()
    }

    fun refresh() {
        val settingsPrefrences = settingsRepository.settings.value

        val (lat, lon) = when (settingsPrefrences.locationSource) {
            LocationSource.GPS -> {
                val lat0 = lastGpsLat ?: return
                val lon0 = lastGpsLon ?: return
                lat0 to lon0
            }
            LocationSource.MAP -> {
                val lat0 = settingsPrefrences.homeLat ?: return
                val lon0 = settingsPrefrences.homeLon ?: return
                lat0 to lon0
            }
        }

        viewModelScope.launch {
            weatherRepository.getHomeWeather(lat, lon, Constants.API_KEY).collect {
                _uiState.value = it
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class WeatherViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(
            weatherRepository = weatherRepository,
            settingsRepository = settingsRepository
        ) as T
    }
}