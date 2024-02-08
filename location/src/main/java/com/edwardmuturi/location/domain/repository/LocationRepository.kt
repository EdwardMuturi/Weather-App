package com.edwardmuturi.location.domain.repository

import com.edwardmuturi.weatherapp.storage.location.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun saveLocation(location: LocationEntity)
    fun getCurrentLocation(): Flow<LocationEntity>
}
