package com.mina.weatherapp.data.alerts.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mina.weatherapp.data.alerts.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Insert
    suspend fun insert(alert: AlertEntity): Long

    @Update
    suspend fun update(alert: AlertEntity)

    @Delete
    suspend fun delete(alert: AlertEntity)

    @Query("SELECT * FROM weather_alerts ORDER BY id DESC")
    fun getAll(): Flow<List<AlertEntity>>

    @Query("SELECT * FROM weather_alerts WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): AlertEntity?

    @Query("UPDATE weather_alerts SET enabled = 0 WHERE id = :id")
    suspend fun disable(id: Long)

    @Query("UPDATE weather_alerts SET enabled = :enabled WHERE id = :id")
    suspend fun setEnabled(id: Long, enabled: Boolean)
}