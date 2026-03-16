package com.mina.weatherapp.data.alerts.local

import com.mina.weatherapp.data.alerts.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

class AlertsLocalDataSource(
    private val dao: AlertDao
) {
    fun getAll(): Flow<List<AlertEntity>> = dao.getAll()

    suspend fun insert(alert: AlertEntity): Long = dao.insert(alert)

    suspend fun update(alert: AlertEntity) = dao.update(alert)

    suspend fun delete(alert: AlertEntity) = dao.delete(alert)

    suspend fun getById(id: Long): AlertEntity? = dao.getById(id)

    suspend fun disable(id: Long) = dao.disable(id)

    suspend fun setEnabled(id: Long, enabled: Boolean) = dao.setEnabled(id, enabled)
}