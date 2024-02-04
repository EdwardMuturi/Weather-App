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
                            Forecast(
                                min = forecastResult.main.tempMin,
                                max = forecastResult.main.tempMax,
                                type = forecastResult.weather.first().description,
                                day = "Tuesday",
                                location = ForecastLocation(
                                    latitude = forecastResult.coord.lat,
                                    longitude = forecastResult.coord.lon,
                                    name = forecastResult.name
                                )
                            )
                        )
                    }
                }

                false -> TODO()
            }
        }
    }
}
