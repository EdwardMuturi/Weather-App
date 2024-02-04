package com.edwardmuturi.forecast.domain

import com.edwardmuturi.forecast.data.remote.dto.FetchCurrentWeatherDataDto
import com.edwardmuturi.forecast.data.remote.dto.FetchFiveDayForecastDto
import kotlinx.coroutines.flow.Flow

/**
 * 1. Set up repository
 * 2. Set up response class
 * 3. Set up safeApiCall
 * 4. Implement Repository interface
 * 5. Bind repository interface with implementation
 * 6.
 *
 * */
interface ForeCastRepository {

    fun getCurrentDayForecast(lat: String, lon: String): Flow<Result<FetchCurrentWeatherDataDto?>>
    fun getFiveDayForecast(lat: String, lon: String): Flow<Result<FetchFiveDayForecastDto?>>
}
