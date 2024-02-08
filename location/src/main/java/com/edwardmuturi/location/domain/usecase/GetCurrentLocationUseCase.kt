package com.edwardmuturi.location.domain.usecase

import com.edwardmuturi.location.domain.repository.LocationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

class GetCurrentLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {
    operator fun invoke() = flow {
        locationRepository.getCurrentLocation().collectLatest {
            emit(
                LocationUiState(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    isCurrent = it.isCurrent,
                    isFavourite = it.isFavourite
                )
            )
        }
    }
}
