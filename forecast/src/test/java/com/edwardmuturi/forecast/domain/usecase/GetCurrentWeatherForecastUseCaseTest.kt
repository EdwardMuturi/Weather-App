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
package com.edwardmuturi.forecast.domain.usecase

import com.edwardmuturi.forecast.MainDispatcherRule
import com.edwardmuturi.forecast.data.FakerForecastRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetCurrentWeatherForecastUseCaseTest {

    private val fakerForecastRepository = FakerForecastRepository()
    private lateinit var getCurrentWeatherForecastUseCase: GetCurrentWeatherForecastUseCase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        getCurrentWeatherForecastUseCase = GetCurrentWeatherForecastUseCase(fakerForecastRepository)
    }

    @Test
    fun `get current weather forecast returns successful result`() = runTest {
        val lat = "44.34"
        val result = getCurrentWeatherForecastUseCase(lat = lat, lon = "10.99")
        assert(
            result.first().forecast?.location?.latitude == lat.toDouble() &&
                result.first().forecast?.type == "moderate rain"
        )
    }

    @Test
    fun `return custom error message when body is null`() = runTest {
        val lat = "44.34"
        val lon = "10.99"
        fakerForecastRepository.setSuccessfullNullResult()
        val result = getCurrentWeatherForecastUseCase(lat = lat, lon = lon)

        assert(result.first().message == "No results fetched, empty results returned")
    }

    @Test
    fun `return error message when fetch current weather fails`() = runTest {
        val lat = "44.34"
        val lon = "10.99"
        fakerForecastRepository.setErrorResult()
        val result = getCurrentWeatherForecastUseCase(lat = lat, lon = lon)

        assert(result.first().message == "Failed to parse response body")
    }
}
