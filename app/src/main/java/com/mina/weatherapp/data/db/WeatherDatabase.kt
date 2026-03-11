package com.mina.weatherapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mina.weatherapp.data.weather.datasource.local.entity.FavoriteLocationEntity
import com.mina.weatherapp.data.weather.datasource.local.FavoriteLocationDao

@Database(
    entities = [FavoriteLocationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun favoriteLocationDao(): FavoriteLocationDao

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