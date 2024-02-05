package com.edwardmuturi.forecast.presentation

import com.edwardmuturi.forecast.MainDispatcherRule
import com.edwardmuturi.forecast.data.FakerForecastRepository
import com.edwardmuturi.forecast.domain.usecase.GetCurrentWeatherForecastUseCase
import com.edwardmuturi.forecast.domain.usecase.GetFiveDayWeatherForecastUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForecastViewModelTest {
    private val fakerForecastRepository = FakerForecastRepository()
    private val getFiveDayWeatherForecastUseCase =
        GetFiveDayWeatherForecastUseCase(fakerForecastRepository)
    private val getCurrentWeatherForecastUseCase =
        GetCurrentWeatherForecastUseCase(fakerForecastRepository)
    private lateinit var viewModel: ForecastViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        viewModel = ForecastViewModel(getCurrentWeatherForecastUseCase, getFiveDayWeatherForecastUseCase)
    }

    @Test
    fun `load current weather forecast ui state successfully`() {
        val lat = 44.34
        val lon = 10.99

        viewModel.loadCurrentDayForecast(latitude = lat, longitude = lon)

        assert(viewModel.currentForecastUiState.value.forecast?.location?.latitude == lat)
    }

    @Test
    fun `load 5 day weather forecast ui state successfully`() {
        val lat = 44.34
        val lon = 10.99

        viewModel.loadFiveDayForecast(latitude = lat, longitude = lon)

        assert(
            viewModel.fiveDayForecastUiState.value.forecasts.isNotEmpty() &&
                viewModel.fiveDayForecastUiState.value.forecasts.first().location.latitude == lat
        )
    }
}
