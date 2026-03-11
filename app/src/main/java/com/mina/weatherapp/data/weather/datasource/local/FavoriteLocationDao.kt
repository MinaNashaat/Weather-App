package com.mina.weatherapp.data.weather.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mina.weatherapp.data.weather.datasource.local.entity.FavoriteLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteLocation(location: FavoriteLocationEntity): Long

    @Delete
    suspend fun deleteFavoriteLocation(location: FavoriteLocationEntity)

    @Query("SELECT * FROM favorite_locations")
    fun getAllFavoriteLocations(): Flow<List<FavoriteLocationEntity>>

    @Query("""SELECT * FROM favorite_locations WHERE latitude = :lat AND longitude = :lon LIMIT 1""")
    suspend fun getFavoriteByCoordinates(lat: Double, lon: Double): FavoriteLocationEntity?
}