package com.edwardmuturi.forecast.domain.entity

import com.edwardmuturi.location.domain.entity.ForecastLocation

data class Forecast(
    val type: String,
    val min: Double,
    val max: Double,
    val day: String,
    val location: ForecastLocation
)
