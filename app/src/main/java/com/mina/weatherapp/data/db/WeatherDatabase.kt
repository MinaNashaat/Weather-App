package com.mina.weatherapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
//import com.mina.weatherapp.data.alerts.local.AlertConverters
import com.mina.weatherapp.data.alerts.local.AlertDao
import com.mina.weatherapp.data.alerts.local.entity.AlertEntity
import com.mina.weatherapp.data.weather.datasource.local.entity.FavoriteLocationEntity
import com.mina.weatherapp.data.weather.datasource.local.FavoriteLocationDao

//@TypeConverters(AlertConverters::class)
@Database(
    entities = [FavoriteLocationEntity::class, AlertEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun favoriteLocationDao(): FavoriteLocationDao
    abstract fun alertDao(): AlertDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}