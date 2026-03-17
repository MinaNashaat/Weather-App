package com.mina.weatherapp.data.weather.datasource.local

import com.mina.weatherapp.data.weather.datasource.local.entity.FavoriteLocationEntity
import kotlinx.coroutines.flow.Flow

class FavoritesLocalDataSource(
    private val favoriteLocationDao: FavoriteLocationDao
) : IFavoritesDataSource {
    override fun getAllFavoriteLocations(): Flow<List<FavoriteLocationEntity>> {
        return favoriteLocationDao.getAllFavoriteLocations()
    }

    override suspend fun insertFavoriteLocation(location: FavoriteLocationEntity): Long {
        return favoriteLocationDao.insertFavoriteLocation(location)
    }

    override suspend fun deleteFavoriteLocation(location: FavoriteLocationEntity) {
        favoriteLocationDao.deleteFavoriteLocation(location)
    }

    override suspend fun getFavoriteByCoordinates(lat: Double, lon: Double): FavoriteLocationEntity? {
        return favoriteLocationDao.getFavoriteByCoordinates(lat, lon)
    }
}