package com.mina.weatherapp.data.settings

import com.mina.weatherapp.data.settings.model.LocationSource
import com.mina.weatherapp.data.settings.model.SettingsPreferences
import com.mina.weatherapp.data.settings.model.WindSpeedUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository(
    private val sharedPrefrenceDataSource: ISettingDataSource
) : ISettingsRepository{

    private val _settings = MutableStateFlow(load())
    val settings: StateFlow<SettingsPreferences> = _settings.asStateFlow()



    override fun load(): SettingsPreferences {

        val source = if (sharedPrefrenceDataSource.getLocationSource().lowercase() == "map")
            LocationSource.MAP
        else
            LocationSource.GPS

        val wind = if (sharedPrefrenceDataSource.getWindSpeedUnit().lowercase() == "mph")
            WindSpeedUnit.MPH
        else
            WindSpeedUnit.MPS

        return SettingsPreferences(
            locationSource = source,
            units = sharedPrefrenceDataSource.getUnits(),
            language = sharedPrefrenceDataSource.getLanguage(),
            windSpeedUnit = wind,
            homeLat = sharedPrefrenceDataSource.getHomeLat(),
            homeLon = sharedPrefrenceDataSource.getHomeLon()
        )
    }

    override fun saveLanguage(lang: String) {
        sharedPrefrenceDataSource.saveLanguage(lang);
        _settings.value = _settings.value.copy(language = lang)
    }

    override fun saveLocationSource(source: LocationSource) {
        sharedPrefrenceDataSource.saveLocationSource(source.name.lowercase())
        _settings.value = _settings.value.copy(locationSource = source)
    }

    override fun saveHomeLocation(lat: Double, lon: Double) {
        sharedPrefrenceDataSource.saveHomeLocation(lat, lon)
        _settings.value = _settings.value.copy(homeLat = lat, homeLon = lon)
    }

    override fun saveUnitsAndWind(units: String, wind: WindSpeedUnit) {
        sharedPrefrenceDataSource.saveUnits(units)
        if (wind == WindSpeedUnit.MPH)
            sharedPrefrenceDataSource.saveWindSpeedUnit("mph")
        else
            sharedPrefrenceDataSource.saveWindSpeedUnit("mps")
        _settings.value = _settings.value.copy(
            units = units,
            windSpeedUnit = wind
        )
    }

}