package com.okladnikov.yadroweather.home.screen

sealed class HomeScreenIntent {
    data object Refresh : HomeScreenIntent()
    data object CloseError : HomeScreenIntent()
}
