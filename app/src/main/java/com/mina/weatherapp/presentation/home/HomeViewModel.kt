package com.mina.weatherapp.presentation.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mina.weatherapp.data.weather.WeatherRepository
import com.mina.weatherapp.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getHomeWeather(
        lat: Double,
        lon: Double,
        appid: String,
        units: String = Constants.UNITS_METRIC,
        lang: String = Constants.ENGLISH_LANGUAGE
    ) {
        viewModelScope.launch {
            weatherRepository.getHomeWeather(lat, lon, appid, units, lang).collect {
                _uiState.value = it
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class WeatherViewModelFactory(
    private val weatherRepository: WeatherRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(weatherRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}