package com.mina.weatherapp.data.weather.datasource.remote

import com.mina.weatherapp.data.weather.model.home.forecast.ForecastResponse
import com.mina.weatherapp.data.weather.model.home.onecall.OneCallResponse
import com.mina.weatherapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
//    @GET("weather")
//    suspend fun getWeather(
//        @Query("lat") lat: Double,
//        @Query("lon") lon: Double,
//        @Query("appid") appid: String,
//        @Query("lang") lang: String = Constants.ENGLISH_LANGUAGE
//    ): Response<WeatherResponse>


    @GET("3.0/onecall")
    suspend fun getOneCallWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") units: String = Constants.UNITS_METRIC,
        @Query("lang") lang: String = Constants.ENGLISH_LANGUAGE
    ): Response<OneCallResponse>

    @GET("2.5/forecast")
    suspend fun getFiveDaysForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") units: String = Constants.UNITS_METRIC,
        @Query("lang") lang: String = Constants.ENGLISH_LANGUAGE
    ): Response<ForecastResponse>


}