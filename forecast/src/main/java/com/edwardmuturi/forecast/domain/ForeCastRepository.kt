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
