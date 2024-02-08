package com.edwardmuturi.di

import com.edwardmuturi.location.data.LocationRepositoryImpl
import com.edwardmuturi.location.domain.repository.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationDataModule {
    @Binds
    @Singleton
    abstract fun provideLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository
}
