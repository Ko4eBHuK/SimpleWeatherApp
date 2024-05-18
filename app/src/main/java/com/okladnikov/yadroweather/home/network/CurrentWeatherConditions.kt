package com.okladnikov.yadroweather.home.network

import com.google.gson.annotations.SerializedName

data class CurrentWeatherConditions(
    @SerializedName("current")
    val conditions: Conditions? = null
) {
    data class Conditions(
        @SerializedName("temperature_2m")
        val temperature: Double? = null,
    )
}
