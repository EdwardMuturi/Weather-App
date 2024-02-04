package com.edwardmuturi.forecast.di

import com.edwardmuturi.forecast.data.ForecastRepositoryImpl
import com.edwardmuturi.forecast.domain.ForeCastRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun provideForecastRepository(
        forecastRepositoryImpl: ForecastRepositoryImpl
    ): ForeCastRepository
}
