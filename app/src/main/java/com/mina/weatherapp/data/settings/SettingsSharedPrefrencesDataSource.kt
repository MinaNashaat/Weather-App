package com.mina.weatherapp.data.settings

import android.content.Context
import android.content.SharedPreferences

class SettingsSharedPrefrencesDataSource(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    fun saveUnits(units: String) = prefs.edit().putString("units", units).apply()
    fun getUnits(): String = prefs.getString("units", "standard") ?: "standard"

    fun saveLanguage(lang: String) = prefs.edit().putString("lang", lang).apply()
    fun getLanguage(): String = prefs.getString("lang", "en") ?: "en"

    fun saveLocationSource(source: String) = prefs.edit().putString("location_source", source).apply()
    fun getLocationSource(): String = prefs.getString("location_source", "gps") ?: "gps"

    fun saveWindSpeedUnit(unit: String) = prefs.edit().putString("wind_unit", unit).apply()
    fun getWindSpeedUnit(): String = prefs.getString("wind_unit", "mps") ?: "mps"

    fun saveHomeLocation(lat: Double, lon: Double) {
        prefs.edit()
            .putString("home_lat", lat.toString())
            .putString("home_lon", lon.toString())
            .apply()
    }

    fun getHomeLat(): Double? = prefs.getString("home_lat", null)?.toDoubleOrNull()
    fun getHomeLon(): Double? = prefs.getString("home_lon", null)?.toDoubleOrNull()
}