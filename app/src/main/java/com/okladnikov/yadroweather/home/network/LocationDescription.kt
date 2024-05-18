package com.okladnikov.yadroweather.home.network

import com.google.gson.annotations.SerializedName

data class LocationDescription(
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("locality")
    val locality: String? = null,
)
