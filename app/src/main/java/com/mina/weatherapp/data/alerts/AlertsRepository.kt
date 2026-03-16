package com.mina.weatherapp.data.alerts

import com.mina.weatherapp.alertsmanager.AlertScheduler
import com.mina.weatherapp.data.alerts.local.AlertsLocalDataSource
import com.mina.weatherapp.data.alerts.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

class AlertsRepository(
    private val local: AlertsLocalDataSource,
    private val scheduler: AlertScheduler
) {
    fun getAllAlerts(): Flow<List<AlertEntity>> = local.getAll()

    suspend fun addAlert(alert: AlertEntity): Long {
        val id = local.insert(alert)
        scheduler.schedule(id)
        return id
    }

    suspend fun deleteAlert(alert: AlertEntity) {
        scheduler.cancel(alert.id)
        local.delete(alert)
    }

    suspend fun setEnabled(alert: AlertEntity, enabled: Boolean) {
        local.setEnabled(alert.id, enabled)
        if (enabled)
            scheduler.schedule(alert.id)
        else
            scheduler.cancel(alert.id)
    }

    suspend fun disable(alertId: Long) {
        scheduler.cancel(alertId)
        local.disable(alertId)
    }

    suspend fun getById(id: Long): AlertEntity? = local.getById(id)
}