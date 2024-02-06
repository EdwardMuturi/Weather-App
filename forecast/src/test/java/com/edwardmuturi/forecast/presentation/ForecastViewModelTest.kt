/*
 * Copyright 2024
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
