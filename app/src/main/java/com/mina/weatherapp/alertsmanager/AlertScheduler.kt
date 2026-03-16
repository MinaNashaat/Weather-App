package com.mina.weatherapp.alertsmanager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

class AlertScheduler(context: Context) {
    private val workManager = WorkManager.getInstance(context.applicationContext)

    fun schedule(alertId: Long) {
        val request = PeriodicWorkRequestBuilder<WeatherAlertWorker>(15, TimeUnit.MINUTES)
            .setInputData(workDataOf(WeatherAlertWorker.KEY_ALERT_ID to alertId))
            .build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName(alertId),
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancel(alertId: Long) {
        workManager.cancelUniqueWork(uniqueWorkName(alertId))
    }

    private fun uniqueWorkName(alertId: Long) = "weather_alert_$alertId"

}