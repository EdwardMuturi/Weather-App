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

class GetFiveDayWeatherForecastUseCase @Inject constructor(
    private val foreCastRepository: ForeCastRepository
) {
    operator fun invoke(lat: Double, lon: Double) = channelFlow {
        foreCastRepository.getFiveDayForecast(lat = lat.toString(), lon = lon.toString())
            .collectLatest { result ->
                when (result.isSuccess) {
                    true -> {
                        val fiveDayForecast = result.getOrNull()
                        if (fiveDayForecast != null) {
                            send(
                                FiveDayForecastUiState(
                                    message = "Five day forecast fetched successfully",
                                    isLoading = false,
                                    forecasts = fiveDayForecast.list.map { f ->
                                        Forecast(
                                            type = f.weather.first().description,
                                            min = f.main.tempMin,
                                            max = f.main.tempMax,
                                            day = "",
                                            location = ForecastLocation(
                                                latitude = fiveDayForecast.city.coord.lat,
                                                longitude = fiveDayForecast.city.coord.lon,
                                                name = fiveDayForecast.city.name
                                            )
                                        )
                                    }
                                )
                            )
                        } else {
                            send(
                                FiveDayForecastUiState(
                                    message = "Failed to load 5 day forecast, please try again later"
                                )
                            )
                        }
                    }

                    false -> send(
                        FiveDayForecastUiState(
                            message = result.exceptionOrNull()?.message
                                ?: "Failed to fetch five day forecast"
                        )
                    )
                }
            }
    }
}

data class FiveDayForecastUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val forecasts: List<Forecast> = emptyList()
)
