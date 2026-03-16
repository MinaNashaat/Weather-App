package com.mina.weatherapp

import android.content.Context
import com.mina.weatherapp.alertsmanager.AlertScheduler
import com.mina.weatherapp.data.alerts.local.AlertsLocalDataSource
import com.mina.weatherapp.data.alerts.AlertsRepository
import com.mina.weatherapp.data.db.WeatherDatabase
import com.mina.weatherapp.data.settings.SettingsSharedPrefrencesDataSource
import com.mina.weatherapp.data.settings.SettingsRepository
import com.mina.weatherapp.data.weather.WeatherRepository
import com.mina.weatherapp.data.weather.datasource.local.FavoritesLocalDataSource
import com.mina.weatherapp.data.weather.datasource.remote.WeatherRemoteDataSource

class AppContainer(context: Context) {

    private val db = WeatherDatabase.getInstance(context)

    val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(SettingsSharedPrefrencesDataSource(context))
    }

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepository(
            weatherRemoteDataSource = WeatherRemoteDataSource(),
            weatherLocalDataSource = FavoritesLocalDataSource(db.favoriteLocationDao()),
            settingsRepository = settingsRepository
        )
    }

    val alertsRepository: AlertsRepository by lazy {
        AlertsRepository(
            local = AlertsLocalDataSource(db.alertDao()),
            scheduler = AlertScheduler(context)
        )
    }
}