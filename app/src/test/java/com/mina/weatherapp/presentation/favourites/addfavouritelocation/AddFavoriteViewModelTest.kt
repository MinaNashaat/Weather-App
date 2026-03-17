package com.mina.weatherapp.presentation.favourites.addfavouritelocation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.base.Predicates.instanceOf
import com.mina.weatherapp.data.weather.WeatherRepository
import com.mina.weatherapp.data.weather.datasource.local.entity.FavoriteLocationEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.bouncycastle.util.test.SimpleTest.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.junit.After
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.Rule

class AddFavoriteViewModelTest {
    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: WeatherRepository
    private lateinit var viewModel: AddFavoriteViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = AddFavoriteViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun setSelectedLocation_updatesSelectedLocation() {
        viewModel.setSelectedLocation(10.0, 20.0)

        val loc = viewModel.selectedLocation.value

        assertThat(loc?.latitude, `is`(10.0))
        assertThat(loc?.longitude, `is`(20.0))
    }

    @Test
    fun resetScreen_resetsUiStateAndClearsLocation() {
        viewModel.setSelectedLocation(10.0, 20.0)
        viewModel.saveFavorite("Cairo")

        viewModel.resetScreen()

        assertThat(viewModel.uiState.value ,`is`(AddFavoriteUiState.Idle))
        assertThat(viewModel.selectedLocation.value , nullValue())

    }

    @Test
    fun saveFavorite_noLocation_setsError() {
        viewModel.saveFavorite("Cairo")

        val state = viewModel.uiState.value
        assertThat((state as AddFavoriteUiState.Error).message, `is`("Please choose a location on the map")
        )
    }

    @Test
    fun saveFavorite_blankCity_setsError() = runTest {
        viewModel.setSelectedLocation(10.0, 20.0)

        viewModel.saveFavorite("")

        val state = viewModel.uiState.value
        assertThat((state as AddFavoriteUiState.Error).message, `is`("Please enter a city name"))
    }

    @Test
    fun saveFavorite_validInput_callsRepository_andEmitsSavingThenSuccess() {
        viewModel.setSelectedLocation(10.0, 20.0)
        coEvery { repository.insertFavoriteLocation(any()) } returns 1

        viewModel.saveFavorite("Cairo")

        coVerify(exactly = 1) {
            repository.insertFavoriteLocation(
                FavoriteLocationEntity(
                    cityName = "Cairo",
                    latitude = 10.0,
                    longitude = 20.0
                )
            )
        }
    }

    @Test
    fun saveFavorite_repositoryThrowsException_emitsErrorState() {

        viewModel.setSelectedLocation(10.0, 20.0)

        coEvery { repository.insertFavoriteLocation(any()) } throws RuntimeException("DB error")

        viewModel.saveFavorite("Cairo")


        val state = viewModel.uiState.value
        assert(state is AddFavoriteUiState.Error)
        assert((state as AddFavoriteUiState.Error).message == "DB error")
    }
}