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

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.edwardmuturi.forecast.FakerForecastRepository
import com.edwardmuturi.forecast.domain.usecase.GetCurrentWeatherForecastUseCase
import com.edwardmuturi.forecast.domain.usecase.GetFiveDayWeatherForecastUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForecastScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fakerForecastRepository = FakerForecastRepository()
    private val getFiveDayWeatherForecastUseCase =
        GetFiveDayWeatherForecastUseCase(fakerForecastRepository)
    private val getCurrentWeatherForecastUseCase =
        GetCurrentWeatherForecastUseCase(fakerForecastRepository)
    private val viewModel: ForecastViewModel =
        ForecastViewModel(getCurrentWeatherForecastUseCase, getFiveDayWeatherForecastUseCase)

    @Before
    fun setUp() {
        composeTestRule.setContent {
            ForecastScreen(
                latitude = 44.34,
                longitude = 10.99,
                forecastViewModel = viewModel
            )
        }
    }

    @Test
    fun display_weather_type_image() {
        val weatherTypeSample = "moderate rain".uppercase()
        composeTestRule.onNodeWithContentDescription("weatherTypeImage").assertIsDisplayed()
        composeTestRule.onNodeWithText(weatherTypeSample).assertIsDisplayed()
    }

    @Test
    fun display_current_forecast() {
        val sampleMaxTemperatureFromSampleResponse = "300.05°"
        val sampleMinTemperatureFromSampleResponse = "297.56°"
        val sampleCurrentTemperatureFromSampleResponse = "298.74°"

        composeTestRule.onNodeWithText(sampleMaxTemperatureFromSampleResponse).assertIsDisplayed()
        composeTestRule.onNodeWithText(sampleCurrentTemperatureFromSampleResponse).assertIsDisplayed()
        composeTestRule.onNodeWithText(sampleMinTemperatureFromSampleResponse).assertIsDisplayed()
    }

    @Test
    fun display_five_day_forecast() {
        val sampleMaxTemperatureFromSampleResponse = "297.87°"
        val sampleMaxTemperatureFromSampleResponse1 = "295.45°"
        val sampleMaxTemperatureFromSampleResponse2 = "292.46°"
        val sampleMaxTemperatureFromSampleResponse3 = "294.93°"

        composeTestRule.onNodeWithText(sampleMaxTemperatureFromSampleResponse).assertIsDisplayed()
        composeTestRule.onNodeWithText(sampleMaxTemperatureFromSampleResponse1).assertIsDisplayed()
        composeTestRule.onNodeWithText(sampleMaxTemperatureFromSampleResponse2).assertIsDisplayed()
        composeTestRule.onNodeWithText(sampleMaxTemperatureFromSampleResponse3).assertIsDisplayed()
    }
}
