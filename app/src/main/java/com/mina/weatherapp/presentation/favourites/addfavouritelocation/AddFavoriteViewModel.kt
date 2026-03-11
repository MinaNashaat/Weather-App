package com.mina.weatherapp.presentation.favourites.addfavouritelocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mina.weatherapp.data.weather.datasource.local.entity.FavoriteLocationEntity
import com.mina.weatherapp.data.weather.WeatherRepository
import com.mina.weatherapp.presentation.favourites.addfavouritelocation.SelectedLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AddFavoriteUiState {
    object Idle : AddFavoriteUiState()
    object Saving : AddFavoriteUiState()
    object Success : AddFavoriteUiState()
    data class Error(val message: String) : AddFavoriteUiState()
}

class AddFavoriteViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddFavoriteUiState>(AddFavoriteUiState.Idle)
    val uiState: StateFlow<AddFavoriteUiState> = _uiState.asStateFlow()

    private val _selectedLocation = MutableStateFlow<SelectedLocation?>(null)
    val selectedLocation: StateFlow<SelectedLocation?> = _selectedLocation.asStateFlow()

    fun setSelectedLocation(lat: Double, lon: Double) {
        _selectedLocation.value = SelectedLocation(lat, lon)
    }

    fun resetScreen() {
        _uiState.value = AddFavoriteUiState.Idle
        _selectedLocation.value = null
    }

    fun saveFavorite(cityName: String) {
        val location = _selectedLocation.value
        if (location == null) {
            _uiState.value = AddFavoriteUiState.Error("Please choose a location on the map")
            return
        }

        if (cityName.isBlank()) {
            _uiState.value = AddFavoriteUiState.Error("Please enter a city name")
            return
        }

        viewModelScope.launch {
            _uiState.value = AddFavoriteUiState.Saving

            try {
                repository.insertFavoriteLocation(
                    FavoriteLocationEntity(
                        cityName = cityName,
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                )
                _uiState.value = AddFavoriteUiState.Success
            } catch (e: Exception) {
                _uiState.value = AddFavoriteUiState.Error(
                    e.message ?: "Failed to save location"
                )
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class AddFavoriteViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddFavoriteViewModel(repository) as T
    }
}