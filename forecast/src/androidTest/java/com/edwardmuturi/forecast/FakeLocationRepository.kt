package com.edwardmuturi.forecast

import com.edwardmuturi.location.domain.repository.LocationRepository
import com.edwardmuturi.weatherapp.storage.location.entity.LocationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocationRepository : LocationRepository {
    override suspend fun saveLocation(location: LocationEntity) {
        TODO("Not yet implemented")
    }

    override fun getCurrentLocation(): Flow<LocationEntity?> {
        return flow { emit(LocationEntity(latitude = 44.34, longitude = 10.99, isFavourite = false, isCurrent = true, name = null)) }
    }

    override suspend fun updateLocationDetails(locationEntity: LocationEntity) {
        TODO("Not yet implemented")
    }
}
