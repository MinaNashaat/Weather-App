package com.mina.weatherapp.data.settings

import com.mina.weatherapp.data.settings.model.LocationSource
import com.mina.weatherapp.data.settings.model.SettingsPreferences
import com.mina.weatherapp.data.settings.model.WindSpeedUnit

class FakeDataSource(
    private var settings: SettingsPreferences
) : ISettingDataSource {

    override fun saveUnits(units: String) {
        settings = settings.copy(units = units)
    }

    override fun getUnits(): String = settings.units

    override fun saveLanguage(lang: String) {
        settings = settings.copy(language = lang)
    }

    override fun getLanguage(): String = settings.language

    override fun saveLocationSource(source: String) {
        val mapped = if (source == "map") LocationSource.MAP else LocationSource.GPS
        settings = settings.copy(locationSource = mapped)
    }

    override fun getLocationSource(): String {
        return settings.locationSource.name.lowercase()
    }

    override fun saveWindSpeedUnit(unit: String) {
        val mapped = if (unit == "mph") WindSpeedUnit.MPH else WindSpeedUnit.MPS
        settings = settings.copy(windSpeedUnit = mapped)
    }

    override fun getWindSpeedUnit(): String {
        return settings.windSpeedUnit.name.lowercase()
    }

    override fun saveHomeLocation(lat: Double, lon: Double) {
        settings = settings.copy(homeLat = lat, homeLon = lon)
    }

    override fun getHomeLat(): Double? = settings.homeLat
    override fun getHomeLon(): Double? = settings.homeLon
}