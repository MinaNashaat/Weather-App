package com.mina.weatherapp

import android.app.Application
import org.osmdroid.config.Configuration

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("osmdroid_pref", MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = packageName
    }
}