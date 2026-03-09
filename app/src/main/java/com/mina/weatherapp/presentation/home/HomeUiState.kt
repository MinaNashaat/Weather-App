package com.mina.weatherapp.presentation.home

import com.mina.weatherapp.data.weather.model.home.HomeWeatherResponse

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: HomeWeatherResponse) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}