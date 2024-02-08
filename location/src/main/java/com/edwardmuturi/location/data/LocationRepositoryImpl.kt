package com.edwardmuturi.location.data

import com.edwardmuturi.location.domain.repository.LocationRepository
import com.edwardmuturi.weatherapp.storage.location.dao.LocationDao
import com.edwardmuturi.weatherapp.storage.location.entity.LocationEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl @Inject constructor(private val locationDao: LocationDao) : LocationRepository {
    override suspend fun saveLocation(location: LocationEntity) {
        locationDao.insert(location)
    }

    override fun getCurrentLocation(): Flow<LocationEntity> {
        return locationDao.getCurrentLocation()
    }
}
