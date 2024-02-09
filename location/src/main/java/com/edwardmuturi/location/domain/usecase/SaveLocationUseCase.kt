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
package com.edwardmuturi.location.domain.usecase

import com.edwardmuturi.location.domain.repository.LocationRepository
import com.edwardmuturi.weatherapp.storage.location.entity.LocationEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class SaveLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {
    suspend operator fun invoke(locationUiState: LocationUiState) {
        val currentLocation = locationRepository.getCurrentLocation().first()
        if (currentLocation != null) {
            locationRepository.updateLocationDetails(currentLocation.copy(isCurrent = false))
        }
        locationRepository.saveLocation(location = locationUiState.toLocationEntity())
    }
}

data class LocationUiState(
    val name: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isCurrent: Boolean = false,
    val isFavourite: Boolean = false,
    val message: String? = null
)

fun LocationUiState.toLocationEntity() = LocationEntity(
    isCurrent = isCurrent,
    isFavourite = isFavourite,
    latitude = latitude,
    longitude = longitude,
    name = name
)
