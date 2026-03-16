package com.mina.weatherapp.data.alerts.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_alerts")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val title: String,

    val latitude: Double,
    val longitude: Double,

    val startTimeMillis: Long,
    val endTimeMillis: Long,

    val conditionType: AlertConditionType,
    val threshold: Double? = null,

    val actionType: AlertActionType,

    val enabled: Boolean = true
)

enum class AlertConditionType {
    RAIN, WIND, TEMP_HIGH, TEMP_LOW
}

enum class AlertActionType {
    NOTIFICATION, ALARM
}