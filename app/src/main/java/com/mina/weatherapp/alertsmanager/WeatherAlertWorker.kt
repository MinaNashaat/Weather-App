package com.mina.weatherapp.alertsmanager

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mina.weatherapp.data.alerts.local.entity.AlertActionType
import com.mina.weatherapp.data.alerts.local.entity.AlertConditionType
import com.mina.weatherapp.data.db.WeatherDatabase
import com.mina.weatherapp.data.network.Network
import com.mina.weatherapp.utils.Constants

class WeatherAlertWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    companion object {
        const val KEY_ALERT_ID = "alert_id"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val alertId = inputData.getLong(KEY_ALERT_ID, -1L)
        if (alertId == -1L) return Result.failure()

        val db = WeatherDatabase.getInstance(applicationContext)
        val alertDao = db.alertDao()

        val alert = alertDao.getById(alertId) ?: return Result.success()
        if (!alert.enabled) return Result.success()

        val now = System.currentTimeMillis()

        if (now < alert.startTimeMillis || now > alert.endTimeMillis) {
            alertDao.disable(alertId)
            AlertScheduler(applicationContext).cancel(alertId)
            return Result.success()
        }

        val prefs = applicationContext.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val units = prefs.getString("units", Constants.UNITS_METRIC) ?: Constants.UNITS_METRIC
        val lang = prefs.getString("lang", Constants.ENGLISH_LANGUAGE) ?: Constants.ENGLISH_LANGUAGE

        val response = Network.weatherService.getOneCallWeather(
            lat = alert.latitude,
            lon = alert.longitude,
            appid = Constants.API_KEY,
            units = units,
            lang = lang
        )

        if (!response.isSuccessful) return Result.retry()
        val body = response.body() ?: return Result.retry()

        val matched = when (alert.conditionType) {
            AlertConditionType.RAIN -> {

                val h0 = body.hourly.firstOrNull()
                (h0?.pop ?: 0.0) >= 0.3 || h0?.rain != null
            }
            AlertConditionType.WIND -> {
                val th = alert.threshold ?: 10.0
                body.current.wind_speed >= th
            }
            AlertConditionType.TEMP_HIGH -> {
                val th = alert.threshold ?: 35.0
                body.current.temp >= th
            }
            AlertConditionType.TEMP_LOW -> {
                val th = alert.threshold ?: 5.0
                body.current.temp <= th
            }
        }

        if (matched) {
            AlertNotifier(applicationContext).show(
                alertId = alert.id,
                title = alert.title,
                message = "Condition: ${alert.conditionType}",
                alarmSound = (alert.actionType == AlertActionType.ALARM)
            )
        }

        return Result.success()
    }
}