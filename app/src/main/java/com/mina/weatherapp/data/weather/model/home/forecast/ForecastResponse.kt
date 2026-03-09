package com.mina.weatherapp.data.weather.model.home.forecast

import com.mina.weatherapp.data.weather.model.weather.Main
import com.mina.weatherapp.data.weather.model.weather.Weather

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: ForecastCity
)

data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val dt_txt: String
)

data class ForecastCity(
    val name: String
)