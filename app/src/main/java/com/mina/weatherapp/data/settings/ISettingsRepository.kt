package com.mina.weatherapp.data.settings

import com.mina.weatherapp.data.settings.model.LocationSource
import com.mina.weatherapp.data.settings.model.SettingsPreferences
import com.mina.weatherapp.data.settings.model.WindSpeedUnit

interface ISettingsRepository {
    fun load(): SettingsPreferences
    fun saveLanguage(lang: String)
    fun saveLocationSource(source: LocationSource)
    fun saveHomeLocation(lat: Double, lon: Double)
    fun saveUnitsAndWind(units: String, wind: WindSpeedUnit)

}