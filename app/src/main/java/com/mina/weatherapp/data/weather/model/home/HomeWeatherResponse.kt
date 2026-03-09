package com.mina.weatherapp.data.weather.model.home

import com.mina.weatherapp.data.weather.model.home.forecast.ForecastResponse
import com.mina.weatherapp.data.weather.model.home.onecall.OneCallResponse

data class HomeWeatherResponse(
    val oneCallResponse: OneCallResponse,
    val forecastResponse: ForecastResponse
)