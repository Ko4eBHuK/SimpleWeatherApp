package com.okladnikov.yadroweather.navigation

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.okladnikov.yadroweather.MainActivity
import com.okladnikov.yadroweather.home.HomeViewModel
import com.okladnikov.yadroweather.home.screen.HomeScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    activity: MainActivity
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            val homeViewModel: HomeViewModel by activity.viewModels()
            HomeScreen(viewModel = homeViewModel)
        }
    }
}
