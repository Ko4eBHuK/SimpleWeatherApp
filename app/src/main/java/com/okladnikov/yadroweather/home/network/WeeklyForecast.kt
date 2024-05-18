package com.okladnikov.yadroweather.home.network

import com.google.gson.annotations.SerializedName

data class WeeklyForecast(
    @SerializedName("daily")
    val days: Daily? = null
) {
    data class Daily(
        @SerializedName("time")
        val time: List<String>? = null,
        @SerializedName("temperature_2m_max")
        val temperatureMaxList: List<Double>? = null,
        @SerializedName("temperature_2m_min")
        val temperatureMinList: List<Double>? = null
    )
}
