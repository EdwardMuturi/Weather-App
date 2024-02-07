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
package com.edwardmuturi.forecast.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edwardmuturi.forecast.domain.usecase.FiveDayForecastUiState
import com.edwardmuturi.forecast.domain.usecase.ForecastUiState
import com.edwardmuturi.forecast.domain.usecase.GetCurrentWeatherForecastUseCase
import com.edwardmuturi.forecast.domain.usecase.GetFiveDayWeatherForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getCurrentWeatherForecastUseCase: GetCurrentWeatherForecastUseCase,
    private val getFiveDayWeatherForecastUseCase: GetFiveDayWeatherForecastUseCase
) : ViewModel() {
    private val _currentForecastUiState: MutableState<ForecastUiState> =
        mutableStateOf(ForecastUiState())
    val currentForecastUiState: State<ForecastUiState> = _currentForecastUiState

    private val _fiveDayForecastUiState: MutableState<FiveDayForecastUiState> =
        mutableStateOf(FiveDayForecastUiState())
    val fiveDayForecastUiState: State<FiveDayForecastUiState> = _fiveDayForecastUiState

    fun loadCurrentDayForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _currentForecastUiState.value = ForecastUiState(isLoading = true)
            getCurrentWeatherForecastUseCase(lat = latitude.toString(), lon = longitude.toString())
                .collectLatest {
                    _currentForecastUiState.value = it
                }
        }
    }

    fun loadFiveDayForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _fiveDayForecastUiState.value = FiveDayForecastUiState(isLoading = true)
            getFiveDayWeatherForecastUseCase(lat = latitude, lon = longitude).collectLatest {
                _fiveDayForecastUiState.value = it
            }
        }
    }
}
