package com.mina.weatherapp.data.settings

import android.content.Context
import android.content.SharedPreferences

class SettingsSharedPrefrencesDataSource(context: Context) : ISettingDataSource {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    override fun saveUnits(units: String) = prefs.edit().putString("units", units).apply()
    override fun getUnits(): String = prefs.getString("units", "standard") ?: "standard"

    override fun saveLanguage(lang: String) = prefs.edit().putString("lang", lang).apply()
    override fun getLanguage(): String = prefs.getString("lang", "en") ?: "en"

    override fun saveLocationSource(source: String) = prefs.edit().putString("location_source", source).apply()
    override fun getLocationSource(): String = prefs.getString("location_source", "gps") ?: "gps"

    override fun saveWindSpeedUnit(unit: String) = prefs.edit().putString("wind_unit", unit).apply()
    override fun getWindSpeedUnit(): String = prefs.getString("wind_unit", "mps") ?: "mps"

    override fun saveHomeLocation(lat: Double, lon: Double) {
        prefs.edit()
            .putString("home_lat", lat.toString())
            .putString("home_lon", lon.toString())
            .apply()
    }

    override fun getHomeLat(): Double? = prefs.getString("home_lat", null)?.toDoubleOrNull()
    override fun getHomeLon(): Double? = prefs.getString("home_lon", null)?.toDoubleOrNull()
}