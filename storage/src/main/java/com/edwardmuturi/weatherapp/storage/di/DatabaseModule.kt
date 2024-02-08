package com.edwardmuturi.weatherapp.storage.di

import android.content.Context
import androidx.room.Room
import com.edwardmuturi.weatherapp.storage.WeatherAppDatabase
import com.edwardmuturi.weatherapp.storage.forecast.dao.ForecastDao
import com.edwardmuturi.weatherapp.storage.location.dao.LocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherAppDatabase {
        return Room.databaseBuilder(
            context,
            WeatherAppDatabase::class.java,
            "weather.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocationDao(weatherAppDatabase: WeatherAppDatabase): LocationDao {
        return weatherAppDatabase.locationDao()
    }

    @Provides
    @Singleton
    fun provideForecastDao(weatherAppDatabase: WeatherAppDatabase): ForecastDao {
        return weatherAppDatabase.forecastDao()
    }
}
