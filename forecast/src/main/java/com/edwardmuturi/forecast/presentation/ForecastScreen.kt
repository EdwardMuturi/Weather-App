package com.edwardmuturi.forecast.presentation

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ForecastScreen(forecastViewModel: ForecastViewModel = viewModel()) {
    LaunchedEffect(key1 = true, block = {
        forecastViewModel.loadCurrentDayForecast()
    })
    Scaffold {
        Text(text = "Hello Forecast")
    }
}
