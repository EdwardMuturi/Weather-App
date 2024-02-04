package com.edwardmuturi.forecast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edwardmuturi.forecast.domain.ForeCastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ForecastViewModel @Inject constructor(private val foreCastRepository: ForeCastRepository) :
    ViewModel() {
    fun loadCurrentDayForecast() {
        viewModelScope.launch {
            foreCastRepository.getCurrentDayForecast(lat = "44.34", lon = "10.99")
                .collectLatest {
                    Timber.e("Result it $it")
                }
        }
    }
}
