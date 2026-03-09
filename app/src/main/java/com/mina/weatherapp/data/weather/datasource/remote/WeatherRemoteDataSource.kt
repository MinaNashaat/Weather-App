package com.mina.weatherapp.data.weather.datasource.remote

import com.mina.weatherapp.data.network.Network
import com.mina.weatherapp.data.weather.model.home.forecast.ForecastResponse
import com.mina.weatherapp.data.weather.model.home.onecall.OneCallResponse
import com.mina.weatherapp.utils.Constants

class WeatherRemoteDataSource {
    private val weatherService: WeatherService = Network.weatherService

    suspend fun getOneCallWeather(
        lat: Double,
        lon: Double,
        appid: String,
        units: String = Constants.UNITS_METRIC,
        lang: String = Constants.ENGLISH_LANGUAGE
    ): Result<OneCallResponse> {
        return try {
            val response = weatherService.getOneCallWeather(lat, lon, appid, units, lang)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("OneCall response body is null"))
                }
            } else {
                Result.failure(Exception("OneCall error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFiveDaysForecast(
        lat: Double,
        lon: Double,
        appid: String,
        units: String = Constants.UNITS_METRIC,
        lang: String = Constants.ENGLISH_LANGUAGE
    ): Result<ForecastResponse> {
        return try {
            val response = weatherService.getFiveDaysForecast(lat, lon, appid, units, lang)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Forecast response body is null"))
                }
            } else {
                Result.failure(Exception("Forecast error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}