package com.okladnikov.yadroweather.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
}