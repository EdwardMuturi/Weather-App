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
import com.edwardmuturi.location.domain.usecase.GetCurrentLocationUseCase
import com.edwardmuturi.location.domain.usecase.LocationUiState
import com.edwardmuturi.location.presentation.getlocationinfo.LocationDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getCurrentWeatherForecastUseCase: GetCurrentWeatherForecastUseCase,
    private val getFiveDayWeatherForecastUseCase: GetFiveDayWeatherForecastUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase
) : ViewModel() {
    val currentLocation = getCurrentLocationUseCase().map {
        _forecastUiState.update { fs ->
//            if (fs.currentLocation.longitude != null && fs.currentLocation.latitude != null) {
            fs.copy(
                currentLocation = LocationDetails(
                    latitude = fs.currentLocation?.latitude,
                    longitude = fs.currentLocation?.longitude
                )
            )
//            }
        }
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = LocationUiState()
    )

    private val _currentForecastUiState: MutableState<ForecastUiState> = mutableStateOf(ForecastUiState())
    val currentForecastUiState: State<ForecastUiState> = _currentForecastUiState

    private val _forecastUiState: MutableStateFlow<ForecastScreenUiState> = MutableStateFlow(ForecastScreenUiState())
    val forecastUiState: StateFlow<ForecastScreenUiState> = _forecastUiState

    private val _fiveDayForecastUiState: MutableState<FiveDayForecastUiState> = mutableStateOf(FiveDayForecastUiState())
    val fiveDayForecastUiState: State<FiveDayForecastUiState> = _fiveDayForecastUiState

    fun loadCurrentLocation() {
        viewModelScope.launch {
            Timber.d("Loading current location")
            getCurrentLocationUseCase().collectLatest { currentLocation ->
                if (currentLocation.longitude != null && currentLocation.latitude != null) {
                    Timber.d("Loading current location success!! $currentLocation")
                    _forecastUiState.update {
                        it.copy(
                            currentLocation = LocationDetails(
                                latitude = currentLocation.latitude as Double,
                                longitude = currentLocation.longitude as Double
                            )
                        )
                    }
                }
            }
        }
    }

    fun loadForecast(latitude: Double, longitude: Double) {
        Timber.d("Loading forecast From current location $latitude, $longitude")
        loadCurrentDayForecast(
            latitude = latitude,
            longitude = longitude
        )
        loadFiveDayForecast(
            latitude = latitude,
            longitude = longitude
        )
    }

    fun loadCurrentDayForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _currentForecastUiState.value = ForecastUiState(isLoading = true)
            _forecastUiState.value = ForecastScreenUiState(isLoading = true)
            getCurrentWeatherForecastUseCase(lat = latitude.toString(), lon = longitude.toString())
                .collectLatest {
                    _currentForecastUiState.value = it
                    _forecastUiState.update { f ->
                        f.copy(isLoading = it.isLoading, currentDayForecastUiState = it)
                    }
                }
        }
    }

    fun loadFiveDayForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _fiveDayForecastUiState.value = FiveDayForecastUiState(isLoading = true)
            getFiveDayWeatherForecastUseCase(lat = latitude, lon = longitude).collectLatest {
                _fiveDayForecastUiState.value = it
                _forecastUiState.update { f ->
                    f.copy(fiveDayForecastUiState = it)
                }
            }
        }
    }
}

data class ForecastScreenUiState(
    val currentDayForecastUiState: ForecastUiState = ForecastUiState(),
    val fiveDayForecastUiState: FiveDayForecastUiState = FiveDayForecastUiState(),
    val currentLocation: LocationDetails? = null,
    val message: String? = null,
    val isLoading: Boolean = false
)
