package com.mina.weatherapp.data.settings

interface ISettingDataSource {
    fun saveUnits(units: String)
    fun getUnits(): String
    fun saveLanguage(lang: String)
    fun getLanguage(): String
    fun saveLocationSource(source: String)
    fun getLocationSource(): String
    fun saveWindSpeedUnit(unit: String)
    fun getWindSpeedUnit(): String
    fun saveHomeLocation(lat: Double, lon: Double)
    fun getHomeLat(): Double?
    fun getHomeLon(): Double?
}