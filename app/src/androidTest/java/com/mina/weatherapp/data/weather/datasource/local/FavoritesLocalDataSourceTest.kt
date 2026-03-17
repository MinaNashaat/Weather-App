package com.mina.weatherapp.data.weather.datasource.local

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mina.weatherapp.data.db.WeatherDatabase
import com.mina.weatherapp.data.weather.datasource.local.entity.FavoriteLocationEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*

class FavoritesLocalDataSourceTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private lateinit var database: WeatherDatabase
    private lateinit var dao: FavoriteLocationDao
    private lateinit var dataSource: FavoritesLocalDataSource

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            WeatherDatabase::class.java
        ).build()

        dao = database.favoriteLocationDao()
        dataSource = FavoritesLocalDataSource(dao)
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun insert_and_getByCoordinates() {
        runBlocking {

            val location = FavoriteLocationEntity("Cairo",30.0, 31.0)

            dataSource.insertFavoriteLocation(location)
            val result = dataSource.getFavoriteByCoordinates(30.0, 31.0)

            assertThat(result, `is`(location))
        }
    }

    @Test
    fun delete_location() {
        runBlocking {

            val location = FavoriteLocationEntity("Cairo",30.0, 31.0 )
            dataSource.insertFavoriteLocation(location)

            dataSource.deleteFavoriteLocation(location)
            val result = dataSource.getFavoriteByCoordinates(30.0, 31.0)

            assertThat(result, `is`(nullValue()))
        }
    }

    @Test
    fun getAll_returnsData() {
        runBlocking {

            val loc1 = FavoriteLocationEntity("Cairo",30.0, 31.0 )
            val loc2 = FavoriteLocationEntity("Giza",29.0, 30.0 )

            dataSource.insertFavoriteLocation(loc1)
            dataSource.insertFavoriteLocation(loc2)

            val result = dataSource.getAllFavoriteLocations().first()

            assertThat(result.size, `is`(2))
        }

    }
}