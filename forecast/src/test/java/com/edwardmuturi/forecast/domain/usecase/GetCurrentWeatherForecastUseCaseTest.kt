package com.edwardmuturi.forecast.domain.usecase

import com.edwardmuturi.forecast.MainDispatcherRule
import com.edwardmuturi.forecast.data.remote.dto.FetchCurrentWeatherDataDto
import com.edwardmuturi.forecast.data.remote.dto.FetchFiveDayForecastDto
import com.edwardmuturi.forecast.domain.repository.ForeCastRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class GetCurrentWeatherForecastUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val getCurrentWeatherForecastUseCase =
        GetCurrentWeatherForecastUseCase(FakerForecastRepository())

    @Test
    fun `get current weather forecast returns successful result`() = runTest {
        val lat = "44.34"
        val result = getCurrentWeatherForecastUseCase(lat = lat, lon = "10.99")
        assert(
            result.first().location.latitude == lat.toDouble() &&
                result.first().type == "moderate rain"
        )
    }
}

class FakerForecastRepository : ForeCastRepository {
    override fun getCurrentDayForecast(
        lat: String,
        lon: String
    ): Flow<Result<FetchCurrentWeatherDataDto?>> {
        return flow { emit(Result.success(currentForecast)) }
    }

    override fun getFiveDayForecast(
        lat: String,
        lon: String
    ): Flow<Result<FetchFiveDayForecastDto?>> {
        TODO("Not yet implemented")
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

val currentForecast = Gson().fromJson(currentForecastString, FetchCurrentWeatherDataDto::class.java)
