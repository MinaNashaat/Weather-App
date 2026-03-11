package com.mina.weatherapp.presentation.favourites.favouritesdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mina.weatherapp.data.weather.WeatherRepository
import com.mina.weatherapp.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.mina.weatherapp.presentation.home.*


class FavoriteDetailsViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoriteDetailsUiState>(FavoriteDetailsUiState.Loading)
    val uiState: StateFlow<FavoriteDetailsUiState> = _uiState.asStateFlow()

    fun loadFavoriteWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _uiState.value = FavoriteDetailsUiState.Loading

            val favorite = weatherRepository.getFavoriteByCoordinates(lat, lon)
            if (favorite == null) {
                _uiState.value = FavoriteDetailsUiState.Error("Favorite location not found")
                return@launch
            }

            weatherRepository.getHomeWeather(
                lat = favorite.latitude,
                lon = favorite.longitude,
                appid = Constants.API_KEY
            ).collect { result ->
                when (result) {
                    is HomeUiState.Loading -> {
                        _uiState.value = FavoriteDetailsUiState.Loading
                    }
                    is HomeUiState.Success -> {
                        _uiState.value = FavoriteDetailsUiState.Success(result.data)
                    }
                    is HomeUiState.Error -> {
                        _uiState.value = FavoriteDetailsUiState.Error(result.message)
                    }
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class FavoriteDetailsViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteDetailsViewModel(repository) as T
    }
}