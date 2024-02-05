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
package com.edwardmuturi.forecast
import com.edwardmuturi.forecast.data.remote.dto.FetchCurrentWeatherDataDto
import com.edwardmuturi.forecast.data.remote.dto.FetchFiveDayForecastDto
import com.edwardmuturi.forecast.domain.repository.ForeCastRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakerForecastRepository : ForeCastRepository {
    private var result: Result<FetchCurrentWeatherDataDto?> = Result.success(currentForecast)
    private var fiveDayForecastResult: Result<FetchFiveDayForecastDto?> = Result.success(
        fetchFiveDayForecastDto
    )

    fun setSuccessfullResult() {
        result = Result.success(currentForecast)
    }

    fun setSuccessfullNullResult() {
        result = Result.success(null)
        fiveDayForecastResult = Result.success(null)
    }

    fun setErrorResult() {
        result = Result.failure(Exception("Failed to parse response body"))
        fiveDayForecastResult = Result.failure(Exception("502 Bad Gateway Error"))
    }

    override fun getCurrentDayForecast(lat: String, lon: String): Flow<Result<FetchCurrentWeatherDataDto?>> {
        return flow { emit(result) }
    }

    override fun getFiveDayForecast(lat: String, lon: String): Flow<Result<FetchFiveDayForecastDto?>> {
        return flow { emit(fiveDayForecastResult) }
    }
}

val currentForecastString = """
{
  "coord": {
    "lon": 10.99,
    "lat": 44.34
  },
  "weather": [
    {
      "id": 501,
      "main": "Rain",
      "description": "moderate rain",
      "icon": "10d"
    }
  ],
  "base": "stations",
  "main": {
    "temp": 298.48,
    "feels_like": 298.74,
    "temp_min": 297.56,
    "temp_max": 300.05,
    "pressure": 1015,
    "humidity": 64,
    "sea_level": 1015,
    "grnd_level": 933
  },
  "visibility": 10000,
  "wind": {
    "speed": 0.62,
    "deg": 349,
    "gust": 1.18
  },
  "rain": {
    "1h": 3.16
  },
  "clouds": {
    "all": 100
  },
  "dt": 1661870592,
  "sys": {
    "type": 2,
    "id": 2075663,
    "country": "IT",
    "sunrise": 1661834187,
    "sunset": 1661882248
  },
  "timezone": 7200,
  "id": 3163858,
  "name": "Zocca",
  "cod": 200
}                        
""".trimIndent()

val currentForecast: FetchCurrentWeatherDataDto =
    Gson().fromJson(currentForecastString, FetchCurrentWeatherDataDto::class.java)

val fiveDayForecastString = """
    {
      "cod": "200",
      "message": 0,
      "cnt": 40,
      "list": [
        {
          "dt": 1661871600,
          "main": {
            "temp": 296.76,
            "feels_like": 296.98,
            "temp_min": 296.76,
            "temp_max": 297.87,
            "pressure": 1015,
            "sea_level": 1015,
            "grnd_level": 933,
            "humidity": 69,
            "temp_kf": -1.11
          },
          "weather": [
            {
              "id": 500,
              "main": "Rain",
              "description": "light rain",
              "icon": "10d"
            }
          ],
          "clouds": {
            "all": 100
          },
          "wind": {
            "speed": 0.62,
            "deg": 349,
            "gust": 1.18
          },
          "visibility": 10000,
          "pop": 0.32,
          "rain": {
            "3h": 0.26
          },
          "sys": {
            "pod": "d"
          },
          "dt_txt": "2022-08-30 15:00:00"
        },
        {
          "dt": 1661882400,
          "main": {
            "temp": 295.45,
            "feels_like": 295.59,
            "temp_min": 292.84,
            "temp_max": 295.45,
            "pressure": 1015,
            "sea_level": 1015,
            "grnd_level": 931,
            "humidity": 71,
            "temp_kf": 2.61
          },
          "weather": [
            {
              "id": 500,
              "main": "Rain",
              "description": "light rain",
              "icon": "10n"
            }
          ],
          "clouds": {
            "all": 96
          },
          "wind": {
            "speed": 1.97,
            "deg": 157,
            "gust": 3.39
          },
          "visibility": 10000,
          "pop": 0.33,
          "rain": {
            "3h": 0.57
          },
          "sys": {
            "pod": "n"
          },
          "dt_txt": "2022-08-30 18:00:00"
        },
        {
          "dt": 1661893200,
          "main": {
            "temp": 292.46,
            "feels_like": 292.54,
            "temp_min": 290.31,
            "temp_max": 292.46,
            "pressure": 1015,
            "sea_level": 1015,
            "grnd_level": 931,
            "humidity": 80,
            "temp_kf": 2.15
          },
          "weather": [
            {
              "id": 500,
              "main": "Rain",
              "description": "light rain",
              "icon": "10n"
            }
          ],
          "clouds": {
            "all": 68
          },
          "wind": {
            "speed": 2.66,
            "deg": 210,
            "gust": 3.58
          },
          "visibility": 10000,
          "pop": 0.7,
          "rain": {
            "3h": 0.49
          },
          "sys": {
            "pod": "n"
          },
          "dt_txt": "2022-08-30 21:00:00"
        },
        {
          "dt": 1662292800,
          "main": {
            "temp": 294.93,
            "feels_like": 294.83,
            "temp_min": 294.93,
            "temp_max": 294.93,
            "pressure": 1018,
            "sea_level": 1018,
            "grnd_level": 935,
            "humidity": 64,
            "temp_kf": 0
          },
          "weather": [
            {
              "id": 804,
              "main": "Clouds",
              "description": "overcast clouds",
              "icon": "04d"
            }
          ],
          "clouds": {
            "all": 88
          },
          "wind": {
            "speed": 1.14,
            "deg": 17,
            "gust": 1.57
          },
          "visibility": 10000,
          "pop": 0,
          "sys": {
            "pod": "d"
          },
          "dt_txt": "2022-09-04 12:00:00"
        }
      ],
      "city": {
        "id": 3163858,
        "name": "Zocca",
        "coord": {
          "lat": 44.34,
          "lon": 10.99
        },
        "country": "IT",
        "population": 4593,
        "timezone": 7200,
        "sunrise": 1661834187,
        "sunset": 1661882248
      }
    }
""".trimIndent()

val fetchFiveDayForecastDto: FetchFiveDayForecastDto =
    Gson().fromJson(fiveDayForecastString, FetchFiveDayForecastDto::class.java)
