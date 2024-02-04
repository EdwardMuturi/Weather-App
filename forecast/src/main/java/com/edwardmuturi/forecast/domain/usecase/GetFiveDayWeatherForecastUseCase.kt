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
                send(
                    Result.success(
                        FiveDayForecastUiState(
                            message = "Five day forecast fetched successfully",
                            isLoading = false,
                            forecasts = result.getOrNull()?.list?.map { f ->
                                Forecast(
                                    type = f.weather.first().description,
                                    min = f.main.tempMin,
                                    max = f.main.tempMax,
                                    day = "",
                                    location = ForecastLocation(
                                        latitude = result.getOrNull()?.city?.coord?.lat ?: 0.00,
                                        longitude = result.getOrNull()?.city?.coord?.lon ?: 0.00,
                                        name = result.getOrNull()?.city?.name
                                    )
                                )
                            } ?: emptyList()
                        )
                    )
                )
            }
    }
}

data class FiveDayForecastUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val forecasts: List<Forecast> = emptyList()
)
