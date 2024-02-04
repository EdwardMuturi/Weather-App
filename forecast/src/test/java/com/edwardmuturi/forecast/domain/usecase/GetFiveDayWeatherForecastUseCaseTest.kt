package com.edwardmuturi.forecast.domain.usecase

import com.edwardmuturi.forecast.MainDispatcherRule
import com.edwardmuturi.forecast.data.FakerForecastRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetFiveDayWeatherForecastUseCaseTest {
    private val fakerForecastRepository = FakerForecastRepository()
    private lateinit var getFiveDayWeatherForecastUseCase: GetFiveDayWeatherForecastUseCase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        getFiveDayWeatherForecastUseCase = GetFiveDayWeatherForecastUseCase(fakerForecastRepository)
    }

    @Test
    fun `fetch five day forecast successfully`() = runTest {
        val lat = 44.34
        val lon = 10.99

        val result = getFiveDayWeatherForecastUseCase(lat = lat, lon = lon)

        assert(result.first().getOrNull()?.forecasts?.isNotEmpty() == true)
    }
}
