package com.mina.weatherapp.alertsmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.mina.weatherapp.R
import com.mina.weatherapp.alertsmanager.WeatherAlertWorker.Companion.KEY_ALERT_ID

class AlertNotifier(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val silentChannelId = "weather_alerts_silent"
    private val alarmChannelId = "weather_alerts_alarm"

    @RequiresApi(Build.VERSION_CODES.O)
    fun show(alertId: Long, title: String, message: String, alarmSound: Boolean) {
        ensureChannels()

        val disableIntent = Intent(context, DisableAlertReceiver::class.java).apply {
            putExtra(KEY_ALERT_ID, alertId)
        }

        val disablePending = PendingIntent.getBroadcast(
            context,
            alertId.toInt(),
            disableIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = if (alarmSound) alarmChannelId else silentChannelId

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(0, "Turn off", disablePending)
            .build()

        notificationManager.notify(alertId.toInt(), notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun ensureChannels() {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                silentChannelId,
                "Weather Alerts (Notification)",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val alarmChannel = NotificationChannel(
            alarmChannelId,
            "Weather Alerts (Alarm)",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(alarmSound, attrs)
            enableVibration(true)
        }

        notificationManager.createNotificationChannel(alarmChannel)
    }
}