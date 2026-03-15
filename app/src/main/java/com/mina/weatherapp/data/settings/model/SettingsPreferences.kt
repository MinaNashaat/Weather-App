package com.mina.weatherapp.data.settings.model

data class SettingsPreferences(
    val locationSource: LocationSource = LocationSource.GPS,
    val units: String = "standard",
    val language: String = "en",
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.MPS,
    val homeLat: Double? = null,
    val homeLon: Double? = null
)

enum class LocationSource {
    GPS,
    MAP
}
enum class WindSpeedUnit { MPS, MPH }