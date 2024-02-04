package com.edwardmuturi.forecast.data

import com.edwardmuturi.forecast.data.remote.api.ForecastService
import com.edwardmuturi.forecast.data.remote.dto.FetchCurrentWeatherDataDto
import com.edwardmuturi.forecast.data.remote.dto.FetchFiveDayForecastDto
import com.edwardmuturi.forecast.domain.ForeCastRepository
import com.edwardmuturi.network.utils.ApiCaller.safeApiCall
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ForecastRepositoryImpl @Inject constructor(private val forecastService: ForecastService) :
    ForeCastRepository {
    override fun getCurrentDayForecast(
        lat: String,
        lon: String
    ): Flow<Result<FetchCurrentWeatherDataDto?>> {
        return flow {
            try {
                val result = forecastService.getCurrentWeatherData(lat = lat, lon = lon)

                when (result.isSuccessful) {
                    true -> emit(Result.success(result.body()))
                    false -> emit(Result.failure(IllegalStateException(result.message())))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getFiveDayForecast(
        lat: String,
        lon: String
    ): Flow<Result<FetchFiveDayForecastDto?>> {
        return flow {
            /*try {
                val result = forecastService.getFiveDayForecast(lat = "", lon = "")

                when (result.isSuccessful) {
                    true -> emit(Result.success(result.body()))
                    false -> emit(Result.failure(IllegalStateException(result.message())))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }*/
            safeApiCall {
                forecastService.getFiveDayForecast(
                    lat = lat,
                    lon = lon
                )
            }
        }
    }
}
