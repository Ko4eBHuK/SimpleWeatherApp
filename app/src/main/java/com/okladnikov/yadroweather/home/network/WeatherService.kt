package com.okladnikov.yadroweather.home.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    /**
     * https://open-meteo.com/en/docs
     * */
    @GET("forecast?daily=temperature_2m_max,temperature_2m_min")
    suspend fun getWeeklyForecastMinMax(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("timezone") timezone: String,
    ): Response<WeeklyForecast>

    /**
     * https://open-meteo.com/en/docs
     * */
    @GET("forecast?current=temperature_2m")
    suspend fun getCurrentTemperature(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("timezone") timezone: String,
    ): Response<CurrentWeatherConditions>
}
