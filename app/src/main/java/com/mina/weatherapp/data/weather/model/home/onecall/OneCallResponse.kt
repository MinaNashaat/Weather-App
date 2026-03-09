package com.mina.weatherapp.data.weather.model.home.onecall

import com.google.gson.annotations.SerializedName
import com.mina.weatherapp.data.weather.model.weather.Weather

data class OneCallResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val current: CurrentWeather,
    val hourly: List<HourlyWeather>,
    val daily: List<DailyOneCallWeather>
)

data class CurrentWeather(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double?,
    val weather: List<Weather>
)

data class HourlyWeather(
    val dt: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double?,
    val pop: Double,
    val weather: List<Weather>,
    val rain: RainHourly? = null
)

data class RainHourly(
    @SerializedName("1h")
    val oneHour: Double
)

data class DailyOneCallWeather(
    val dt: Long
)