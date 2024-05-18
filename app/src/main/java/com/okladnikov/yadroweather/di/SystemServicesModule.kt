package com.okladnikov.yadroweather.di

import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SystemServicesModule {
    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Provides
    @Singleton
    fun provideFusedLocationClient(@ApplicationContext context: Context) =
        LocationServices.getFusedLocationProviderClient(context)
}