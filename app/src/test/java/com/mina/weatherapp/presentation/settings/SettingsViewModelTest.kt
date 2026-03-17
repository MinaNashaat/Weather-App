package com.mina.weatherapp.presentation.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mina.weatherapp.data.settings.SettingsRepository
import com.mina.weatherapp.data.settings.model.LocationSource
import com.mina.weatherapp.data.settings.model.SettingsPreferences
import com.mina.weatherapp.data.settings.model.WindSpeedUnit
import com.mina.weatherapp.utils.Constants
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.verify

class SettingsViewModelTest {
    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: SettingsRepository
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk()

        coEvery  { repository.saveUnitsAndWind(any(), any()) } just runs
        coEvery  { repository.saveLanguage(any()) } just runs
        coEvery  { repository.saveLocationSource(any()) } just runs
        coEvery  { repository.saveHomeLocation(any(),any()) } just runs

        every { repository.settings } returns MutableStateFlow(
            SettingsPreferences(
                units = Constants.UNITS_METRIC,
                language = "en",
                locationSource = LocationSource.GPS,
                windSpeedUnit = WindSpeedUnit.MPS,
                homeLat = 0.0,
                homeLon = 0.0
            )
        )

        viewModel = SettingsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun updateUnits_imperial_callsSaveUnitsAndWindWithMPH() {

        viewModel.updateUnits(Constants.UNITS_IMPERIAL)

        coVerify  {
            repository.saveUnitsAndWind(Constants.UNITS_IMPERIAL, WindSpeedUnit.MPH)
        }
    }

    @Test
    fun updateUnits_metric_callsSaveUnitsAndWindWithMPS() {
        viewModel.updateUnits(Constants.UNITS_METRIC)

        coVerify {
            repository.saveUnitsAndWind(Constants.UNITS_METRIC, WindSpeedUnit.MPS)
        }
    }

    @Test
    fun updateWindSpeedUnit_MPH_callsSaveUnitsAndWindImperial() {
        viewModel.updateWindSpeedUnit(WindSpeedUnit.MPH)

        coVerify {
            repository.saveUnitsAndWind(Constants.UNITS_IMPERIAL, WindSpeedUnit.MPH)
        }
    }

    @Test
    fun updateLanguage_callsSaveLanguage() {
        viewModel.updateLanguage("ar")

        coVerify { repository.saveLanguage("ar") }
    }

    @Test
    fun updateLocationSource_callsSaveLocationSource() {
        viewModel.updateLocationSource(LocationSource.MAP)

        coVerify { repository.saveLocationSource(LocationSource.MAP) }
    }

    @Test
    fun updateHomeLocation_callsSaveHomeLocation() {
        viewModel.updateHomeLocation(10.5, 20.25)

        coVerify { repository.saveHomeLocation(10.5, 20.25) }
    }
}