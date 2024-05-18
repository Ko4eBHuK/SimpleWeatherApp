package com.okladnikov.yadroweather.home.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {
    /**
     * https://www.bigdatacloud.com/free-api/free-reverse-geocode-to-city-api
     * */
    @GET("data/reverse-geocode-client")
    suspend fun getLocationName(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Response<LocationDescription>
}
