package com.mina.weatherapp.presentation.favourites.favouriteslocations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mina.weatherapp.data.weather.datasource.local.entity.FavoriteLocationEntity
import com.mina.weatherapp.data.weather.WeatherRepository
import com.mina.weatherapp.presentation.favourites.addfavouritelocation.AddFavoriteViewModel
import com.mina.weatherapp.presentation.favourites.favouritesdetails.FavoriteDetailsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesRepository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = FavoritesUiState.Loading
        getAllFavorites()
    }

    private fun getAllFavorites() {
        viewModelScope.launch {
            favoritesRepository.getAllFavoriteLocations().collect { locations ->
                _uiState.value = FavoritesUiState.Success(locations)
            }
        }
    }

    fun deleteFavorite(location: FavoriteLocationEntity) {
        viewModelScope.launch {
            favoritesRepository.deleteFavoriteLocation(location)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class FavoritesViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoritesViewModel(repository) as T
    }
}
