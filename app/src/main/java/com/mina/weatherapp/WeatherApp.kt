package com.mina.weatherapp

import android.app.Application
import org.osmdroid.config.Configuration

class WeatherApp : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(applicationContext)

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("osmdroid_pref", MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = packageName
    }
}