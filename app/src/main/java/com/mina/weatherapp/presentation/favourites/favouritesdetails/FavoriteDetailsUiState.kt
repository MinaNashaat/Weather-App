package com.mina.weatherapp.presentation.favourites.favouritesdetails

import com.mina.weatherapp.data.weather.model.home.HomeWeatherResponse

sealed class FavoriteDetailsUiState {
    object Loading : FavoriteDetailsUiState()
    data class Success(val data: HomeWeatherResponse) : FavoriteDetailsUiState()
    data class Error(val message: String) : FavoriteDetailsUiState()
}