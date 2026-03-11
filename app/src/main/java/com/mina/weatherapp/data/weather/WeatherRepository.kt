package com.mina.weatherapp.data.weather

import com.mina.weatherapp.data.weather.datasource.local.entity.FavoriteLocationEntity
import com.mina.weatherapp.data.weather.datasource.local.FavoritesLocalDataSource
import com.mina.weatherapp.data.weather.datasource.remote.WeatherRemoteDataSource
import com.mina.weatherapp.data.weather.model.home.HomeWeatherResponse
import com.mina.weatherapp.data.weather.model.home.forecast.ForecastResponse
import com.mina.weatherapp.data.weather.model.home.onecall.OneCallResponse
import com.mina.weatherapp.presentation.home.HomeUiState
import com.mina.weatherapp.utils.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: FavoritesLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {


    fun getHomeWeather(
        lat: Double,
        lon: Double,
        appid: String,
        units: String = Constants.UNITS_METRIC,
        lang: String = Constants.ENGLISH_LANGUAGE
    ): Flow<HomeUiState> = flow {
        emit(HomeUiState.Loading)

        try {
            val result = coroutineScope {
                val oneCallDeferred = async {
                    weatherRemoteDataSource.getOneCallWeather(lat, lon, appid, units, lang)
                }

                val forecastDeferred = async {
                    weatherRemoteDataSource.getFiveDaysForecast(lat, lon, appid, units, lang)
                }

                val oneCallResult = oneCallDeferred.await()
                val forecastResult = forecastDeferred.await()

                if (oneCallResult.isSuccess && forecastResult.isSuccess) {
                    lateinit var oneCallResponse : OneCallResponse
                    lateinit var forecastResponse : ForecastResponse
                    oneCallResult.onSuccess {
                        oneCallResponse = it
                    }
                    forecastResult.onSuccess {
                        forecastResponse = it
                    }
                    Result.success(HomeWeatherResponse(oneCallResponse, forecastResponse)
                    )
                } else {
                    oneCallResult.onFailure {  }
                    Result.failure(
                        oneCallResult.exceptionOrNull()
                            ?: forecastResult.exceptionOrNull()
                            ?: Exception("Unknown error")
                    )
                }
            }

            result
                .onSuccess { emit(HomeUiState.Success(it)) }
                .onFailure { emit(HomeUiState.Error(it.message ?: "Unknown error")) }

        } catch (e: Exception) {
            emit(HomeUiState.Error(e.message ?: "Unknown error"))
        }
    }

    fun getAllFavoriteLocations(): Flow<List<FavoriteLocationEntity>> {
        return weatherLocalDataSource.getAllFavoriteLocations()
    }

    suspend fun insertFavoriteLocation(location: FavoriteLocationEntity): Long {
        return weatherLocalDataSource.insertFavoriteLocation(location)
    }

    suspend fun deleteFavoriteLocation(location: FavoriteLocationEntity) {
        weatherLocalDataSource.deleteFavoriteLocation(location)
    }

    suspend fun getFavoriteByCoordinates(lat: Double, lon: Double): FavoriteLocationEntity? {
        return weatherLocalDataSource.getFavoriteByCoordinates(lat, lon)
    }

}