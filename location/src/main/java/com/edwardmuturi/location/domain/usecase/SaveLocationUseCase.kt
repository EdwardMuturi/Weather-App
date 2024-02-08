package com.edwardmuturi.location.domain.usecase

import com.edwardmuturi.location.domain.repository.LocationRepository
import com.edwardmuturi.weatherapp.storage.location.entity.LocationEntity
import javax.inject.Inject

class SaveLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {
    suspend operator fun invoke(locationUiState: LocationUiState) {
        locationRepository.saveLocation(location = locationUiState.toLocationEntity())
    }
}

data class LocationUiState(
    val name: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isCurrent: Boolean = false,
    val isFavourite: Boolean = false
)

fun LocationUiState.toLocationEntity() = LocationEntity(
    isCurrent = isCurrent,
    isFavourite = isFavourite,
    latitude = latitude,
    longitude = longitude,
    name = name
)
