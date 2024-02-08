package com.edwardmuturi.location.presentation.getlocationinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edwardmuturi.location.domain.usecase.LocationUiState
import com.edwardmuturi.location.domain.usecase.SaveLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LocationViewModel @Inject constructor(private val saveLocationUseCase: SaveLocationUseCase) : ViewModel() {

    fun saveCurrentLocation(location: LocationDetails) {
        viewModelScope.launch {
            saveLocationUseCase(
                locationUiState = LocationUiState(
                    longitude = location.longitude,
                    latitude = location.latitude
                )
            )
        }
    }
}
