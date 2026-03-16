package com.mina.weatherapp.alertsmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mina.weatherapp.alertsmanager.WeatherAlertWorker.Companion.KEY_ALERT_ID
import com.mina.weatherapp.data.db.WeatherDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DisableAlertReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val alertId = intent.getLongExtra(KEY_ALERT_ID, -1L)
        if (alertId == -1L) return

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            AlertScheduler(context).cancel(alertId)
            val db = WeatherDatabase.getInstance(context)
            db.alertDao().disable(alertId)
            pendingResult.finish()
        }
    }
}