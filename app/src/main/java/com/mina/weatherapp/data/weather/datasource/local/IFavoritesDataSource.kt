package com.mina.weatherapp.data.weather.datasource.local

import com.mina.weatherapp.data.weather.datasource.local.entity.FavoriteLocationEntity
import kotlinx.coroutines.flow.Flow

interface IFavoritesDataSource {

    fun getAllFavoriteLocations(): Flow<List<FavoriteLocationEntity>>
    suspend fun insertFavoriteLocation(location: FavoriteLocationEntity): Long
    suspend fun deleteFavoriteLocation(location: FavoriteLocationEntity)
    suspend fun getFavoriteByCoordinates(lat: Double, lon:Double): FavoriteLocationEntity?

}