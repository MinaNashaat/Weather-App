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

class FavoriteLocationDaoTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private lateinit var database: WeatherDatabase
    private lateinit var dao: FavoriteLocationDao

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            WeatherDatabase::class.java
        ).build()

        dao = database.favoriteLocationDao()
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun insert_and_getByCoordinates() {
        runBlocking {
            val location = FavoriteLocationEntity(
                "Cairo",
                30.0,
                31.0
            )

            dao.insertFavoriteLocation(location)

            val result = dao.getFavoriteByCoordinates(30.0, 31.0)

            assertThat(result, `is`(location))
        }
    }

    @Test
    fun delete_location_removes_it()  {
        runBlocking {

            val location = FavoriteLocationEntity("Cairo",30.0, 31.0)
            dao.insertFavoriteLocation(location)

            dao.deleteFavoriteLocation(location)

            val result = dao.getFavoriteByCoordinates(30.0, 31.0)

            assertThat(result, `is`(nullValue()))
        }
    }

    @Test
    fun getAll_returnsInsertedLocations() {
        runBlocking {

            val loc1 = FavoriteLocationEntity("Cairo",30.0, 31.0 )
            val loc2 = FavoriteLocationEntity("Giza",29.0, 30.0 )

            dao.insertFavoriteLocation(loc1)
            dao.insertFavoriteLocation(loc2)

            val result = dao.getAllFavoriteLocations().first()

            assertThat(result.size, `is`(2))
            assertThat(result.contains(loc1), `is`(true))
            assertThat(result.contains(loc2), `is`(true))
        }

    }
}