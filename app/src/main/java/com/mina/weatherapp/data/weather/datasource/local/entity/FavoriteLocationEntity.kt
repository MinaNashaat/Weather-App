package com.mina.weatherapp.data.weather.datasource.local.entity

import androidx.room.Entity

@Entity(
    tableName = "favorite_locations",
    primaryKeys = ["latitude", "longitude"]
)
data class FavoriteLocationEntity(
    val cityName: String,
    val latitude: Double,
    val longitude: Double
)