package com.okladnikov.yadroweather.home

import android.location.Location
import com.okladnikov.yadroweather.home.network.LocationService
import com.okladnikov.yadroweather.home.network.WeatherService
import com.okladnikov.yadroweather.utils.SystemRepository
import com.okladnikov.yadroweather.utils.simpleNetworkCallFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val weatherApi: WeatherService,
    private val locationApi: LocationService,
    private val systemRepository: SystemRepository
) {
    fun getLocation(
        onSuccess: (Location) -> Unit,
        onFailure: (String) -> Unit
    ) = systemRepository.getLocationFused(onSuccess, onFailure)

    suspend fun loadWeeklyForecastForLocation(
        latitude: Double,
        longitude: Double
    ) = simpleNetworkCallFlow(
        call = {
            weatherApi.getWeeklyForecastMinMax(
                latitude = latitude,
                longitude = longitude,
                timezone = systemRepository.getTimeZone()
            )
        },
        loadingMessage = "Load weekly forecast",
        errorMessage = "Error on loading weekly forecast",
        exceptionMessage = "Exception on loading weekly forecast"
    )

    suspend fun loadCurrentWeatherForLocation(
        latitude: Double,
        longitude: Double
    ) = simpleNetworkCallFlow(
        call = {
            weatherApi.getCurrentTemperature(
                latitude = latitude,
                longitude = longitude,
                timezone = systemRepository.getTimeZone()
            )
        },
        loadingMessage = "Load current conditions",
        errorMessage = "Error on loading current conditions",
        exceptionMessage = "Exception on loading current conditions"
    )

    suspend fun loadLocationName(latitude: Double, longitude: Double) = simpleNetworkCallFlow(
        call = {
            locationApi.getLocationName(
                latitude = latitude,
                longitude = longitude
            )
        },
        loadingMessage = "Load location info",
        errorMessage = "Error on loading location info",
        exceptionMessage = "Exception on loading location info"
    )
}
