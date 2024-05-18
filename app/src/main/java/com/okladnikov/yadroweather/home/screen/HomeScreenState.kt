package com.okladnikov.yadroweather.home.screen

data class HomeScreenState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isMessage: Boolean = false,
    val message: String = "",
    val locationInfo: String = "",
    val temperature: String = "",
    val weeklyForecast: List<DayInfo> = listOf()
) {
    data class DayInfo(
        val name: String,
        val temperatureMin: String,
        val temperatureMax: String
    )
}
