package com.mina.weatherapp.presentation.favourites.favouriteslocations

import com.mina.weatherapp.data.weather.datasource.local.entity.FavoriteLocationEntity

sealed class FavoritesUiState {
    object Loading : FavoritesUiState()
    data class Success(val data: List<FavoriteLocationEntity>) : FavoritesUiState()
    data class Error(val message: String) : FavoritesUiState()
}