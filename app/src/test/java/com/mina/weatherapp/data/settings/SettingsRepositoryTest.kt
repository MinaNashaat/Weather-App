package com.mina.weatherapp.data.settings

import com.mina.weatherapp.data.settings.model.LocationSource
import com.mina.weatherapp.data.settings.model.SettingsPreferences
import com.mina.weatherapp.data.settings.model.WindSpeedUnit
import org.hamcrest.core.IsEqual
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class SettingsRepositoryTest {
    private lateinit var fakeDataSource: FakeDataSource
    private lateinit var repository: SettingsRepository

    @Before
    fun setup() {

        fakeDataSource = FakeDataSource(
            SettingsPreferences(
                locationSource = LocationSource.MAP,
                units = "metric",
                language = "en",
                windSpeedUnit = WindSpeedUnit.MPH,
                homeLat = 10.0,
                homeLon = 20.0
            )
        )
        repository = SettingsRepository(fakeDataSource)
    }

    @Test
    fun load_returnsCorrectMappedData() {
        val result = repository.load()

        assertThat(result.locationSource, IsEqual(LocationSource.MAP))
        assertThat(result.windSpeedUnit, IsEqual(WindSpeedUnit.MPH))
        assertThat(result.units, IsEqual("metric"))
        assertThat(result.language, IsEqual("en"))
    }

    @Test
    fun saveLanguage_updatesDataSource_andStateFlow() {

        repository.saveLanguage("ar")

        val result = repository.settings.value
        assertThat(result.language, IsEqual("ar"))
    }

    @Test
    fun saveLocationSource_updatesCorrectly() {

        repository.saveLocationSource(LocationSource.GPS)

        val result = repository.settings.value

        assertThat(result.locationSource, IsEqual(LocationSource.GPS))
    }

    @Test
    fun saveHomeLocation_updatesCorrectly() {

        repository.saveHomeLocation(30.0, 40.0)

        val result = repository.settings.value

        assertThat(result.homeLat, IsEqual(30.0))
        assertThat(result.homeLon, IsEqual(40.0))
    }

    @Test
    fun saveUnitsAndWind_mph_updatesCorrectly() {

        repository.saveUnitsAndWind("imperial", WindSpeedUnit.MPH)

        val result = repository.settings.value

        assertThat(result.units, IsEqual("imperial"))
        assertThat(result.windSpeedUnit, IsEqual(WindSpeedUnit.MPH))
    }

    @Test
    fun saveUnitsAndWind_mps_updatesCorrectly() {

        repository.saveUnitsAndWind("metric", WindSpeedUnit.MPS)

        val result = repository.settings.value

        assertThat(result.units, IsEqual("metric"))
        assertThat(result.windSpeedUnit, IsEqual(WindSpeedUnit.MPS))
    }

}