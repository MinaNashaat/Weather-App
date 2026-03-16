package com.mina.weatherapp.presentation.favourites.favouritesdetails

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mina.weatherapp.presentation.home.HomeUiState
import com.mina.weatherapp.presentation.home.WeatherForecastScreen

@Composable
fun FavoriteDetailsScreen(uiState: FavoriteDetailsUiState) {
    when (uiState) {
        is FavoriteDetailsUiState.Loading -> {
            Log.d("minanashaat" , "FavoriteDetailsScreen : Loading")
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is FavoriteDetailsUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(uiState.message)
            }
        }

        is FavoriteDetailsUiState.Success -> {
            Log.d("minanashaat" , "FavoriteDetailsScreen : Success")
            WeatherForecastScreen(
                uiState = HomeUiState.Success(uiState.data)
            )
        }
    }
}