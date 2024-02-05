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

import com.edwardmuturi.forecast.domain.entity.Forecast
import com.edwardmuturi.forecast.domain.repository.ForeCastRepository
import com.edwardmuturi.location.domain.entity.ForecastLocation
import javax.inject.Inject
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class GetCurrentWeatherForecastUseCase @Inject constructor(
    private val foreCastRepository: ForeCastRepository
) {
    operator fun invoke(lat: String, lon: String) = channelFlow {
        foreCastRepository.getCurrentDayForecast(lat = lat, lon = lon).collectLatest { result ->
            when (result.isSuccess) {
                true -> {
                    val forecastResult = result.getOrNull()
                    if (forecastResult != null) {
                        send(
                            ForecastUiState(
                                isLoading = false,
                                message = "Successfully fetched current weather forecast",
                                forecast = Forecast(
                                    min = forecastResult.main.tempMin,
                                    max = forecastResult.main.tempMax,
                                    currentTemp = forecastResult.main.feelsLike,
                                    type = forecastResult.weather.first().description,
                                    day = "",
                                    location = ForecastLocation(
                                        latitude = forecastResult.coord.lat,
                                        longitude = forecastResult.coord.lon,
                                        name = forecastResult.name
                                    )
                                )
                            )
                        )
                    } else {
                        send(
                            ForecastUiState(
                                message = "No results fetched, empty results returned",
                                isLoading = false
                            )
                        )
                    }
                }

                false -> send(
                    ForecastUiState(
                        message = result.exceptionOrNull()?.message,
                        isLoading = false
                    )
                )
            }
        }
    }
}

data class ForecastUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val forecast: Forecast? = null
)
